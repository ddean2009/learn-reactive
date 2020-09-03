package com.flydean;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * @author wayne
 * @version FluxUsage,  2020/9/3
 */
public class FluxUsage {

    @Test
    public void useFlux(){
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);
        Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3);

        numbersFromFiveToSeven.subscribe();
        numbersFromFiveToSeven.subscribe(System.out::println);

        Flux<Integer> ints = Flux.range(1, 4)
                .map(i -> {
                    if (i <= 3) return i;
                    throw new RuntimeException("Got to 4");
                });
        ints.subscribe(System.out::println,
                error -> System.err.println("Error: " + error));

        Flux<Integer> ints2 = Flux.range(1, 4);
        ints2.subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));

        Flux<Integer> ints3 = Flux.range(1, 4);
        ints3.subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(2));
    }
}
