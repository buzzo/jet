package br.unicamp.ic.ecj;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.unicamp.ic.TestResource;

public class GEOEvolveTest {

    @Test
    public void testEvolveDouble1() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangleDouble";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveDouble(5f, 100, 100.0f, 0.0f, 400,
                                                                      Arrays.asList(new Integer[] { 0, 11, 12 }), "4433",
                                                                      TestResource.LOCAL, className, methodName,
                                                                      new Class[] { double.class, double.class, double.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 4, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final double[] oss = (double[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", new Double(8.778303024707185), new Double(oss[0]));
        Assert.assertEquals("Unexpected value.", new Double(28.649660807858993), new Double(oss[1]));
        Assert.assertEquals("Unexpected value.", new Double(55.33651612326753), new Double(oss[2]));
    }

    @Test
    public void testEvolveDouble2() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangleDouble";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveDouble(5f, 100, 100.0f, 0.0f, 400,
                                                                      Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }), "4433",
                                                                      TestResource.LOCAL, className, methodName,
                                                                      new Class[] { double.class, double.class, double.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 399, ret.get(0));
        Assert.assertFalse("Expect not to find the solution.", (Boolean) ret.get(1));
        final double[] oss = (double[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", new Double(37.21275462135323), new Double(oss[0]));
        Assert.assertEquals("Unexpected value.", new Double(54.5541552109378), new Double(oss[1]));
        Assert.assertEquals("Unexpected value.", new Double(51.49074877555962), new Double(oss[2]));
    }

    @Test
    public void testEvolveDouble3() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangleDouble";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveDouble(5f, 100, 100.0f, 0.0f, 400,
                                                                      Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }), "4433",
                                                                      TestResource.LOCAL, className, methodName,
                                                                      new Class[] { double.class, double.class, double.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 0, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final double[] oss = (double[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", new Double(89.86346311388255), new Double(oss[0]));
        Assert.assertEquals("Unexpected value.", new Double(81.72615858003111), new Double(oss[1]));
        Assert.assertEquals("Unexpected value.", new Double(55.33651612326753), new Double(oss[2]));
    }

    @Test
    public void testEvolveFloat1() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangleFloat";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveFloat(5f, 100, 100.0f, 0.0f, 400,
                                                                     Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }), "4433",
                                                                     TestResource.LOCAL, className, methodName,
                                                                     new Class[] { float.class, float.class, float.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 399, ret.get(0));
        Assert.assertFalse("Expect not to find the solution.", (Boolean) ret.get(1));
        final float[] oss = (float[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", new Float(60.78778f), new Float(oss[0]));
        Assert.assertEquals("Unexpected value.", new Float(21.020456f), new Float(oss[1]));
        Assert.assertEquals("Unexpected value.", new Float(49.274883f), new Float(oss[2]));
    }

    @Test
    public void testEvolveFloat2() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangleFloat";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveFloat(5f, 100, 100.0f, 0.0f, 400,
                                                                     Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }), "44332",
                                                                     TestResource.LOCAL, className, methodName,
                                                                     new Class[] { float.class, float.class, float.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 1, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final float[] oss = (float[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", new Float(67.07505f), new Float(oss[0]));
        Assert.assertEquals("Unexpected value.", new Float(64.29472f), new Float(oss[1]));
        Assert.assertEquals("Unexpected value.", new Float(94.70286f), new Float(oss[2]));
    }

    @Test
    public void testEvolveFloat3() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangleFloat";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveFloat(5f, 100, 100.0f, 0.0f, 400,
                                                                     Arrays.asList(new Integer[] { 0, 11, 12 }), "44332",
                                                                     TestResource.LOCAL, className, methodName,
                                                                     new Class[] { float.class, float.class, float.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 0, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final float[] oss = (float[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", new Float(67.07505f), new Float(oss[0]));
        Assert.assertEquals("Unexpected value.", new Float(4.0682435f), new Float(oss[1]));
        Assert.assertEquals("Unexpected value.", new Float(94.70286f), new Float(oss[2]));
    }

    @Test
    public void testEvolveInt1() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangle";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveInt(5f, 100, 100, 0, 10000,
                                                                   Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }), "4433",
                                                                   TestResource.LOCAL, className, methodName,
                                                                   new Class<?>[] { int.class, int.class, int.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 280, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final int[] oss = (int[]) ret.get(2);
        for (final int os : oss) {
            Assert.assertEquals("Unexpected value.", 65, os);
        }
    }

    @Test
    public void testEvolveInt2() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangle";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveInt(5f, 100, 100, 0, 400,
                                                                   Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }), "44332",
                                                                   TestResource.LOCAL, className, methodName,
                                                                   new Class<?>[] { int.class, int.class, int.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 0, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final int[] oss = (int[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", 79, oss[0]);
        Assert.assertEquals("Unexpected value.", 84, oss[1]);
        Assert.assertEquals("Unexpected value.", 43, oss[2]);
    }

    @Test
    public void testEvolveInt3() throws Exception {
        // --------------------------------------- Context ---------------------------------------
        final String className = "br.unicamp.ic.problems.SimpleTriangle";
        final String methodName = "run";
        // --------------------------------------- Test ---------------------------------------
        final List<Object> ret = GEOEvolve.newInstance().evolveInt(5f, 100, 100, 0, 400, Arrays.asList(new Integer[] { 0, 11, 12 }),
                                                                   "4321", TestResource.LOCAL, className, methodName,
                                                                   new Class<?>[] { int.class, int.class, int.class }, int.class);
        // --------------------------------------- Verification ---------------------------------------
        Assert.assertEquals("Unexpected number of generations.", 4, ret.get(0));
        Assert.assertTrue("Expect to find the solution.", (Boolean) ret.get(1));
        final int[] oss = (int[]) ret.get(2);
        Assert.assertEquals("Unexpected value.", 11, oss[0]);
        Assert.assertEquals("Unexpected value.", 30, oss[1]);
        Assert.assertEquals("Unexpected value.", 82, oss[2]);
    }

}
