package reactive.step6_callback_custom.completion;

import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

public class ErrorCompletion<T> extends Completion<T, T> {
    Consumer<Throwable> econ;

    public ErrorCompletion(Consumer<Throwable> econ) {
        this.econ = econ;
    }

    @Override
    public void run(T value) {
        if (this.next != null) this.next.run(value);
    }

    @Override
    public void error(Throwable e) {
        this.econ.accept(e);
    }
}
