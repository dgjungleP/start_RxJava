package com.jungle.rxjava.demo;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.operators.observable.ObservableOnErrorComplete;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class SimpleDemo {

    @Test
    public void test01() {
        Disposable disposable = Observable.create(s -> {
            s.onNext("Hello");
            s.onComplete();
        }).subscribe(System.out::println);
    }

    @Test
    public void test02() {
        Disposable disposable = Observable.create(s -> {

            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        }).map(i -> "Number:" + i).subscribe(System.out::println);
    }

    @Test
    public void test03() {
        Disposable subscribe = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(1);
                s.onNext(2);
                s.onNext(3);
                s.onNext(4);
                s.onComplete();
            }).start();
        }).map(i -> "Number:" + i).subscribe(System.out::println);
    }

    @Test
    public void practiceCreate() {
//        log("Before");
//        Observable.range(5, 6).subscribe(SimpleDemo::log);
//        log("After");
        Observable<Object> observable = Observable.create(s -> {
            log("Create");
            s.onNext(5);
            s.onNext(6);
            s.onNext(7);
            s.onNext(8);
            s.onComplete();
            log("Completed");
        });

        Observable<Object> refCount = observable.publish().refCount();

        log("Before");
        refCount.subscribe();
        refCount.subscribe();
        log("After");


        Observable.just(8, 9, 10)
                .doOnNext(i -> System.out.println("A: " + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B: " + i))
                .map(i -> "#" + i * 10)
                .doOnNext(i -> System.out.println("C: " + i))
                .filter(i -> i.length() < 4)
                .subscribe(i -> System.out.println("D: " + i));
    }

    @Test
    public void testDelay() throws InterruptedException {
        Observable.just("HHE", "sddd", "dassswe", "s", "sdda")
                .delay(word -> ObservableOnErrorComplete.timer(word.length(), TimeUnit.SECONDS))
                .subscribe(System.out::println);


        TimeUnit.SECONDS.sleep(15);

    }


    public static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ":[ " + msg + " ]");
    }


}
