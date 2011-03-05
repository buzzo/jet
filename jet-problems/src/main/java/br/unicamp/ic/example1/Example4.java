package br.unicamp.ic.example1;

public class Example4 {

    /**
     * Just one more method to the class.
     */
    public void call(int x, int y, final int z) {
        if (x > 0) {
            x = 2;
        } else {
            x = -2;
        }
        if (y < 0) {
            y = 0;
        }
    }

    public void executor(int x, int y) {
        if (x > 0) {
            x = 2;
        } else {
            x = -2;
        }
        if (y < 0) {
            y = 0;
        }
    }

    /**
     * Just one more method to the class.
     */
    public static void fake(int x, int y) {
        if (x > 0) {
            x = 2;
        } else {
            x = -2;
        }
        if (y < 0) {
            y = 0;
        }
    }

}
