package com.chatroom.study.step6_callback_custom.completion;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.function.Function;

public class ApplyCompletion<S, T> extends Completion<S, T> {
    Function<S, ListenableFuture<T>> fn;

    public ApplyCompletion(Function<S, ListenableFuture<T>> fn) {
        this.fn = fn;
    }

    @Override
    public void run(S value) {
        ListenableFuture<T> lf = this.fn.apply(value);
        lf.addCallback(s -> complete(s), e -> error(e));
    }
}
