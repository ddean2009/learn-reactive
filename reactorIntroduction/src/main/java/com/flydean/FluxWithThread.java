package com.flydean;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wayne
 * @version FluxWithThread,  2020/9/5
 */
public class FluxWithThread {

    @Test
    public void withThread() throws InterruptedException {
        Mono<String> mono = Mono.just("hello ");

        Thread t = new Thread(() -> mono
                .map(msg -> msg + "thread ")
                .subscribe(v ->
                        System.out.println(v + Thread.currentThread().getName())
                )
        );
        t.start();
        t.join();
    }
}
