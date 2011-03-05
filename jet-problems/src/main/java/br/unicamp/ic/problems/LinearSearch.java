package br.unicamp.ic.problems;

import br.unicamp.ic.inject.parameters.IntArrayParameter;

public class LinearSearch {

    @IntArrayParameter(index = 1, value = { 1, 2 })
    public boolean run(final int[] v, final int key) {
        boolean found = false;
        int i = 0;
        while (!found && i < v.length) {
            if (v[i] == key) {
                found = true;
            }
            i++;
        }
        return found;
    }

}
