package com.chatroom.rsocket.presentation;

import com.chatroom.rsocket.application.ChatService;
import com.chatroom.rsocket.domain.ChatMessage;
import io.rsocket.RSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;

    @ConnectMapping
    public void connect(RSocketRequester requester) {
        RSocket rSocket = requester.rsocket();
        rSocket.onClose()
                .doFirst(() -> log.info("RSocket Connected!"))
                .doOnCancel(() -> log.info("RSocket Canceled!"))
                .doOnError(error -> log.error("RSocket Error: {}", error))
                .doFinally(consumer -> {
                    log.info("RSocket Disconnected!");
                    rSocket.dispose();
                })
                .subscribe();
    }

    @MessageMapping("/chatRoom/{topic}")
    public Flux<ChatMessage> chatMessageChannel(@DestinationVariable String topic, @Payload Flux<ChatMessage> chatMessages) {
        return chatMessages
                .doFirst(() -> log.info("ChatMessages Start!"))
                .doOnNext(chatService::save)
                .doOnCancel(() -> log.info("ChatMessages Canceled!"))
                .doOnError(error -> log.error("ChatMessages Error: {}", error))
                .doFinally(consumer -> log.info("ChatMessages End!"));
    }
}
