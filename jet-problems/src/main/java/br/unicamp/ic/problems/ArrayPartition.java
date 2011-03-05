package br.unicamp.ic.problems;

import br.unicamp.ic.inject.parameters.IntArrayParameter;

public class ArrayPartition {

    @IntArrayParameter(index = 1, value = { 25, 65, 39, 93, 71, 96, 59, 32, 68, 87, 51, 2, 88 })
    public static int run(int a[], int left, int right) {
        if (left > right) {
            return right;
        } else {
            int pivot = a[left];
            while (true) {
                while (a[left] < pivot)
                    left++;
                while (a[right] > pivot)
                    right--;
                if (left < right) {
                    swap(a, left, right);
                } else {
                    return right;
                }
            }
        }
    }

    private static void swap(int[] v, int lo, int hi) {
        int tmp;
        if (lo < hi) {
            tmp = v[hi];
            v[hi] = v[lo];
            v[lo] = tmp;
        }
    }
}
