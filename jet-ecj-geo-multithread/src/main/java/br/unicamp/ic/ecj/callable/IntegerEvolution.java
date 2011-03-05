package br.unicamp.ic.ecj.callable;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang.math.NumberUtils;

import br.unicamp.ic.ecj.util.Configuration;
import br.unicamp.ic.ecj.util.Solution;

public final class IntegerEvolution implements Callable<Solution<Integer>> {

    private static final String LIB_SOOT_JAR           = "lib/soot-2.4.0.jar";
    private static final String LIB_INSTRUMENTER_JAR   = "lib/jevolutest-instrumenter-0.0.1-SNAPSHOT.jar";
    private static final String LIB_TEST_INJECTION_JAR = "lib/jevolutest-inject-0.0.1-SNAPSHOT.jar";
    public static final String  LIB_ECJ19_GEO_JAR      = "lib/jevolutest-ecj-geo-0.0.1-SNAPSHOT.jar";
    public static final String  LIB_ECJ19_JAR          = "lib/ecj-19.jar";

    private static final String INVOKE_CLASS           = "br.unicamp.ic.ecj.GEOEvolve";
    private static final String INVOKE_METHOD          = "evolveInt";
    private static final int    TAU_ITERATIONS         = 100;

    private final List<Integer> path;
    private final Configuration config;
    private String              seed                   = "time";

    public IntegerEvolution(final List<Integer> path, final Configuration config) {
        this.path = path;
        this.config = config;
    }

    public IntegerEvolution(final List<Integer> path, final Configuration config, final String seed) {
        this.path = path;
        this.config = config;
        if (!NumberUtils.isNumber(seed)) {
            throw new IllegalArgumentException("Seed parameter must be a number.");
        }
        this.seed = seed;
    }

    @Override
    public Solution<Integer> call() throws Exception {

        // load our classloader.
        final URL[] urls = new URL[] { new File(IntegerEvolution.LIB_ECJ19_JAR).toURI().toURL(),
            new File(IntegerEvolution.LIB_ECJ19_GEO_JAR).toURI().toURL(),
            new File(IntegerEvolution.LIB_TEST_INJECTION_JAR).toURI().toURL(),
            new File(IntegerEvolution.LIB_INSTRUMENTER_JAR).toURI().toURL(), new File(IntegerEvolution.LIB_SOOT_JAR).toURI().toURL() };
        final ClassLoader loader = new URLClassLoader(urls);

        // gets our reflection instances.
        final Class<?> main = loader.loadClass(IntegerEvolution.INVOKE_CLASS);
        final Object o = main.newInstance();
        final Method m = getMethod(IntegerEvolution.INVOKE_METHOD, main);

        // prepare to call.
        final Object[] args = new Object[] { this.config.getTau(), IntegerEvolution.TAU_ITERATIONS, this.config.getMax(),
            this.config.getMin(), this.config.getGenerations(), this.path, this.seed, this.config.getLocal(), this.config.getClassName(),
            this.config.getMethodName(), this.config.getParameters(), this.config.getReturnType() };

        @SuppressWarnings("unchecked")
        final List<Object> ret = (List<Object>) m.invoke(o, args);

        // get the response and return.
        final int[] i = (int[]) ret.get(2);
        final Integer[] I = new Integer[i.length];
        for (int c = 0; c < i.length; c++) {
            I[c] = i[c];
        }
        return new Solution<Integer>((Boolean) ret.get(1), I, (Integer) ret.get(0), this.path, (Long) ret.get(3));
    }

    private Method getMethod(final String name, final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException("Could not find method named [" + name + "] in class [" + clazz.getName() + "]");
    }
}
