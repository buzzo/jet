package br.unicamp.ic.instrumenter.reflect;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import br.unicamp.ic.instrumenter.io.IOHelper;

/**
 * Class that represents a instrumented instance of a class.<br>
 * <br>
 * This class is not thread safe!
 */
public final class InstrumentedInstance {
    private final InstrumentedClass iClass;
    private final Class<?>          clazz;
    private final Object            instance;

    /**
     * Creates a instance of {@link InstrumentedInstance}.
     * 
     * @param iClass
     *            the {@link InstrumentedClass} that will be instanced.
     */
    private InstrumentedInstance(final InstrumentedClass iClass) {
        final String tmpDirStr = System.getProperty("java.io.tmpdir");
        if (StringUtils.trimToNull(tmpDirStr) == null) {
            throw new RuntimeException("Cannot get 'java.io.tmpdir' system propertie.");
        }
        try {
            // we want a random dir inside tmp so multiple threads wont fight for the same I/O file.
            final File tmp = new File(tmpDirStr + File.separatorChar + new Random().nextInt());
            final URL classpath = tmp.toURI().toURL();
            this.iClass = iClass;
            // write the instrumented class to some place (tmp).
            IOHelper.newInstance().writeJavaByteCode(classpath, this.iClass.getInternalClass());
            IOHelper.newInstance().writeReadableByteCode(classpath, this.iClass.getInternalClass());
            final String className = iClass.getInternalClass().getName();
            // gets a classloader to get this instrumented new class.
            final ClassLoader loader = IOHelper.newInstance().getCustomClassLoader(classpath, className, this.iClass.getBaseClasspath());
            // loads class from classloader.
            this.clazz = loader.loadClass(className);
            // creates a instance of the instrumented class.
            this.instance = this.clazz.newInstance();
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clear the path of the CFG made by the execution of a method of this class. Its just clean the 'java.util.List'
     * that can be retrieved with the {{@link #getTrace()}.
     * 
     * @throws RuntimeException
     *             if have any problem finding the internal method and calling it.
     */
    public void clearTrace() {
        try {
            final Method method = this.clazz.getMethod("init" + StringUtils.capitalize(this.iClass.getUniqueName()), new Class<?>[] {});
            method.invoke(this.instance, new Object[] {});
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a instance of the instrumented class.
     * 
     * @return instance of the instrumented class.
     */
    public Object getInstrumentedInstance() {
        return this.instance;
    }

    /**
     * Gets a {@link List} of the visited nodes of the CFG of the instrumented method.
     * 
     * @return {@link List} of the visited nodes of the CFG of the instrumented method.
     * @throws RuntimeException
     *             if have any problem finding the internal method and calling it.
     */
    public List<Integer> getTrace() {
        try {
            final Method method = this.clazz.getMethod("get" + StringUtils.capitalize(this.iClass.getUniqueName()), new Class<?>[] {});
            // suppressed because we are sure that this method is ours (we instrumented and created it) and the return
            // type is 'java.util.List'.
            @SuppressWarnings("unchecked")
            final List<Integer> list = (List<Integer>) method.invoke(this.instance, new Object[] {});
            return list;
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a new fresh instance of {@link InstrumentedInstance}.
     * 
     * @param iClass
     *            the instrumented class necessary to get a instance.
     * 
     * @return fresh instance of {@link InstrumentedInstance}.
     */
    public static InstrumentedInstance newInstance(final InstrumentedClass iClass) {
        return new InstrumentedInstance(iClass);
    }
}
