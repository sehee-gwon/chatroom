package reactive.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> f = es.submit(() -> {
            Thread.sleep(5000); // 일부러 5초간 대기하도록 설정하여 비동기 작업 시뮬레이션
            log.info("[future] 비동기 작업 진행중...");
            return "[future] 비동기 작업 완료";
        });

        log.info(f.get());
        log.info("[future] 비동기 작업 완료 여부: {}", f.isDone());
        log.info("[main] 메소드 종료!");

        es.shutdown();
    }
}