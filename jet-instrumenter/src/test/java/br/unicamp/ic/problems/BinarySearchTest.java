package br.unicamp.ic.problems;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.unicamp.ic.TestResource;
import br.unicamp.ic.instrumenter.InstrumenterHelper;
import br.unicamp.ic.instrumenter.reflect.InstrumentedClass;
import br.unicamp.ic.instrumenter.reflect.InstrumentedInstance;
import br.unicamp.ic.instrumenter.reflect.InstrumentedMethod;

public class BinarySearchTest {

    @Test
    public void test1() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.problems.BinarySearch";
        final String methodName = "run";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, new Class[] { int[].class, int.class }, Boolean.class);
        // simple CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = BinarySearchTest.getMethod(methodName, clazz);

        // fixed size of 40.
        final int[] vector = new int[40];
        for (int c = 1; c <= 40; c++) {
            vector[c - 1] = c;
        }

        // -------------------- Execution ------------------------------
        boolean ret = (Boolean) method.invoke(o, new Object[] { vector, 20 });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertTrue("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 9 nodes long.", 9, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 6, 7, 1, 3, 5, 6, 7, 8 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Boolean) method.invoke(o, new Object[] { vector, 10 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertTrue("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 13 nodes long.", 13, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 6, 7, 1, 2, 6, 7, 1, 3, 5, 6, 7, 8 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Boolean) method.invoke(o, new Object[] { vector, 30 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertTrue("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 14 nodes long.", 14, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 6, 7, 1, 3, 4, 6, 7, 1, 3, 5, 6, 7, 8 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Boolean) method.invoke(o, new Object[] { vector, 41 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertFalse("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 33 nodes long.", 33, path.size());
        // path too long to assert.
    }

    public static Method getMethod(final String name, final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException("Could not find method named [" + name + "] in class [" + clazz.getName() + "]");
    }
}
