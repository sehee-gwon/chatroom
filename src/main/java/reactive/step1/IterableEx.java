package reactive.step1;

import java.util.Iterator;

public class IterableEx {
    public static void main(String[] args) {
        Iterable<Integer> iter = () -> new Iterator<>() {
            int i = 0;
            final static int MAX = 10;

            public boolean hasNext() {
                return i < MAX;
            }

            public Integer next() {
                return ++i;
            }
        };

        for (Integer i : iter) {    // for-each
            System.out.println(i);
        }

        for (Iterator<Integer> it = iter.iterator(); it.hasNext();) {
            System.out.println(it.next());
        }
    }
}
