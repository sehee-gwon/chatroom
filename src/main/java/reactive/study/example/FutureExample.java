package reactive.study.example;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Slf4j
public class FutureExample {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            int idx = i;
            Future<?> future = es.submit(() -> {
                try {
                    Future<TeamPlan> futureTeamPlan = es.submit(() -> getTeamPlan(idx)); // 정보 조회
                    TeamPlan tp = futureTeamPlan.get();

                    Future<TeamPlan> futurePayment = es.submit(() -> payment(tp)); // 결제
                    TeamPlan tp2 = futurePayment.get();

                    Future<TeamPlan> futureMail = es.submit(() -> sendMail(tp2));   // 메일 전송
                    TeamPlan tp3 = futureMail.get();

                    Future<TeamPlan> futureSubscribe = es.submit(() -> subscribe(tp3)); // 구독
                    return futureSubscribe.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            futures.add(future);
        }

        boolean allDone;
        do {
            allDone = true;
            for (Future<?> future : futures) {
                allDone &= future.isDone();
            }
        } while (!allDone);

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