package com.chatroom.study.step4_future_and_threads;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@Slf4j
@Service("step4_myService")
public class MyService {
    // 비동기 작업은 결과를 바로 가져올 수 없다.
    // Future 를 이용해 비동기 작업의 결과를 저장한다.

    @Async
    public Future<String> futureHello() throws InterruptedException {
        log.debug("FutureService hello()");
        Thread.sleep(1000);
        return new AsyncResult<>("Hello");
    }

    @Async
    public ListenableFuture<String> listenableFutureHello() throws InterruptedException {
        log.debug("FutureService hello()");
        Thread.sleep(1000);
        return new AsyncResult<>("Hello");
    }
}
