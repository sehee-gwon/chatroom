package reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class ReactiveApplication {
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

    /*@Bean
    ApplicationRunner run() {
        return args -> {
            log.info("run()");

            // Future
            Future<String> f = myService.hello();
            log.info("exit: {}", f.isDone());
            log.info("result: {}", f.get());

            // ListenableFuturee
            ListenableFuture<String> f = myService.listenableFutureHello();
            f.addCallback(s -> log.info(s), e -> log.error(e.getMessage()));
            log.info("exit");
        };
    }*/

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }
}