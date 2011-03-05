package br.unicamp.ic.problems;


public class Rest {

    public int run(final int x, final int y) {
        int quotient = 0;
        int rest = x;

        while (rest >= y && y > 0) {
            rest -= y;
            quotient++;
        }
        return rest;
    }

}
