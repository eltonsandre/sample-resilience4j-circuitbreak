package com.github.eltonsandre.sample.circuitbreak.client.produtos;

import com.github.eltonsandre.sample.circuitbreak.exception.ProdutoException;
import com.github.eltonsandre.sample.circuitbreak.model.Produto;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author eltonsandre
 * date 04/04/2019 22:14
 */
@Slf4j
@Component(ProdutosFallback.BEAN_NAME)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProdutosFallback {

    public static final String BEAN_NAME = "produtosFallback";

    public List<Produto> circuitBreaker(final String tipo, final java.lang.Exception throwable) {
        log.info("tipo={} throwable={} fallback=CircuitBreaker", tipo, throwable.getMessage());
        throw new ProdutoException("---------- Fallback CircuitBreaker ----------");
    }

    public List<Produto> retry(final String tipo, final java.lang.Exception throwable) {
        log.info("tipo={} throwable={} fallback=Retry", tipo, throwable.getMessage());
        throw new ProdutoException("---------- Fallback Retry ----------");
    }

    public List<Produto> rateLimiter(final String tipo, final java.lang.Exception throwable) {
        log.info("tipo={} throwable={} fallback=RateLimiter", tipo, throwable.getMessage());
        throw new ProdutoException("---------- Fallback RateLimiter ----------");
    }

}
