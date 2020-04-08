package com.github.eltonsandre.sample.circuitbreak.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author eltonsandre
 * date 03/04/2020 12:16
 */
public class ProdutoException extends RuntimeException {

    @Getter
    private HttpStatus httpStatus;

    public ProdutoException(final HttpStatus httpStatus, final String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ProdutoException(final String message) {
        super(message);
    }
}
