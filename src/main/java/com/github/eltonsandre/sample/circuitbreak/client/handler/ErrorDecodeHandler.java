package com.github.eltonsandre.sample.circuitbreak.client.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * @author eltonsandre
 * date 29/03/2020 19:01
 */
@Slf4j
@Component
public class ErrorDecodeHandler implements ErrorDecoder {

    @Override
    public Exception decode(final String methodKey, final Response response) {
        final HttpStatus.Series series = HttpStatus.Series.resolve(response.status());

        if (HttpStatus.Series.CLIENT_ERROR.equals(series)) {
            log.error("cause=client, error={}", response.status());
            return handleHttpStatusSerie4xx(methodKey, response);

        } else if (HttpStatus.Series.SERVER_ERROR.equals(series)) {
            log.error("cause=server, error={}", response.status());
            return handleHttpStatusSerie5xx(methodKey, response);
        }
        log.info("methodkey={}, status=OK, error={}", methodKey, response.status());

        return new ErrorDecoder.Default().decode(methodKey, response);
    }

    protected Exception handleHttpStatusSerie4xx(final String methodKey, final Response response) {
        log.error(" status={}, reason={}", response.status(), response.reason());
        return new HttpClientErrorException(HttpStatus.resolve(response.status()));
    }

    protected Exception handleHttpStatusSerie5xx(final String methodKey, final Response response) {
        log.error("status={}, reason={}", response.status(), response.reason());
        return new HttpServerErrorException(HttpStatus.resolve(response.status()));
    }

}
