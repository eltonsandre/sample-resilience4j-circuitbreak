package com.github.eltonsandre.sample.circuitbreak.exception;

import java.util.function.Predicate;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author eltonsandre
 * date 04/04/2019 17:58
 */
public class RecordFailurePredicate implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {
        return !(throwable instanceof ProdutoException) && !(throwable instanceof HttpClientErrorException) ;
    }


}