package br.unicamp.ic.instrumenter;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import br.unicamp.ic.instrumenter.reflect.InstrumentedClass;

/**
 * This is the main class where the instrumentation of a <code>.class</code> file begins. Just place the
 * <code>.class</code> inside a folder and get a instance of this class {{@link #newInstance(URL[])} passing the
 * {@link URL} to that folder. Remember to keep the package structure!<br>
 * <br>
 * Example: if you have a class called <code>Example.class</code> that is inside the packages <code>br.unicamp.ic</code>
 * you must have the file <code>Example.class</code> in this folder structure.<br>
 * <br>
 * URL ClassPath: c:/myfolder<br>
 * Structure: c:/myfolder/br/unicamp/ic/Example.class<br>
 * <br>
 * From there just call {{@link #instrumentClass(String)} passing the full class name <code>br.unicamp.ic.Example</code>
 * to get the initial {@link InstrumentedClass}.<br>
 * <br>
 * This class is not thread safe!
 */
public final class InstrumenterHelper {

    public static final String   JAVA_IO_PRINT_STREAM_PRINT = "<java.io.PrintStream: void print(java.lang.String)>";
    public static final String   JAVA_IO_PRINT_STREAM_OUT   = "<java.lang.System: java.io.PrintStream out>";
    public static final String   JAVA_IO_PRINT_STREAM       = "java.io.PrintStream";
    public static final String   JAVA_LANG_INTEGER          = "java.lang.Integer";
    public static final String   JAVA_LANG_INTEGER_VALUEOF  = "<java.lang.Integer: java.lang.Integer valueOf(int)>";
    public static final String   JAVA_LANG_SYSTEM           = "java.lang.System";
    public static final String   JAVA_UTIL_LIST_VOID_INIT   = "<java.util.ArrayList: void <init>()>";
    public static final String   JAVA_UTIL_ARRAY_LIST       = "java.util.ArrayList";
    public static final String   JAVA_UTIL_LIST             = "java.util.List";
    public static final String   JAVA_UTIL_LIST_ADD         = "<java.util.List: boolean add(java.lang.Object)>";

    private final URLClassLoader loader;

    /**
     * Private constructor. For fresh instance please check method {@link #newInstance()}.
     * 
     * @see {@link #newInstance()}.
     */
    private InstrumenterHelper(final URL[] classpath) {
        // create the classpath where we are going to search for the class that
        // will be instrumented.
        this.loader = new URLClassLoader(classpath);
    }

    /**
     * Prepares the class for instrumentation by adding/removing (if necessary) some statements in the bytecode. This
     * method <b>DO NOT</b> instruments any method inside the class.<br>
     * <br>
     * The class is searched in the system classpath. If it cannot be found the we look for the classpath referenced by
     * the {@link URL} used to construct the instance of {@link InstrumenterHelper}. If the class cannot be found a
     * {@link NullPointerException} will be thrown.<br>
     * <br>
     * An example of class name is: <b>br.com.otherpackage.FileUtilExample</b>. The name must be referenced with full
     * package and class name.
     * 
     * @param clazz
     *            the name of the class that will be instrumented.
     * @return an instance of {@link InstrumentedClass}.
     * 
     * @throws NullPointerException
     *             if the class cannot be found in any classpath.
     * @throws IllegalArgumentException
     *             if the name is empty or <code>null</code>.
     */
    public InstrumentedClass instrumentClass(final String clazz) {
        Validate.notNull(StringUtils.trimToNull(clazz), "Class name cannot be empty or null.");

        // clean all Soot singletrons so the class can be fresh loaded.
        // this operation is time expensive!
        G.reset();

        // loads java classes that soot will need.
        Scene.v().loadClassAndSupport(InstrumenterHelper.JAVA_LANG_SYSTEM);
        Scene.v().loadClassAndSupport(InstrumenterHelper.JAVA_UTIL_LIST);
        Scene.v().loadClassAndSupport(InstrumenterHelper.JAVA_UTIL_ARRAY_LIST);
        // tells soot that if the class to be instrumented cannot be find in
        // soot classpath (and it will not) then soot should search this (ours)
        // classloader.
        SourceLocator.v().additionalClassLoader(this.loader);

        // the next three lines are needed to soot load our class before
        // instrument it.
        final SootClass sClass = Scene.v().loadClassAndSupport(clazz);
        Scene.v().loadNecessaryClasses();
        sClass.setApplicationClass();
        // read all methods inside the class and tells that their body (code)
        // are active. This is necessary or when we try to write the sootClass
        // to the .class file we will have an error.
        final List<SootMethod> sms = sClass.getMethods();
        for (final SootMethod sm : sms) {
            sm.retrieveActiveBody();
        }
        // returns the new instrumented class.
        return InstrumentedClass.newInstance(sClass, this.loader.getURLs());
    }

    /**
     * Gets a new fresh instance of {@link InstrumenterHelper}.
     * 
     * @param classpath
     *            paths included as classpath.
     * 
     * @return fresh instance of {@link InstrumenterHelper}.
     */
    public static InstrumenterHelper newInstance(final URL[] classpath) {
        return new InstrumenterHelper(classpath);
    }

}
