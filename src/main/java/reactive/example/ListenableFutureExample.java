package reactive.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ListenableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        ListenableFutureTask<Integer> f1 = new ListenableFutureTask<>(() -> taskLogging(1));
        f1.addCallback(s1 -> {
            ListenableFutureTask<Integer> f2 = new ListenableFutureTask<>(() -> taskLogging(s1));
            f2.addCallback(s2 -> {
                ListenableFutureTask<Integer> f3 = new ListenableFutureTask<>(() -> taskLogging(s2));
                f3.addCallback(s3 -> {
                    ListenableFutureTask<Integer> f4 = new ListenableFutureTask<>(() -> taskLogging(s3));
                    f4.addCallback(s4 -> {
                        try {
                            taskLogging(s4);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }, e -> errorLogging(s3, e));
                    f4.run();
                }, e -> errorLogging(s2, e));
                f3.run();
            }, e -> errorLogging(s1, e));
            f2.run();
        }, e -> errorLogging(1, e));

        es.submit(() -> f1.run());
        log.info("[main] 메소드 종료!");

        es.shutdown();
    }

    private static int taskLogging(int i) throws InterruptedException {
        Thread.sleep(1000);
        log.info("[listenableFuture-{}] 비동기 작업 진행중...", i);
        log.info("[listenableFuture-{}] 비동기 작업 완료", i);
        return ++i;
    }

    private static void errorLogging(int i, Throwable e) {
        log.error("[listenableFuture-{}] 비동기 작업 실패: {}", i, e.getMessage());
    }
}