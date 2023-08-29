package reactive.study.step7_completable_future;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController("step7_myController")
public class MyController {
    private static final String URL1 = "http://localhost:8081/service?req={req}";
    private static final String URL2 = "http://localhost:8081/service2?req={req}";

    private final MyService myService;
    private AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

    // Refactoring Code
    @GetMapping("/completable-rest")
    public DeferredResult<String> completableRest(int idx) {
        DeferredResult<String> dr = new DeferredResult<>();

        toCF(rt.getForEntity(URL1, String.class, "hello" + idx))
                .thenCompose(s -> toCF(rt.getForEntity(URL2, String.class, s.getBody())))
                .thenApplyAsync(s2 -> myService.work(s2.getBody()))
                .thenAccept(s3 -> dr.setResult(s3))
                .exceptionally(e -> {
                    dr.setErrorResult(e.getMessage());
                    return null;
                });

        /* Completion
                .from(rt.getForEntity(URL1, String.class, "hello" + idx))
                .andApply(s -> rt.getForEntity(URL2, String.class, s.getBody()))
                .andApply(s -> myService.work(s.getBody()))
                .andError(e -> dr.setErrorResult(e.getMessage()))
                .andAccept(s ->  dr.setResult(s)); */

        return dr;
    }

    private <T> CompletableFuture<T> toCF(ListenableFuture<T> lf) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        lf.addCallback(s -> cf.complete(s), e -> cf.completeExceptionally(e));
        return cf;
    }
}
