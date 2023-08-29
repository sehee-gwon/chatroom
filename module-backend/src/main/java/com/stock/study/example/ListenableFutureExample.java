package com.stock.study.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ListenableFutureExample {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);

        // future 에 비동기 결과값이 저장되고, addCallback 은 비동기 작업이 완료된 시점에 호출된다.
        for (int i = 1; i <= 2; i++) {
            int idx = i;

            ListenableFutureTask<TeamPlan> futureTeamPlan = new ListenableFutureTask<>(() -> getTeamPlan(idx)); // 정보 조회
            futureTeamPlan.addCallback(tp -> {
                ListenableFutureTask<TeamPlan> futurePayment = new ListenableFutureTask<>(() -> payment(tp));   // 결제
                futurePayment.addCallback(tp2 -> {
                    ListenableFutureTask<TeamPlan> futureMail = new ListenableFutureTask<>(() -> sendMail(tp2));   // 메일 전송
                    futureMail.addCallback(tp3 -> {
                        ListenableFutureTask<TeamPlan> futureSubscribe = new ListenableFutureTask<>(() -> subscribe(tp3));  // 구독
                        futureSubscribe.addCallback(tp4 -> {
                            // 처리 완료
                        }, e -> log.error(e.getMessage()));
                        futureSubscribe.run();
                    }, e -> log.error(e.getMessage()));
                    futureMail.run();
                }, e -> log.error(e.getMessage()));
                futurePayment.run();
            }, e -> log.error(e.getMessage()));

            es.submit(() -> futureTeamPlan.run());
        }

        es.shutdown();
    }

    public static TeamPlan getTeamPlan(int idx) {
        log.info("[idx={}] 정보 조회", idx);
        return new TeamPlan(idx);
    }

    public static TeamPlan payment(TeamPlan teamPlan) {
        log.info("[idx={}] 결제", teamPlan.getIdx());
        return teamPlan;
    }

    public static TeamPlan sendMail(TeamPlan teamPlan) {
        log.info("[idx={}] 메일 전송", teamPlan.getIdx());
        return teamPlan;
    }

    public static TeamPlan subscribe(TeamPlan teamPlan) {
        log.info("[idx={}] 구독", teamPlan.getIdx());
        return teamPlan;
    }
}