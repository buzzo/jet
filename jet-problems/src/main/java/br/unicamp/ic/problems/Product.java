package br.unicamp.ic.problems;


public class Product {

    public int run(final int x, final int y) {
        int mult;

        if (x == 0 || y == 0) {
            mult = 0;
        } else {
            mult = x;
            while (mult < y) {
                mult += x;
            }
        }
        return mult;
    }

}
