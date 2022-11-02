package com.jungle.rxjava.demo;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.junit.Test;

import javax.swing.*;

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
                })
                .map(i -> "Number:" + i)
                .subscribe(System.out::println);
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
                    });
                }).map(i -> "Number:" + i)
                .subscribe(System.out::println);
    }


}
