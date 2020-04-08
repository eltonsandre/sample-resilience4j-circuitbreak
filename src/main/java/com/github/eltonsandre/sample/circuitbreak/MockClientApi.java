package com.github.eltonsandre.sample.circuitbreak;

import com.github.eltonsandre.sample.circuitbreak.model.Produto;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eltonsandre
 * date 04/03/2020 23:20
 */
@Slf4j
@Profile("internal-api-client")
@RestController
@RequestMapping("/internal-api/produtos")
@RequiredArgsConstructor
public class MockClientApi {

    private static final List<Produto> PRODUTOS = List.of(
            Produto.builder().id("PRDT-001").descricao("Produto ABC 001").build(),
            Produto.builder().id("PRDT-002").descricao("Produto ASD 002").build(),
            Produto.builder().id("PRDT-003").descricao("Produto QWE 003").build());

    @SneakyThrows
    @GetMapping
    public ResponseEntity<List<Produto>> produtos(@RequestParam("tipo") final String tipo) {
        switch (tipo) {
        case "ok":
            log.info("SUCCESS OK ");
            break;
        case "wait20":
            log.info("SUCCESS WAIT 20 ");
            Thread.sleep(Duration.ofSeconds(20).toMillis());
            break;
        case "wait8":
            log.info("SUCCESS WAIT 8 ");
            Thread.sleep(Duration.ofSeconds(8).toMillis());
            break;
        case "wait5":
            log.info("SUCCESS WAIT 5 ");
            Thread.sleep(Duration.ofSeconds(5).toMillis());
            break;
        case "400":
            log.error("erro 400 ");
            return ResponseEntity.badRequest().build();
        case "404":
            log.error("erro 404 ");
            return ResponseEntity.notFound().build();
        case "500":
            log.error("erro 500 ");
            return ResponseEntity.status(500).build();
        case "503":
            log.error("erro 503 ");
            return ResponseEntity.status(503).build();
        case "timeout":
            log.error("time out ");
            Thread.sleep(Duration.ofSeconds(100).toMillis());
            throw new TimeoutException("timeout");
        default:
            log.error("erro fodasse ");
            throw new RuntimeException("fodasse");
        }
        return ResponseEntity.ok(PRODUTOS);
    }

}


