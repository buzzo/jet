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

public final class DoubleEvolution implements Callable<Solution<Double>> {

    private static final String LIB_SOOT_JAR           = "lib/soot-2.4.0.jar";
    private static final String LIB_INSTRUMENTER_JAR   = "lib/instrumenter.jar";
    private static final String LIB_TEST_INJECTION_JAR = "lib/test-injection.jar";
    public static final String  LIB_ECJ19_GEO_JAR      = "lib/ecj19-geo.jar";
    public static final String  LIB_ECJ19_JAR          = "lib/ecj19.jar";

    private static final String INVOKE_CLASS           = "br.unicamp.ic.ecj.GEOEvolve";
    private static final String INVOKE_METHOD          = "evolveDouble";
    private static final int    TAU_ITERATIONS         = 100;

    private final List<Integer> path;
    private final Configuration config;
    private String              seed                   = "time";

    public DoubleEvolution(final List<Integer> path, final Configuration config) {
        this.path = path;
        this.config = config;
    }

    public DoubleEvolution(final List<Integer> path, final Configuration config, final String seed) {
        this.path = path;
        this.config = config;
        if (!NumberUtils.isNumber(seed)) {
            throw new IllegalArgumentException("Seed parameter must be a number.");
        }
        this.seed = seed;
    }

    @Override
    public Solution<Double> call() throws Exception {
        // load our classloader.
        final URL[] urls = new URL[] { new File(DoubleEvolution.LIB_ECJ19_JAR).toURI().toURL(),
            new File(DoubleEvolution.LIB_ECJ19_GEO_JAR).toURI().toURL(), new File(DoubleEvolution.LIB_TEST_INJECTION_JAR).toURI().toURL(),
            new File(DoubleEvolution.LIB_INSTRUMENTER_JAR).toURI().toURL(), new File(DoubleEvolution.LIB_SOOT_JAR).toURI().toURL() };
        final ClassLoader loader = new URLClassLoader(urls);

        // gets our reflection instances.
        final Class<?> main = loader.loadClass(DoubleEvolution.INVOKE_CLASS);
        final Object o = main.newInstance();
        final Method m = DoubleEvolution.getMethod(DoubleEvolution.INVOKE_METHOD, main);

        // prepare to call.
        final Object[] args = new Object[] { this.config.getTau(), DoubleEvolution.TAU_ITERATIONS, this.config.getMax(),
            this.config.getMin(), this.config.getGenerations(), this.path, this.seed, this.config.getLocal(), this.config.getClassName(),
            this.config.getMethodName(), this.config.getParameters(), this.config.getReturnType() };

        @SuppressWarnings("unchecked")
        final List<Object> ret = (List<Object>) m.invoke(o, args);

        // get the response and return.
        final double[] d = (double[]) ret.get(2);
        final Double[] D = new Double[d.length];
        for (int c = 0; c < d.length; c++) {
            D[c] = d[c];
        }
        return new Solution<Double>((Boolean) ret.get(1), D, (Integer) ret.get(0), this.path, (Long) ret.get(3));
    }

    private static Method getMethod(final String name, final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException("Could not find method named [" + name + "] in class [" + clazz.getName() + "]");
    }
}
