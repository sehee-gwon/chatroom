package com.stock.study.step6_callback_custom.completion;

import java.util.function.Consumer;

public class AcceptCompletion<S> extends Completion<S, Void> {
    Consumer<S> con;

    public AcceptCompletion(Consumer<S> con) {
        this.con = con;
    }

    @Override
    public void run(S value) {
        this.con.accept(value);
    }
}