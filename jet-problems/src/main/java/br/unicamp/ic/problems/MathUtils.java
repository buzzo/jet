package br.unicamp.ic.problems;

import br.unicamp.ic.inject.parameters.StringParameter;

public class MathUtils {

    /**
     * Add two long integers, checking for overflow.
     * 
     * @param a
     *            an addend
     * @param b
     *            an addend
     * @param msg
     *            the message to use for any thrown exception.
     * @return the sum <code>a+b</code>
     * @throws ArithmeticException
     *             if the result can not be represented as an long
     * @since 1.2
     */
    @StringParameter(index = 3, value = "message")
    public static int addAndCheck(int a, int b, String msg) {
        int ret;
        if (a > b) {
            // use symmetry to reduce boundary cases
            ret = addAndCheckInverse(b, a, msg);
        } else {
            // assert a <= b

            if (a < 0) {
                if (b < 0) {
                    // check for negative overflow
                    if (Integer.MIN_VALUE - b <= a) {
                        ret = a + b;
                    } else {
                        // throw new ArithmeticException(msg);
                        // modified since we not support an exception yet.
                        return 0;
                    }
                } else {
                    // opposite sign addition is always safe
                    ret = a + b;
                }
            } else {
                // assert a >= 0
                // assert b >= 0

                // check for positive overflow
                if (a <= Integer.MAX_VALUE - b) {
                    ret = a + b;
                } else {
                    // throw new ArithmeticException(msg);
                    // modified since we not support an exception yet.
                    return 0;
                }
            }
        }
        return ret;
    }

    /**
     * Same method as addAndCheck but we created just to avoid a own call.
     */
    public static int addAndCheckInverse(int a, int b, String msg) {
        int ret;

        if (a < 0) {
            if (b < 0) {
                if (Integer.MIN_VALUE - b <= a) {
                    ret = a + b;
                } else {
                    return 0;
                }
            } else {
                ret = a + b;
            }
        } else {
            if (a <= Integer.MAX_VALUE - b) {
                ret = a + b;
            } else {
                return 0;
            }
        }

        return ret;
    }

}
