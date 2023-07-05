package reactive.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CallbackExample {
    public interface SuccessCallback {
        void onComplete(String result);
    }

    public interface ExceptionCallback {
        void onError(Throwable e);
    }

    public static class AsyncOperation {
        SuccessCallback sc;
        ExceptionCallback ec;

        public AsyncOperation(SuccessCallback sc, ExceptionCallback ec) {
            this.sc = sc;
            this.ec = ec;
        }

        public void performAsyncTask() {
            ExecutorService es = Executors.newCachedThreadPool();

            es.execute(() -> {
                try {
                    Thread.sleep(5000); // 일부러 5초간 대기하도록 설정하여 비동기 작업 시뮬레이션
                    if (1 == 1) throw new RuntimeException("[performAsyncTask] 비동기 작업 실패");
                    log.info("[performAsyncTask] 비동기 작업 진행중...");
                    this.sc.onComplete("[performAsyncTask] 비동기 작업 완료");
                } catch (Exception e) {
                    this.ec.onError(e);
                }
            });

            es.shutdown();
        }
    }

    public static void main(String[] args) {
        AsyncOperation asyncOperation = new AsyncOperation(s -> log.info(s), e -> log.error(e.getMessage()));
        asyncOperation.performAsyncTask();
        log.info("[main] 메소드 종료!");
    }
}