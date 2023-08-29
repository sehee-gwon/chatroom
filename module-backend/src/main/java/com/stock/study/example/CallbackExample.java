package com.stock.study.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CallbackExample {
    public interface SuccessCallback {
        void onComplete(TeamPlan teamPlan);
    }

    public interface FailCallback {
        void onError(Throwable e);
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);

        for (int i=1; i<=2; i++) {
            int idx = i;

            es.execute(() -> {
                getTeamPlan(idx, tp -> {  // 정보 조회
                    payment(tp, tp2 -> {    // 결제
                        sendMail(tp2, tp3 -> {  // 메일 전송
                            subscribe(tp3, tp4 -> { // 구독
                                // 처리 완료
                            }, e -> log.error(e.getMessage()));
                        }, e -> log.error(e.getMessage()));
                    }, e -> log.error(e.getMessage()));
                }, e -> log.error(e.getMessage()));
            });
        }

        es.shutdown();
    }

    public static void getTeamPlan(int idx, SuccessCallback sc, FailCallback fc) {
        try {
            log.info("[idx={}] 정보 조회", idx);
            sc.onComplete(new TeamPlan(idx));
        } catch (Exception e) {
            fc.onError(e);
        }
    }

    public static void payment(TeamPlan teamPlan, SuccessCallback sc, FailCallback fc) {
        try {
            log.info("[idx={}] 결제", teamPlan.getIdx());
            sc.onComplete(teamPlan);
        } catch (Exception e) {
            fc.onError(e);
        }
    }

    public static void sendMail(TeamPlan teamPlan, SuccessCallback sc, FailCallback fc) {
        try {
            log.info("[idx={}] 메일 전송", teamPlan.getIdx());
            sc.onComplete(teamPlan);
        } catch (Exception e) {
            fc.onError(e);
        }
    }

    public static void subscribe(TeamPlan teamPlan, SuccessCallback sc, FailCallback fc) {
        try {
            log.info("[idx={}] 구독", teamPlan.getIdx());
            sc.onComplete(teamPlan);
        } catch (Exception e) {
            fc.onError(e);
        }
    }
}