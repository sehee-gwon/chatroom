package reactive.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CompletableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        CompletableFuture
                .supplyAsync(() -> taskLogging(1), es)
                .thenCompose(s1 -> CompletableFuture.completedFuture(taskLogging(s1)))
                .thenApply(s2 -> taskLogging(s2))
                .thenApplyAsync(s3 -> taskLogging(s3))
                .exceptionally(e -> {
                    log.error("[completableFuture] 비동기 작업 실패 {}", e.getMessage());
                    return null;
                })
                .thenAcceptAsync(s4 -> log.info("[completableFuture] 비동기 작업 완료"), es);

        log.info("[main] 메소드 종료!");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(3, TimeUnit.SECONDS);
    }

    private static int taskLogging(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[listenableFuture-{}] 비동기 작업 진행중...", i);
        return ++i;
    }
}