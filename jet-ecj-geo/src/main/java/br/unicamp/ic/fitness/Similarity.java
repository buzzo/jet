package br.unicamp.ic.fitness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public final class Similarity {

    /**
     * Private constructor. Please use {{@link #newInstance()} for a new instance.
     */
    private Similarity() {
        // private.
    }

    /**
     * Calculates the similarity between two paths. Bigger the number better the similarity. Smaller the number worst
     * the similarity.<br>
     * This fitness function ignores loop paths by calling the method {{@link #removeLoops(CoveredPath)}. For details
     * check its javadoc.
     * 
     * @param t
     *            target path.
     * @param c
     *            current path.
     * @return a value indicating the similarity between the paths.
     */
    public final double evaluate(final CoveredPath t, final CoveredPath c) {
        // remove repetitive patterns from the paths
        final CoveredPath targetPath = removeLoops(t);
        final CoveredPath currentPath = removeLoops(c);

        // gets target order
        final int targetOrder = targetPath.getPath().length();
        // initializes the SIM (result) of the similarity calculation
        BigDecimal sim = new BigDecimal(0);
        // loops until the similarity goes zero or until target order
        for (int k = 1; k <= targetOrder; k++) {
            // get current path in the right order (k)
            final String[] current = getSet(k, currentPath);
            // get target path in the right order (k)
            final String[] target = getSet(k, targetPath);
            // calculates the union path
            final String[] union = getUnion(current, target);
            // calculates the intersection path
            final String[] intersection = getIntersection(target, current);
            // calculates the difference path
            // final String[] difference = Similarity.getDifference(union,
            // intersection);
            // calculates the DN
            final BigDecimal dn = getInterByUnion(intersection, union);

            if (dn.equals(new BigDecimal("0.00"))) {
                // no more calculation. Ends loop
                break;
            }

            // calculates weight for this interaction
            final BigDecimal weight = new BigDecimal(getWeightFactor(targetOrder, k));
            // and multiplies weight and DN. After that adds the result to SIM
            sim = sim.add(dn.multiply(weight));
        }

        return sim.abs().doubleValue();
    }

    /**
     * Remove repetitive patterns. Example: "abcdcdeeefjfjfjg" would return "abcdefjg".
     * 
     * @param path
     *            the path that to remove the repeated patterns.
     * @return the path we the repetitive patterns removed.
     */
    public final CoveredPath removeLoops(final CoveredPath path) {
        final StringBuffer sb = new StringBuffer(path.toString());
        // the size of the pattern will increase each time
        for (int strSize = 1; strSize <= sb.length(); strSize++) {
            // the initial position of the cut will walk from the begin to the
            // end of the path
            for (int pos = 0; pos < sb.length(); pos++) {
                // we check the boundary. We cannot remove a pattern beyond the
                // end of the string
                if (pos + strSize + strSize > sb.length()) {
                    break;
                }
                // variable for the do {} while loop
                boolean walk = true;
                do {
                    // again we check the boundary because the sb.delete bellow
                    // will change the
                    // sb variable size
                    if (pos + strSize + strSize > sb.length()) {
                        break;
                    }
                    // get the string that repeats and compare to the
                    // subsequente pattern
                    final String strPos = sb.substring(pos, pos + strSize);
                    final String strFw = sb.substring(pos + strSize, pos + strSize + strSize);
                    // if they are equal delete one pattern
                    if (strPos.equals(strFw)) {
                        sb.delete(pos + strSize, pos + strSize + strSize);
                    } else {
                        // there is no repetitive pattern for this loop. Break
                        // the loop
                        walk = false;
                    }
                } while (walk);
            }
        }
        // return the new path without the repetitive patterns
        return CoveredPath.createCoveredPath(sb.toString());
    }

    private BigDecimal getInterByUnion(final String[] intersection, final String[] union) {
        final BigDecimal dBD = new BigDecimal(intersection.length);
        final BigDecimal uBD = new BigDecimal(union.length);
        // returns the division with 2 decimal rounded up (if needed)
        return dBD.divide(uBD, 2, BigDecimal.ROUND_HALF_UP);
    }

    private String[] getIntersection(final String[] targetPath, final String[] currentPath) {
        // checks if arguments are null
        if (targetPath == null || currentPath == null) {
            throw new IllegalArgumentException("Arguments must not be null. TargetPath: " + targetPath + " and CurrentPath:" + currentPath);
        }
        // create the list that will have the intersection of the paths
        final List<String> intersection = new ArrayList<String>();
        // transforms the array in a list so we have the .contains() method
        final List<String> expected = new ArrayList<String>(Arrays.asList(targetPath));
        // runs over the currentPath array and copy the elements that are in
        // target list to the intersection array
        for (final String element : currentPath) {
            if (expected.contains(element)) {
                intersection.add(element);
            }
        }
        // moves the strings inside the list to an array
        final String[] intersectionArray = new String[intersection.size()];
        for (final ListIterator<String> i = intersection.listIterator(); i.hasNext();) {
            final String piece = i.next();
            intersectionArray[i.previousIndex()] = piece;
        }
        // returns the array
        return intersectionArray;
    }

    private String[] getSet(final int order, final CoveredPath cPath) {
        // the order must be between 1 and path.size()
        if (order > cPath.getPath().length() || order < 1) {
            throw new IllegalArgumentException("order must greater than 1 and less than the size of path. Path size: "
                + cPath.getPath().length() + ", order entered: " + order + " for path: [" + cPath + "]");
        }
        // breaks the list of characters depending of the "order" variable
        final List<String> pieces = new ArrayList<String>();
        for (int i = 0; i + order - 1 < cPath.getPath().length(); i++) {
            final StringBuffer piece = new StringBuffer();
            for (int j = i; j < i + order; j++) {
                piece.append(cPath.getPath().charAt(j));
            }
            // adds piece only if it doesn't exist in the list of pieces
            if (!pieces.contains(piece.toString())) {
                pieces.add(piece.toString());
            }
        }
        // transforms a list to an array
        final String[] set = new String[pieces.size()];
        for (final ListIterator<String> i = pieces.listIterator(); i.hasNext();) {
            final String piece = i.next();
            set[i.previousIndex()] = piece;
        }
        // returns the array
        return set;
    }

    private String[] getUnion(final String[] targetPath, final String[] currentPath) {
        // checks if arguments are null
        if (targetPath == null || currentPath == null) {
            throw new IllegalArgumentException("Arguments must not be null. TargetPath: " + targetPath + " and CurrentPath:" + currentPath);
        }
        // create the list that will have the union of the paths
        final List<String> union = new ArrayList<String>(Arrays.asList(targetPath));
        // runs over the currentPath array and copy the elements that aren't in
        // targetPath array to the union array
        for (int i = 0; i < currentPath.length; i++) {
            if (!union.contains(currentPath[i])) {
                union.add(currentPath[i]);
            }
        }
        // moves the strings inside the list to an array
        final String[] unionArray = new String[union.size()];
        for (final ListIterator<String> i = union.listIterator(); i.hasNext();) {
            final String piece = i.next();
            unionArray[i.previousIndex()] = piece;
        }
        // returns the array
        return unionArray;
    }

    private long getWeightFactor(final int order, final int k) {
        if (order < 1 || k < 1 || k > order) {
            throw new IllegalArgumentException("Order must be greater than 1. Order: " + order
                + ". K must be greater than 1 and less than Order. K:" + k);
        }
        // by definition, order or k equals 1 returns 1
        if (order == 1 || k == 1) {
            return 1;
        }
        // returns a recursive calculation
        return (order + 2 - k) * getWeightFactor(order, k - 1);
    }

    /**
     * Gets a new fresh instance of {@link Similarity}.
     * 
     * @return fresh instance of {@link Similarity}.
     */
    public static Similarity newInstance() {
        return new Similarity();
    }
}
