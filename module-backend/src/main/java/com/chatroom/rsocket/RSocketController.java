package com.chatroom.rsocket;

import io.rsocket.RSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RSocketController {

    @ConnectMapping
    public Mono<Void> setUp(RSocketRequester requester) {
        RSocket rSocket = requester.rsocket();
        rSocket.onClose()
                .doFirst(() -> log.info("RSocket Connected!"))
                .doOnError(error -> log.error("RSocket Error: {}", error))
                .doOnCancel(() -> log.info("RSocket Canceled!"))
                .doFinally(consumer -> {
                    log.info("RSocket Disconnected!");
                    rSocket.dispose();
                })
                .subscribe();
        return Mono.empty();
    }
}
