package com.flydean;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author wayne
 * @version PublishOnUsage,  2020/9/6
 */
public class PublishOnUsage {

    @Test
    public void usePublishOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> 10 + i + ":"+ Thread.currentThread())
                .publishOn(s)
                .map(i -> "value " + i+":"+ Thread.currentThread());

        new Thread(() -> flux.subscribe(System.out::println),"ThreadA").start();
        System.out.println(Thread.currentThread());
        Thread.sleep(5000);
    }
}
