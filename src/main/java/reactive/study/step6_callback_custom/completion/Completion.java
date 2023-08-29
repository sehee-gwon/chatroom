package reactive.study.step6_callback_custom.completion;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.function.Consumer;
import java.util.function.Function;

public class Completion<S, T> {
    Completion next;

    public <V> Completion<T, V> andApply(Function<T, ListenableFuture<V>> fn) {
        this.next = new ApplyCompletion<>(fn);
        return this.next;
    }

    public Completion<T, T> andError(Consumer<Throwable> econ) {
        this.next = new ErrorCompletion<>(econ);
        return this.next;
    }

    public void andAccept(Consumer<T> con) {
        this.next = new AcceptCompletion<>(con);
    }

    public void complete(T value) {
        if (this.next != null) this.next.run(value);
    }

    public void error(Throwable e) {
        if (this.next != null) this.next.error(e);
    }

    public void run(S value) {}

    public static <S, T> Completion<S, T> from(ListenableFuture<T> lf) {
        Completion<S, T> c = new Completion();
        lf.addCallback(c::complete, c::error);
        return c;
    }
}