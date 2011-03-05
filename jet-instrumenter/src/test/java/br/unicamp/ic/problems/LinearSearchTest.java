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

public class LinearSearchTest {

    @Test
    public void test1() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.problems.LinearSearch";
        final String methodName = "run";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, new Class[] { int[].class, int.class }, boolean.class);
        // simple CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = LinearSearchTest.getMethod(methodName, clazz);

        // fixed size of 13.
        final int[] vector = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };

        // -------------------- Execution ------------------------------
        boolean ret = (Boolean) method.invoke(o, new Object[] { vector, 1 });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertTrue("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 8 nodes long.", 8, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 4, 5, 1, 2, 3, 4, 6 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Boolean) method.invoke(o, new Object[] { vector, 2 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertTrue("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 12 nodes long.", 12, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 4, 5, 1, 3, 4, 5, 1, 2, 3, 4, 6 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Boolean) method.invoke(o, new Object[] { vector, 3 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertTrue("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 16 nodes long.", 16, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 4, 5, 1, 3, 4, 5, 1, 3, 4, 5, 1, 2, 3, 4, 6 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Boolean) method.invoke(o, new Object[] { vector, 14 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertFalse("Unexpected returned value.", ret);
        Assert.assertEquals("Path must be 56 nodes long.", 56, path.size());
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
