package com.jungle.rxjava.demo;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.operators.observable.ObservableOnErrorComplete;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.swing.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
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


        Observable.just(8, 9, 10).doOnNext(i -> System.out.println("A: " + i)).filter(i -> i % 3 > 0).doOnNext(i -> System.out.println("B: " + i)).map(i -> "#" + i * 10).doOnNext(i -> System.out.println("C: " + i)).filter(i -> i.length() < 4).subscribe(i -> System.out.println("D: " + i));
    }

    @Test
    public void testDelay() throws InterruptedException {
        Observable.just("HHE", "sddd", "dassswe", "s", "sdda").delay(word -> ObservableOnErrorComplete.timer(word.length(), TimeUnit.SECONDS)).subscribe(System.out::println);


        TimeUnit.SECONDS.sleep(15);

    }

    @Test
    public void testMerge() {
        Observable<Integer> oneToEight = Observable.range(1, 8);
        Observable<String> ranks = oneToEight.map(Objects::toString);

        Observable<String> files = oneToEight.map(x -> 'a' + x - 1).map(ascii -> (char) ascii.intValue()).map(ch -> Character.toString(ch));

        Observable<String> map = files.flatMap(file -> ranks.map(rank -> file + rank));
        map.subscribe(System.out::println);

    }


    @Test
    public void testMerge02() {
        Observable<LocalDate> nextTenDay = Observable.range(1, 10).map(i -> LocalDate.now().plusDays(i));

        Observable<Vacation> possibleVacation = Observable.just(City.London, City.Paris).flatMap(city -> nextTenDay.map(date -> new Vacation(city, date))).flatMap(vacation ->
                Observable.zip(vacation.weather().filter(Whether::isSunny), vacation.cheepFlightFrom(City.NewYork), vacation.cheepHotel(), (w, f, h) -> vacation)
        );
        possibleVacation.subscribe(System.out::println);

    }

    @Test
    public void testMerge03() {
        Observable<Long> red = Observable.interval(10, TimeUnit.MILLISECONDS);
        Observable<Long> green = Observable.interval(11, TimeUnit.MILLISECONDS);


        Observable.zip(red.timestamp(), green.timestamp(), (r, g) -> r.time() - g.time()).forEach(System.out::println);


        while (true) {

        }

    }

    @Test
    public void testScan() {
        Single<BigInteger> reduce = Observable.range(2, 100)
                .reduce(BigInteger.ONE, (l, r) -> l.multiply(BigInteger.valueOf(r)));
        reduce.subscribe(System.out::println);
    }


    public class Vacation {
        private final City where;
        private final LocalDate when;

        public Vacation(City where, LocalDate when) {
            this.where = where;
            this.when = when;
        }

        public Observable<Whether> weather() {
            boolean b = new Random().nextBoolean();
            return Observable.just(b ? Whether.Rain : Whether.Sunny);
        }

        public Observable<Flight> cheepFlightFrom(City from) {
            return Observable.just(new Flight());
        }

        public Observable<Hotel> cheepHotel() {
            return Observable.just(new Hotel());
        }

    }

    public enum City {
        London, NewYork, Paris
    }

    public enum Whether {
        Sunny(true), Rain(false);

        public final boolean isSunny;

        Whether(boolean isSunny) {
            this.isSunny = isSunny;
        }

        public boolean isSunny() {
            return isSunny;
        }
    }

    private class Flight {
    }

    private class Hotel {
    }

    public static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ":[ " + msg + " ]");
    }


}
