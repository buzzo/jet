package br.unicamp.ic.example3;

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
     * Method with two parameter.<br>
     * Called four times.<br>
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
        final String className = "br.unicamp.ic.example3.Example1";
        final String methodName = "method";
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
        method.invoke(o, new Object[] { 0, 1 });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 4 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { 0, 2 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 4 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { 0, 3 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 4 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { 0, 4 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 3, 4 }), path);
    }

    /**
     * Two method in class.<br>
     * Method with two parameter.<br>
     * Called four times.<br>
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
        final String className = "br.unicamp.ic.example3.Example1";
        final String methodName = "method";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName);
        // full CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, true);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = Example1Test.getMethod(methodName, clazz);
        method.invoke(o, new Object[] { 0, 1 });
        List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 4 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { 0, 2 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 4 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { 0, 3 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 4 }), path);

        // -------------------- Execution ------------------------------
        instance.clearTrace();
        method.invoke(o, new Object[] { 0, 4 });
        path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 3 nodes long.", 3, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 3, 4 }), path);
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
