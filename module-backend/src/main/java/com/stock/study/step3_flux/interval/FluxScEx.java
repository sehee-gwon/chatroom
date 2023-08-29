package com.stock.study.step3_flux.interval;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx {
    public static void main(String[] args) throws InterruptedException {
        // Daemon thread 로 실행한다.
        Flux.interval(Duration.ofMillis(200)) // Operator: 데이터 변환, 조작, 스케줄링, publish 제어
                .take(10)   // N 개만 받겠다.
                .subscribe(s -> log.debug("onNext: {}", s));

        // User thread vs Daemon thread
        // User thread 는 한개라도 남아있으면 종료되지 않는다.
        // Daemon thread 만 남아있으면 강제 종료한다.

        // User thread... 이 코드가 없으면 Flux.interval 코드가 실행되지 않는다.
        TimeUnit.SECONDS.sleep(5);
    }
}
