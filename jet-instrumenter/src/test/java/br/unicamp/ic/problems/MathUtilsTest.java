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

public class MathUtilsTest {

    @Test
    public void test1() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.problems.MathUtils";
        final String methodName = "addAndCheck";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, new Class[] { int.class, int.class, String.class },
                                                                        int.class);
        // simple CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = MathUtilsTest.getMethod(methodName, clazz);

        // -------------------- Execution ------------------------------
        long ret = (Integer) method.invoke(o, new Object[] { 1, 1, "" });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 2, ret);
        Assert.assertEquals("Path must be 5 nodes long.", 5, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 8, 9, 11 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { -1, -1, "" });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", -2, ret);
        Assert.assertEquals("Path must be 6 nodes long.", 6, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 3, 4, 5, 11 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { -1, 1, "" });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 0, ret);
        Assert.assertEquals("Path must be 5 nodes long.", 5, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 3, 7, 11 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { 1, Integer.MAX_VALUE, "" });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 0, ret);
        Assert.assertEquals("Path must be 4 nodes long.", 4, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 8, 10 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { 1, -1, "" });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 0, ret);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 11 }), path);
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
