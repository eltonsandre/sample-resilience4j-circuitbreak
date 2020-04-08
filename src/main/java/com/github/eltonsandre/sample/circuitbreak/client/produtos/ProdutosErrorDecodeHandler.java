package com.github.eltonsandre.sample.circuitbreak.client.produtos;

import com.github.eltonsandre.sample.circuitbreak.client.handler.ErrorDecodeHandler;
import com.github.eltonsandre.sample.circuitbreak.exception.ProdutoException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author eltonsandre
 * date 29/03/2020 19:53
 */
@Slf4j
public class ProdutosErrorDecodeHandler extends ErrorDecodeHandler {

    @Override protected Exception handleHttpStatusSerie4xx(final String methodKey, final Response response) {
        log.debug("methodKey={}", methodKey);
        return new ProdutoException(HttpStatus.resolve(response.status()), "Business Message");
    }
}
