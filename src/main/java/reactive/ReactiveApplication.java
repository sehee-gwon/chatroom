package reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

@Slf4j
//@EnableAsync
@RequiredArgsConstructor
@SpringBootApplication
public class ReactiveApplication {

    @RestController
    public static class Controller {
        Queue<DeferredResult<String>> results = new ConcurrentLinkedQueue<>();

        @GetMapping("emitter")
        public ResponseBodyEmitter emitter() {
            ResponseBodyEmitter emitter = new ResponseBodyEmitter();

            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    for (int i=1; i<=50; i++) {
                        emitter.send("<p>Stream " + i + "</p>");
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });

            return emitter;
        }

        @GetMapping("/dr")
        public DeferredResult<String> dr() {
            log.info("dr");
            DeferredResult<String> dr = new DeferredResult<>(600000L);
            results.add(dr);
            return dr;
        }

        @GetMapping("/dr/count")
        public String drCount() {
            return String.valueOf(results.size());
        }

        @GetMapping("/dr/event")
        public String drEvent(String msg) {
            for (DeferredResult<String> dr : results) {
                dr.setResult("Hello " + msg);
                results.remove(dr);
            }
            return "OK";
        }

        // 비동기로 코드를 변경 시
        // tomcat max-thread 개수를 1개로 줄여도 REST API 수행 속도가 비슷하게 나온다. (서블릿 스레드 수)
        @GetMapping("/callable")
        public Callable<String> callable() {  // 비동기 Web Controller
            log.info("callable");
            return () -> {
                log.info("async");
                Thread.sleep(2000);
                return "hello";
            };
        }
        /*public String callable() throws InterruptedException {    // 동기 Web Controller
            log.info("async");
            Thread.sleep(2000);
            return "Hello";
        }*/
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

    /*private final MyService myService;

    @Service
    public static class MyService {
        // 비동기 작업은 결과를 바로 가져올 수 없다.
        *//*@Async
        public Future<String> hello() throws InterruptedException {
            log.debug("hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }*//*
        @Async
        public ListenableFuture<String> hello() throws InterruptedException {
            log.debug("hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }
    }

    @Bean
    ThreadPoolTaskExecutor tp() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        // CorePoolSize -> Queue 까지 꽉 차면 MaxPoolSize 만큼 스레드를 늘린다.
        te.setCorePoolSize(10);
        te.setMaxPoolSize(100);
        te.setQueueCapacity(200);
        te.setThreadNamePrefix("mythread-");
        te.initialize();
        return te;
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext c = SpringApplication.run(ReactiveApplication.class, args)) {

        }
    }

    @Bean
    ApplicationRunner run() {
        return args -> {
            log.info("run()");

            // Future
            // Future<String> f = myService.hello();
            // log.info("exit: {}", f.isDone());
            // log.info("result: {}", f.get());

            // ListenableFuture
            ListenableFuture<String> f = myService.hello();
            f.addCallback(s -> log.info(s), e -> log.error(e.getMessage()));
            log.info("exit");
        };
    }*/
}
