package reactive.step6_callback_custom;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import reactive.step6_callback_custom.completion.Completion;

@Slf4j
@RequiredArgsConstructor
@RestController("step6_myController")
public class MyController {
    private static final String URL1 = "http://localhost:8081/service?req={req}";
    private static final String URL2 = "http://localhost:8081/service2?req={req}";

    private final MyService myService;
    private AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

    // Async Non-Blocking
    // Callback Hell
    @GetMapping("/before-rest")
    public DeferredResult<String> beforeRest(int idx) {
        DeferredResult<String> dr = new DeferredResult<>();

        // f1 에 비동기 결과값이 저장되고, addCallback 은 비동기 작업이 완료된 시점에 호출된다.
        ListenableFuture<ResponseEntity<String>> f1 = rt.getForEntity(URL1, String.class, "hello" + idx);
        f1.addCallback(s -> {
            ListenableFuture<ResponseEntity<String>> f2 = rt.getForEntity(URL2, String.class, s.getBody());
            f2.addCallback(s2 -> {
                ListenableFuture<String> f3 = myService.work(s2.getBody());
                f3.addCallback(s3 -> dr.setResult(s3), e -> dr.setErrorResult(e.getMessage()));
            }, e -> dr.setErrorResult(e.getMessage()));
        }, e -> dr.setErrorResult(e.getMessage()));

        return dr;
    }

    // Refactoring Code
    @GetMapping("/rest")
    public DeferredResult<String> rest(int idx) {
        DeferredResult<String> dr = new DeferredResult<>();

        // 1. from -> f1 addCallback
        // 2. andApply -> f2 apply completion 객체 연결
        // 3. andAccept -> f3 apply completion 객체 연결
        // 4. andError -> error completion 객체 연결
        // 5. andAccept -> accept completion 객체 연결
        Completion  // 객체 체인
                .from(rt.getForEntity(URL1, String.class, "hello" + idx))
                .andApply(s -> rt.getForEntity(URL2, String.class, s.getBody()))
                .andApply(s -> myService.work(s.getBody()))
                .andError(e -> dr.setErrorResult(e.getMessage()))
                .andAccept(s ->  dr.setResult(s));

        // 비동기 작업 완료 성공시
        // 1. f1 callback 수행
        // 2. f1 callback complete 는 연결된 f2 completion 객체 run (f2 addCallback)
        // 3. f2 callback complete 는 연결된 f3 completion 객체 run (f3 accept)

        // 비동기 작업 완료 실패시
        // 1. 위에서 설정된 객체 체인대로 next 에 error 를 연쇄적으로 던짐
        // 2. Error Completion 객체를 만나면 설정된 에러 로직을 수행하고 로직 종료

        return dr;
    }
}
