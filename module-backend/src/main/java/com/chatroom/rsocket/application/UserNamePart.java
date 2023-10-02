package com.chatroom.rsocket.application;

import java.util.concurrent.ThreadLocalRandom;

public enum UserNamePart {
    PREFIX("멋진", "빠른", "용감한", "착한", "창의적인", "똑똑한", "활발한", "고요한", "센스있는", "대담한",
            "유쾌한", "자유로운", "행복한", "순수한", "열정적인", "기쁜", "귀여운", "섹시한", "화려한", "민첩한"),
    SUFFIX("미어캣", "사자", "호랑이", "치타", "코끼리", "독수리", "여우", "펭귄", "상어", "생쥐",
            "돌고래", "토끼", "곰", "고양이", "강아지", "물고기", "악어", "기린", "매", "하이에나");

    private final String[] values;

    UserNamePart(String... values) {
        this.values = values;
    }

    public String getRandomValue() {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
