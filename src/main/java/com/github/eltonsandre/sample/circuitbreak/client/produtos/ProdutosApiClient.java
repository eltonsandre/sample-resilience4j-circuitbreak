package com.github.eltonsandre.sample.circuitbreak.client.produtos;

import com.github.eltonsandre.sample.circuitbreak.CircuitbreakApplication;
import com.github.eltonsandre.sample.circuitbreak.model.Produto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author eltonsandre
 * date 29/03/2020 17:14
 */
@FeignClient(name = ProdutosApiClient.CLIENT_NAME, url = "${client.api.url-base}", configuration = { ProdutosErrorDecodeHandler.class })
public interface ProdutosApiClient {

    String CLIENT_NAME = "produtoApiClient";

    @GetMapping("${client.api.path-produtos}")
    @Retry(name = CLIENT_NAME)
    @RateLimiter(name = CLIENT_NAME)
    @CircuitBreaker(name = CLIENT_NAME, fallbackMethod = "fallbackCircuitBreaker")
    List<Produto> produtos(@RequestParam("tipo") final String tipo);

    /**
     * Event NOT_PERMITTED
     * @see CallNotPermittedException
     */
    default List<Produto> fallbackCircuitBreaker(final String tipo, final CallNotPermittedException callNotPermittedException) {
        return this.produtosFallback().circuitBreaker(tipo, callNotPermittedException);
    }

    /**
     * @return bean fallback ProdutosFallback
     */
    default ProdutosFallback produtosFallback() {
        return CircuitbreakApplication.context.getBean(ProdutosFallback.class);
    }
}
