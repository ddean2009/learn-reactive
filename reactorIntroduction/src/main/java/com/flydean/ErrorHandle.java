package com.flydean;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wayne
 * @version ErrorHandle,  2020/9/6
 */
public class ErrorHandle {

    @Test
    public void useErrorhandle(){
        Flux flux= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorReturn("Divided by zero :(");
        flux.subscribe(System.out::println);

        Flux flux2= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i));
//        flux2.subscribe(System.out::println);

        flux2.subscribe(System.out::println,
                error -> System.err.println("Error: " + error));

        Flux flux3= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorReturn("Divided by zero :(")
                .map(i -> i + ":continue");
        flux3.subscribe(System.out::println);
    }

    @Test
    public void normalErrorHandle(){
        try{
            Arrays.asList(1,2,0).stream().map(i -> "100 / " + i + " = " + (100 / i)).forEach(System.out::println);
        }catch (Exception e){
            System.err.println("Error: " + e);
        }
    }

    @Test
    public void useFallbackMethod(){
        Flux flux= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorResume(e -> System.out::println);
        flux.subscribe(System.out::println);
    }

    @Test
    public void useDynamicFallback(){
        Flux flux= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorResume(error -> Mono.just(
                        MyWrapper.fromError(error)));
    }

    public static class MyWrapper{
        public static String fromError(Throwable error){
            return "That is a new Error";
        }
    }

    @Test
    public void useRethrow(){
        Flux flux= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorResume(error -> Flux.error(
                        new RuntimeException("oops, ArithmeticException!", error)));

        Flux flux2= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorMap(error -> new RuntimeException("oops, ArithmeticException!", error));
    }

    @Test
    public void useDoOnError(){
        Flux flux= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .doOnError(error -> System.out.println("we got the error: "+ error));
    }

    @Test
    public void useUsing(){
        AtomicBoolean isDisposed = new AtomicBoolean();
        Disposable disposableInstance = new Disposable() {
            @Override
            public void dispose() {
                isDisposed.set(true);
            }

            @Override
            public String toString() {
                return "DISPOSABLE";
            }
        };

        Flux<String> flux =
                Flux.using(
                        () -> disposableInstance,
                        disposable -> Flux.just(disposable.toString()),
                        Disposable::dispose);
    }

    @Test
    public void testRetry(){
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .retry(1)
                .elapsed()
                .subscribe(System.out::println, System.err::println);

        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
