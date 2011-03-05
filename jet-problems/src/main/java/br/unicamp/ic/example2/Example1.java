package br.unicamp.ic.example2;

public class Example1 {

    public void call(int x, final int y, final int z) {
        try {
            final int[] i = new int[3];
            i[x] = 1;
        } catch (final Exception e) {
            x = 5;
        }
    }

}
