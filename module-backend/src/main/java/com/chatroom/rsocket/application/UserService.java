package com.chatroom.rsocket.application;

import com.chatroom.rsocket.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserService {
    private AtomicLong counter = new AtomicLong();

    public User createUser() {
        return new User(counter.getAndIncrement(), RandomUserNameGenerator.getUserName());
    }
}
