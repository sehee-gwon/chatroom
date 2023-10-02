package com.chatroom.rsocket.application;

import com.chatroom.rsocket.domain.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChatService {
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void save(ChatMessage chatMessage) {
        log.info("save chat message: {}", chatMessage);
        chatMessages.add(chatMessage);
    }
}
