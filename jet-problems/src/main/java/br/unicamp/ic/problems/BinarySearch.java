package br.unicamp.ic.problems;

import br.unicamp.ic.inject.parameters.IntArrayParameter;

public class BinarySearch {

    @IntArrayParameter(index = 1, value = { 1, 2, 3 })
    public boolean run(final int[] v, final int key) {
        boolean found = false;
        int low = 0;
        int high = v.length - 1;
        int mid;

        while (low <= high && !found) {
            mid = (low + high) / 2;
            if (key < v[mid]) {
                high = mid - 1;
            } else {
                if (key > v[mid]) {
                    low = mid + 1;
                } else {
                    found = true;
                }
            }
        }
        return found;
    }

    // path = CoveredPath.newInstance();
    // path.addStep('a');
    // while (low <= high && !found) {
    // path.addStep('b');
    // mid = (low + high) / 2;
    // path.addStep('c');
    // if (key.compareTo(v[mid]) < 0) {
    // path.addStep('d');
    // high = mid - 1;
    // } else if (key.compareTo(v[mid]) > 0) {
    // path.addStep('e');
    // low = mid + 1;
    // path.addStep('f');
    // } else {
    // path.addStep('e');
    // found = true;
    // path.addStep('g');
    // }
    // path.addStep('h');
    // }
    // path.addStep('i');
    // return found;

}
