package reactive.study.step7_completable_future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Future 에 대한 결과를 명시적으로 선언할 수 있다.
        // CompletableFuture<Integer> f = new CompletableFuture<>();
        // f.complete(1), f.completeExceptionally(new RuntimeException())
        // f.get() 는 여전히 Blocking 방식이다.

        // Future, CompletionStage 를 상속받는다.
        // 6강에서 구현했던 Complete 처럼 객체 체인 가능
        ExecutorService es = Executors.newFixedThreadPool(10);

        CompletableFuture
                .supplyAsync(() -> {
                    log.info("runAsync");
                    return 1;
                }, es)
                .thenCompose(s -> {
                    log.info("thenApply {}", s);
                    return CompletableFuture.completedFuture(s + 1);
                })
                .thenApplyAsync(s2 -> {
                    log.info("thenApply {}", s2);
                    return s2 * 3;
                }, es)
                .exceptionally(e -> -10)
                .thenAcceptAsync(s3 -> log.info("thenAccept {}", s3), es);

        log.info("exit");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
