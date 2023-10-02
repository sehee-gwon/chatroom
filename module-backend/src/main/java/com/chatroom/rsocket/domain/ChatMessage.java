package com.chatroom.rsocket.domain;

public record ChatMessage(User user, String message) {
}