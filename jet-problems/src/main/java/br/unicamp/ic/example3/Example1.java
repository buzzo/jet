package br.unicamp.ic.example3;

public class Example1 {

    /**
     * Just one more method to the class.
     */
    public void call(int x, final int y, final int z) {
        try {
            final int[] i = new int[3];
            i[x] = 1;
        } catch (final Exception e) {
            x = 5;
        }
    }

    public void method(final int x, int z) {
        switch (z) {
            case 1:
                z = 1;
                break;
            case 2:
            case 3:
                z = 2;
                break;
            default:
                z = 3;
                break;
        }
        z = 5;
    }

}
