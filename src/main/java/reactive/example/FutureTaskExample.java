package reactive.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class FutureTaskExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<>(() -> {
            Thread.sleep(5000);
            log.info("[futureTask] 비동기 작업 진행중...");
            return "[futureTask] 비동기 작업 완료";
        }) {
            @Override
            protected void done() {
                try {
                    log.info(get());
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.getMessage(), e);
                }
            }
        };

        es.execute(f);  // 여기서 실행

        log.info("[main] 메소드 종료!");

        es.shutdown();
    }
}