package com.jungle.rxjava.demo;

import io.reactivex.rxjava3.core.Flowable;

public class FlowableDemo {

    public static void hello(String... args) {
        Flowable.fromArray(args)
                .subscribe(s -> System.out.println("Hello " + s + " !"));
    }

}
