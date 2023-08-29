package com.stock.study.step4_future_and_threads;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@RestController("step4_myController")
public class MyController {
    Queue<DeferredResult<String>> results = new ConcurrentLinkedQueue<>();

    @GetMapping("/dr")
    public DeferredResult<String> dr() {
        log.info("dr");
        DeferredResult<String> dr = new DeferredResult<>(600000L);
        results.add(dr);
        return dr;
    }

    @GetMapping("/dr/count")
    public String drCount() {
        return String.valueOf(results.size());
    }

    @GetMapping("/dr/event")
    public String drEvent(String msg) {
        for (DeferredResult<String> dr : results) {
            dr.setResult("Hello " + msg);
            results.remove(dr);
        }
        return "OK";
    }

    /**
     * 비동기 Web Controller
     * 비동기로 코드를 변경 시,
     * tomcat max-thread 개수를 1개로 줄여도 REST API 수행 속도가 비슷하게 나온다. (서블릿 스레드 수)
     * @return
     */
    @GetMapping("/callable-async")
    public Callable<String> callableAsync() {
        log.info("callable");
        return () -> {
            log.info("async");
            Thread.sleep(2000);
            return "hello";
        };
    }

    /**
     * 동기 Web Controller
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/callable-sync")
    public String callableSync() throws InterruptedException {
        log.info("async");
        Thread.sleep(2000);
        return "Hello";
    }
}
