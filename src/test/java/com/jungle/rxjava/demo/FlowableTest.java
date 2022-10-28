package com.jungle.rxjava.demo;

import io.reactivex.rxjava3.core.Flowable;
import org.junit.Test;

public class FlowableTest {


    @Test
    public void sayHello() {
        FlowableDemo.hello("Jungle", "Ben");
    }
}
