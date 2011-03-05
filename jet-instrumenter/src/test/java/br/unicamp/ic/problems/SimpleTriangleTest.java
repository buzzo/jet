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

public class SimpleTriangleTest {

    @Test
    public void test1() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.problems.SimpleTriangle";
        final String methodName = "run";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, new Class[] { int.class, int.class, int.class },
                                                                        int.class);
        // simple CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = SimpleTriangleTest.getMethod(methodName, clazz);

        // -------------------- Execution ------------------------------
        int ret = (Integer) method.invoke(o, new Object[] { 1, 2, 3 });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 1, ret);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 11, 12 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { 5, 4, 3 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 4, ret);
        Assert.assertEquals("Path must be 8 nodes long.", 8, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 10, 12 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { 5, 5, 5 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 2, ret);
        Assert.assertEquals("Path must be 8 nodes long.", 8, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 2, 3, 6, 7, 8, 12 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        ret = (Integer) method.invoke(o, new Object[] { 5, 4, 4 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Unexpected returned value.", 3, ret);
        Assert.assertEquals("Path must be 8 nodes long.", 8, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 6, 9, 12 }), path);
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
