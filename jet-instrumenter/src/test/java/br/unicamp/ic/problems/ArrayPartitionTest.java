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

public class ArrayPartitionTest {

    @Test
    public void test1() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.problems.ArrayPartition";
        final String methodName = "run";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, new Class[] { int[].class, int.class, int.class },
                                                                        int.class);
        // simple CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = ArrayPartitionTest.getMethod(methodName, clazz);

        final int[] vector = new int[] { 25, 65, 39, 93, 71, 96, 59, 32, 68, 87, 51, 2, 88 };

        // -------------------- Execution ------------------------------
        method.invoke(o, new Object[] { vector, 6, 8 });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 4, 5, 7, 6, 7, 8, 9, 11, 4, 3, 4, 5, 7, 8, 10 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { vector, 8, 6 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1 }), path);
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
