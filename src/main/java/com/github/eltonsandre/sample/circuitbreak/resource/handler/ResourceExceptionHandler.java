package com.github.eltonsandre.sample.circuitbreak.resource.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.eltonsandre.sample.circuitbreak.exception.ProdutoException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author eltonsandre
 * date 04/04/2019 18:50
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String message = this.messageSourceSafeGet("mensagem.invalida");

        final ErrorMessage erro = ErrorMessage.build(HttpStatus.BAD_REQUEST, message);
        erro.addValidationError(ex.getCause().getMessage());

        return super.handleExceptionInternal(ex, erro, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        return super.handleExceptionInternal(exception,
                this.fromBindingErrors(exception.getBindingResult()), headers, status, request);
    }

    @ExceptionHandler({ Exception.class, HttpClientErrorException.class })
    public ResponseEntity<Object> handleHttpClientErrorException(final Exception ex, final WebRequest request) {

        return super.handleExceptionInternal(ex, ErrorMessage.build(HttpStatus.BAD_REQUEST, messageSourceSafeGet(ex.getMessage())),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * <code><pre>DataIntegrityViolationException</pre></code>
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex, final WebRequest request) {
        final String message = this.messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());

        return super.handleExceptionInternal(ex, ErrorMessage.build(HttpStatus.BAD_REQUEST, message),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * @param ex BusinessException
     * @param request WebRequest
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler({ ProdutoException.class })
    public <E extends ProdutoException> ResponseEntity<Object> handleExceptionDefault(final E ex, final WebRequest request) {
        String[] messages = StringUtils.split(ex.getMessage(), ";");
        HttpStatus httpStatus = ex.getHttpStatus() == null ? HttpStatus.CONFLICT : ex.getHttpStatus();

        return super.handleExceptionInternal(ex, ErrorMessage.build(httpStatus, this.messageSourceSafeGet(messages[0])),
                new HttpHeaders(), httpStatus, request);
    }

    public ErrorMessage fromBindingErrors(final Errors errors) {
        final Object[] errorsObj = new Object[] { errors.getErrorCount() };
        final String messages = messageSourceSafeGet("recurso.falha.validacao", errorsObj);

        final ErrorMessage error = ErrorMessage.builder().message(messages).build();

        errors.getFieldErrors().forEach(objectError -> {
            log.debug("DefaultMessage: {} - Field: {} ", objectError.getDefaultMessage(), objectError.getField());
            error.addValidationError(MessageFormat.format(Objects.requireNonNull(objectError.getDefaultMessage()),
                    objectError.getField()));
        });

        return error;
    }

    public String messageSourceSafeGet(String message) {
        return this.messageSourceSafeGet(message, null);
    }

    public String messageSourceSafeGet(String message, @Nullable Object[] args) {
        try {
            return this.messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return message;
        }
    }

}

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorMessage {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Atributo status representa "status": 500,
     */
    @Builder.Default
    private Integer status = HttpStatus.BAD_REQUEST.value();

    /**
     * Atributo error representa "error": default "Internal Server Error",
     */
    @Builder.Default
    private String error = HttpStatus.BAD_REQUEST.getReasonPhrase();

    /**
     * Atributo message representa "message de erro"
     */
    private String message;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

    public static ErrorMessage build(final HttpStatus status, final String message) {
        ErrorMessage erro = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();

        if (status != null) {
            erro.setError(status.getReasonPhrase());
            erro.setStatus(status.value());
        }

        return erro;
    }

    public ErrorMessage addValidationError(final String error) {
        this.errors.add(error);
        return this;
    }

}

