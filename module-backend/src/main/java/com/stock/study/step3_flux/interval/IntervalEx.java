package com.stock.study.step3_flux.interval;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IntervalEx {
    public static void main(String[] args) {
        Publisher<Integer> pub = sub ->
                sub.onSubscribe(new Subscription() {
                    int no = 0;
                    boolean cancelled = false;

                    @Override
                    public void request(long n) {
                        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                        exec.scheduleAtFixedRate(() -> {
                            if (cancelled) {
                                exec.shutdown();
                                return;
                            }
                            sub.onNext(no++);
                        }, 0, 300, TimeUnit.MILLISECONDS);
                    }

                    @Override
                    public void cancel() {
                        cancelled = true;
                    }
                });

        Publisher<Integer> takePub = sub ->
                pub.subscribe(new Subscriber<>() {
                    int count = 0;
                    Subscription subsc;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subsc = s;
                        sub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Integer i) {
                        sub.onNext(i);
                        if (++count >= 10) subsc.cancel();
                    }

                    @Override
                    public void onError(Throwable t) {
                        sub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        sub.onComplete();
                    }
                });

        takePub.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription sub) {
                log.debug("onSubscribe");
                sub.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer i) {
                log.debug("onNext: {}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError: {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });
    }
}
