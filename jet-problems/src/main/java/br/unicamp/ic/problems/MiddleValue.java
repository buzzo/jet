package br.unicamp.ic.problems;

public class MiddleValue {

    public int run(final int l, final int m, final int h) {
        int resp = h;

        // 0
        if (l <= m && m <= h) {
            // 1 // 2
            resp = m;
        } else {
            // 3
            if (h <= m && m <= l) {
                // 4 // 5
                resp = m;
            } else {
                // 6
                if (m <= l && l <= h) {
                    // 7 // 8
                    resp = l;
                }
                // the else here: resp=h (not necessary as resp already starts with h)
            }
        }
        // 9
        return resp;
    }

    // path.addStep('a');
    // data.addStep('a');
    // if (middle.compareTo(higher) < 0) {
    // path.addStep('b');
    // data.addStep('b', middle.subtract(higher).abs().add(K));
    // if (lower.compareTo(middle) < 0) {
    // path.addStep('c');
    // data.addStep('c', lower.subtract(middle).abs().add(K));
    // resp = middle;
    // } else {
    // path.addStep('d');
    // data.addStep('d', lower.subtract(middle).abs().add(K));
    // if (lower.compareTo(higher) < 0) {
    // path.addStep('e');
    // data.addStep('e', lower.subtract(higher).abs().add(K));
    // resp = lower;
    // } else {
    // path.addStep('f');
    // data.addStep('f', lower.subtract(higher).abs().add(K));
    // }
    // }
    // } else {
    // path.addStep('g');
    // data.addStep('g', middle.subtract(higher).abs().add(K));
    // if (lower.compareTo(middle) > 0) {
    // path.addStep('h');
    // data.addStep('h', middle.subtract(lower).abs().add(K));
    // resp = middle;
    // } else {
    // path.addStep('i');
    // data.addStep('i', middle.subtract(lower).abs().add(K));
    // if (lower.compareTo(higher) > 0) {
    // path.addStep('j');
    // data.addStep('j', higher.subtract(lower).abs().add(K));
    // resp = lower;
    // } else {
    // path.addStep('k');
    // data.addStep('k', higher.subtract(lower).abs().add(K));
    // }
    // }
    // }
    // path.addStep('l');
    // data.addStep('l');

}
