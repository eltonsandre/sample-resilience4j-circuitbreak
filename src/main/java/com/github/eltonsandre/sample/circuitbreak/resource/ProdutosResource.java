package com.github.eltonsandre.sample.circuitbreak.resource;

import com.github.eltonsandre.sample.circuitbreak.client.produtos.ProdutosApiClient;
import com.github.eltonsandre.sample.circuitbreak.model.Produto;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe responsavel por expor recurso de produtos
 * @author eltonsandre
 * date 29/03/2020 15:37
 */
@RestController
@RequestMapping("/produtos")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)
public class ProdutosResource {

    ProdutosApiClient produtosApiClient;

    @GetMapping
    public ResponseEntity<List<Produto>> produtos(@RequestParam(value = "tipo", required = false, defaultValue = "ok")final String tipo){
        return ResponseEntity.ok(this.produtosApiClient.produtos(tipo));
    }

}
