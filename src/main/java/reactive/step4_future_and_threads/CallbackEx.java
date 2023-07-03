package reactive.step4_future_and_threads;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class CallbackEx {
    interface SuccessCallback<T> {
        void onSuccess(T result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask<T> extends FutureTask<T> {
        SuccessCallback<T> sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<T> callable, SuccessCallback<T> sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask<String> cf = new CallbackFutureTask<>(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        }, s-> log.info("Result: {}", s), e -> log.error("Error: {}", e.getMessage()));

        es.execute(cf);

        log.info("Exit");

        es.shutdown();
    }
}
