package com.chatroom.rsocket.presentation;

import com.chatroom.rsocket.application.UserService;
import com.chatroom.rsocket.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @MessageMapping("/user/create")
    public Mono<User> userResponse() {
        return Mono.just(userService.createUser());
    }
}
