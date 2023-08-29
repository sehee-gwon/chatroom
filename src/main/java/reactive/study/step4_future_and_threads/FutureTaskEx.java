package reactive.study.step4_future_and_threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureTaskEx {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        // 비동기식 작업의 결과를 저장하는 자료형
        // Future: 작업을 제출하는 시점에 실행한다. 보다 간단하게 사용이 가능하다.
        // FutureTask: 작업을 명시적으로 선언해놓고 ExecutorService 에게 제출하여 실행한다. 세밀한 제어가 가능하다.
        FutureTask<String> f = new FutureTask<>(() -> {
           Thread.sleep(2000);
           log.info("Async");
           return "Hello";
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

        log.info("Exit");

        es.shutdown();
    }
}
