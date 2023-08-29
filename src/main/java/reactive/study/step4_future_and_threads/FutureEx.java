package reactive.study.step4_future_and_threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureEx {
    // 비동기 작업의 결과를 가져오는 방법
    // 1. Java 의 가장 기본은 Future (자바 8 이전), get() 메소드는 Blocking 방식이다.
    // 2. Callback 을 이용하는 방식
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        // 비동기식 작업을 실행시키는 코드, 일종의 핸들러 같은 것이다. (= Javascript Promise)
        // Future 는 비동기식 작업의 결과를 저장하는 자료형이다.
        Future<String> f = es.submit(() -> {  // Callable, 결과 리턴 및 예외 처리
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });

        // 작업의 결과가 완료될 때까지 기다리는지? => Blocking, Non-Blocking
        log.info(f.get());  // Blocking
        log.info("Exit");

        es.shutdown();
    }
}
