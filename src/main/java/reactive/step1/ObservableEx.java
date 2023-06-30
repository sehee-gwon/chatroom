package reactive.step1;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class ObservableEx {
    // Iterable <---> Observable (duality)
    // Pull           Push

    // Observable 의 문제점
    // Complete, Error 시점에 대한 처리 방법이 없다.

    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for (int i=1; i<=10; i++) {
                setChanged();           // push
                notifyObservers(i);     // pull
            }
        }
    }

    public static void main(String[] args) {
        Observer ob = (o, arg) -> System.out.println(Thread.currentThread().getName() + " " + arg);

        IntObservable io = new IntObservable();
        io.addObserver(ob);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() +  "  EXIT");
        es.shutdown();
    }
}
