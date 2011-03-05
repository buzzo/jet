package br.unicamp.ic.fitness;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class SimilarityTest {

    @Test
    public void testCalculation1() {
        final CoveredPath targetPath = CoveredPath.createCoveredPath("abefhjkli");
        final CoveredPath currentPath = CoveredPath.createCoveredPath("abefhjmjkljmi");
        final Similarity sim = Similarity.newInstance();
        final double eval = sim.evaluate(targetPath, currentPath);
        Assert.assertEquals("Unexpected fitness value.", 2024.28, eval);
    }

    @Test
    public void testCalculation2() {
        final CoveredPath targetPath = CoveredPath.createCoveredPath("abcbcbc");
        final CoveredPath currentPath = CoveredPath.createCoveredPath("abc");
        final Similarity sim = Similarity.newInstance();
        final double eval = sim.evaluate(targetPath, currentPath);
        Assert.assertEquals("Unexpected fitness value.", 10.00, eval);
    }

    @Test
    public void testCalculation3() {
        final CoveredPath targetPath = CoveredPath.createCoveredPath("abefhjkli");
        final CoveredPath currentPath = CoveredPath.createCoveredPath("abefhi");
        final Similarity sim = Similarity.newInstance();
        final double eval = sim.evaluate(targetPath, currentPath);
        Assert.assertEquals("Unexpected fitness value.", 692.23, eval);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculation4() {
        final CoveredPath targetPath = CoveredPath.createCoveredPath("abacdhl");
        final CoveredPath currentPath = CoveredPath.createCoveredPath(Arrays.asList(new Integer[] { 0, 2, 3, 7 }));
        final Similarity sim = Similarity.newInstance();
        sim.evaluate(targetPath, currentPath);
    }

    @Test
    public void testRemoveLoops1() {
        final CoveredPath path = CoveredPath.createCoveredPath("abbbbc");
        final Similarity sim = Similarity.newInstance();
        final CoveredPath eval = sim.removeLoops(path);
        Assert.assertEquals("Loop not removed.", "abc", eval.getPath());
    }

    @Test
    public void testRemoveLoops2() {
        final CoveredPath path = CoveredPath.createCoveredPath("abbbbccddddee");
        final Similarity sim = Similarity.newInstance();
        final CoveredPath eval = sim.removeLoops(path);
        Assert.assertEquals("Loop not removed.", "abcde", eval.getPath());
    }

    @Test
    public void testRemoveLoops3() {
        final CoveredPath path = CoveredPath.createCoveredPath("ababcccdedee");
        final Similarity sim = Similarity.newInstance();
        final CoveredPath eval = sim.removeLoops(path);
        Assert.assertEquals("Loop not removed.", "abcde", eval.getPath());
    }

    @Test
    public void testRemoveLoops4() {
        final CoveredPath path = CoveredPath.createCoveredPath("abcdededefghfghfghdedejjj");
        final Similarity sim = Similarity.newInstance();
        final CoveredPath eval = sim.removeLoops(path);
        Assert.assertEquals("Loop not removed.", "abcdefghdej", eval.getPath());
    }

}
