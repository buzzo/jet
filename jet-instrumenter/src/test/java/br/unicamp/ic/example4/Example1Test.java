package br.unicamp.ic.example4;

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

public class Example1Test {

    /**
     * Two method in class.<br>
     * Method with one parameter.<br>
     * Called one time.<br>
     * <br>
     * Simple CFG.<br>
     * No 'System.out.println' instrumentation.<br>
     * <br>
     * 
     * @throws Exception
     *             any error occurs.
     */
    @Test
    public void test1() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.example4.Example1";
        final String methodName = "listTest";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName);
        // simple CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = Example1Test.getMethod(methodName, clazz);
        method.invoke(o, new Object[] { 0 });
        final List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 7 nodes long.", 7, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 1, 2, 1, 2, 3 }), path);
    }

    /**
     * Two method in class.<br>
     * Method with one parameter.<br>
     * Called one time.<br>
     * <br>
     * Full CFG.<br>
     * No 'System.out.println' instrumentation.<br>
     * <br>
     * 
     * @throws Exception
     *             any error occurs.
     */
    @Test
    public void test2() throws Exception {
        // --------------------- Context -------------------------------
        final URL[] urls = new URL[] { TestResource.LOCAL.toURI().toURL() };
        final String className = "br.unicamp.ic.example4.Example1";
        final String methodName = "test";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final Class<?>[] parametersType = new Class<?>[] { Integer.class };
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, parametersType, Integer.class);
        // full CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, true);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = Example1Test.getMethod(methodName, clazz);
        method.invoke(o, new Object[] { 1 });
        final List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 5 nodes long.", 5, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 1, 2, 3 }), path);
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
