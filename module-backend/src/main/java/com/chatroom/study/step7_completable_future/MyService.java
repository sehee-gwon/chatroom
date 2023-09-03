package com.chatroom.study.step7_completable_future;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public String work(String req) {
        return req + "/asyncwork";
    }
}
