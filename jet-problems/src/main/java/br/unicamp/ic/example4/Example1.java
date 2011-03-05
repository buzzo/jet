package br.unicamp.ic.example4;

import java.util.ArrayList;
import java.util.List;

public class Example1 {

    public int test(final int x) {
        int y = x;
        List<Integer> l;
        l = new ArrayList<Integer>();
        l.add(3);
        for (final Integer i : l) {
            y += i;
        }
        return y;
    }

    public static void listTest(final int x) {
        List<Integer> l;
        l = new ArrayList<Integer>();
        l.add(1);
        l.add(2);
        for (Integer i : l) {
            i++;
        }
    }

}
