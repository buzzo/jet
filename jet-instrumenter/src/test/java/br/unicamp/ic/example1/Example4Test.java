package br.unicamp.ic.example1;

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

public class Example4Test {

    /**
     * More than on method in class.<br>
     * Method with two parameter.<br>
     * Called once.<br>
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
        final String className = "br.unicamp.ic.example1.Example4";
        final String methodName = "executor";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName);
        // full CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, false);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = Example4Test.getMethod(methodName, clazz);
        method.invoke(o, new Object[] { 2, 2 });
        final List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 4 nodes long.", 4, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 1, 3, 5 }), path);

    }

    /**
     * More than on method in class.<br>
     * Called twice. First one method than other.<br>
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
        final String className = "br.unicamp.ic.example1.Example4";
        // -------------------- Execution ------------------------------
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(urls).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod("call");
        // full CFG, no 'syso' instrumentation.
        iMethod.getCFGAndInstrument(false, true);
        final InstrumentedInstance instance = iClass.getInstrumentedInstance();
        final Object o = instance.getInstrumentedInstance();
        instance.clearTrace();
        final Class<?> clazz = o.getClass();
        final Method method = Example4Test.getMethod("call", clazz);
        method.invoke(o, new Object[] { -1, 2, 2 });
        final List<Integer> path = instance.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path);
        Assert.assertEquals("Path must be 4 nodes long.", 4, path.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 3, 5 }), path);

        final InstrumentedMethod iMethod2 = iClass.getInstrumentedMethod("executor");
        iMethod2.getCFGAndInstrument(false, true);
        final InstrumentedInstance instance2 = iClass.getInstrumentedInstance();
        final Object o2 = instance2.getInstrumentedInstance();
        instance2.clearTrace();
        final Class<?> clazz2 = o2.getClass();
        final Method method2 = Example4Test.getMethod("executor", clazz2);
        method2.invoke(o2, new Object[] { -1, -1 });
        final List<Integer> path2 = instance2.getTrace();
        // -------------------- Check ----------------------------------
        Assert.assertNotNull("Path list must not be null.", path2);
        Assert.assertEquals("Path must be 5 nodes long.", 5, path2.size());
        Assert.assertEquals("Wrong path.", Arrays.asList(new Integer[] { 0, 2, 3, 4, 5 }), path2);

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
