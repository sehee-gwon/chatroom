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
        ListenableFutureTask<String> f1 = new ListenableFutureTask<>(() -> onComplete(1));

        f1.addCallback(s1 -> {
            ListenableFutureTask<String> f2 = new ListenableFutureTask<>(() -> s1 + onComplete(2));
            f2.addCallback(s2 -> {
                ListenableFutureTask<String> f3 = new ListenableFutureTask<>(() -> s2 + onComplete(3));
                f3.addCallback(s3 -> {
                    ListenableFutureTask<String> f4 = new ListenableFutureTask<>(() -> s3 + onComplete(4));
                    f4.addCallback(s4 -> {
                        try {
                            log.info("[listenableFuture-{}] 비동기 작업 완료", s4 + onComplete(5));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }, e -> onError(4, e));
                    f4.run();
                }, e -> onError(3, e));
                f3.run();
            }, e -> onError(2, e));
            f2.run();
        }, e -> onError(1, e));

        es.submit(() -> f1.run());
        log.info("[main] 메소드 종료!");

        es.shutdown();
    }

    private static String onComplete(int i) throws InterruptedException {
        Thread.sleep(1000);
        log.info("[listenableFuture-{}] 비동기 작업 진행중...", i);
        return i > 1 ? ", " + i : String.valueOf(i);
    }

    private static void onError(int i, Throwable e) {
        log.error("[listenableFuture-{}] 비동기 작업 실패: {}", i, e.getMessage());
    }
}