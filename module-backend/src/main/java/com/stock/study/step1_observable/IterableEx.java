package com.stock.study.step1_observable;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
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
            log.info(String.valueOf(i));
        }

        for (Iterator<Integer> it = iter.iterator(); it.hasNext();) {
            log.info(String.valueOf(it.next()));
        }
    }
}
