package com.flydean;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author wayne
 * @version SubscribeOnUsage,  2020/9/6
 */
public class SubscribeOnUsage {

    @Test
    public void useSubscribeOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> 10 + i + ":" + Thread.currentThread())
                .subscribeOn(s)
                .map(i -> "value " + i + ":"+ Thread.currentThread());

        new Thread(() -> flux.subscribe(System.out::println), "ThreadA").start();
        Thread.sleep(5000);
    }
}
