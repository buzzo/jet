package br.unicamp.ic.fitness;

import java.util.List;

public final class CoveredPath {
    private static final String ALPHA = "abcdefghijklmnopqrstuvxywz";
    private final StringBuffer  path;

    /**
     * Private constructor to avoid creating instance. Use {@link #newInstance()}, {@link #createCoveredPath(List)} or
     * {@link #createCoveredPath(String)} instead of the constructor.
     */
    private CoveredPath() {
        this.path = new StringBuffer();
    }

    /**
     * Appends this char to the {@link String} even if it was appended previously.
     * 
     * @param step
     *            char that is appended to the current path.
     */
    public void addStep(final char step) {
        this.path.append(step);
    }

    /**
     * Appends this {@link String} to the current path.
     * 
     * @param step
     *            {@link Integer} that is appended to the current path.
     * @throws IllegalArgumentException
     *             if the <code>step</code> is less than 0 and greater than 25.
     */
    public void addSteps(final Integer step) {
        if (step < 0 || step > 25) {
            throw new IllegalArgumentException("[" + CoveredPath.class.getName()
                + "] cannot accept numbers less than 0 and greater than 25. Your value [" + step + "]");
        }
        this.path.append(CoveredPath.ALPHA.charAt(step));
    }

    /**
     * Appends this {@link String} to the current path.
     * 
     * @param steps
     *            {@link String} that is appended to the current path.
     */
    public void addSteps(final String steps) {
        this.path.append(steps);
    }

    /**
     * Get the hole path as {@link String}.
     * 
     * @return the hole path as {@link String}.
     */
    public String getPath() {
        return this.path.toString();
    }

    /**
     * Prints the path {@link String}.
     */
    @Override
    public String toString() {
        return this.path.toString();
    }

    /**
     * Creates a new {@link CoveredPath} with the full path described by the {@link List} parameter.
     * 
     * @param list
     *            a {@link List} that describes a path.
     * @return new {@link CoveredPath} with the path described by the {@link List}.
     */
    public static CoveredPath createCoveredPath(final List<Integer> list) {
        final CoveredPath path = CoveredPath.newInstance();
        for (final Integer i : list) {
            path.addSteps(i);
        }
        return path;
    }

    /**
     * Creates a new {@link CoveredPath} with the full path described by the {@link String} parameter.
     * 
     * @param str
     *            a {@link String} that describes a path.
     * @return new {@link CoveredPath} with the path described by the {@link String}.
     */
    public static CoveredPath createCoveredPath(final String str) {
        final CoveredPath path = CoveredPath.newInstance();
        path.addSteps(str);
        return path;
    }

    /**
     * Gets a new instance of {@link CoveredPath}.
     * 
     * @return new instance of {@link CoveredPath}.
     */
    public static CoveredPath newInstance() {
        return new CoveredPath();
    }
}
