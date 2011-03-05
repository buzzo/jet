package br.unicamp.ic.ecj.util;

import java.util.List;

/**
 * Holds the solutions of a problem.
 * 
 * @param <T>
 *            type of the solution array returned.
 */
public class Solution<T> {
    private final boolean       found;
    private final T[]           solutions;
    private final int           generations;
    private final List<Integer> path;
    private final long          time;

    public Solution(final boolean found, final T[] solutions, final int generations, final List<Integer> path, final long time) {
        this.found = found;
        this.solutions = solutions;
        this.generations = generations;
        this.path = path;
        this.time = time;
    }

    /**
     * Get the time spend calculating solution (miliseconds).
     * 
     * @return time spend calculating solution (miliseconds).
     */
    public long getTime() {
        return time;
    }

    public List<Integer> getPath() {
        return path;
    }

    public int getGenerations() {
        return this.generations;
    }

    public T[] getSolutions() {
        return this.solutions;
    }

    public boolean isFound() {
        return this.found;
    }
}
