package com.flydean;

import reactor.core.publisher.Mono;

/**
 * @author wayne
 * @version MonoUsage,  2020/9/3
 */
public class MonoUsage {

    public void useMono(){
        Mono<String> noData = Mono.empty();
        Mono<String> data = Mono.just("foo");
    }
}
