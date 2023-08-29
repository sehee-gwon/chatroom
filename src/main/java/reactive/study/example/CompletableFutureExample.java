package reactive.study.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CompletableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> allFuture = new CompletableFuture<>();
        ExecutorService es = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 2; i++) {
            int idx = i;

            allFuture =
                    CompletableFuture.supplyAsync(() -> getTeamPlan(idx), es)   // 정보 조회
                            .thenApply(tp -> payment(tp))              // 결제
                            .thenApply(tp -> sendMail(tp))             // 메일 전송
                            .thenAccept(tp -> subscribe(tp))           // 구독
                            .exceptionally(e -> {
                                log.error(e.getMessage());
                                throw new RuntimeException(e);
                            });
        }

        // blocking
        // get() 과의 차이는 Checked Exception vs Unchecked Exception
        allFuture.join();
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