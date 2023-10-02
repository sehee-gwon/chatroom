package com.chatroom.rsocket.application;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomUserNameGenerator {
    public static String getUserName() {
        String prefix = UserNamePart.PREFIX.getRandomValue();
        String suffix = UserNamePart.SUFFIX.getRandomValue();
        return String.format("%s %s", prefix, suffix);
    }
}
