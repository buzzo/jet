package br.unicamp.ic.ecj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.unicamp.ic.ecj.geo.GEOCandidate;
import br.unicamp.ic.ecj.geo.GEOEvolutionState;
import br.unicamp.ic.ecj.geo.GEOSubpopulation;
import br.unicamp.ic.ecj.problem.DoubleTestProblem;
import br.unicamp.ic.ecj.problem.FloatTestProblem;
import br.unicamp.ic.ecj.problem.IntTestProblem;
import br.unicamp.ic.inject.ParametersHelper;
import br.unicamp.ic.instrumenter.InstrumenterHelper;
import br.unicamp.ic.instrumenter.reflect.InstrumentedClass;
import br.unicamp.ic.instrumenter.reflect.InstrumentedInstance;
import br.unicamp.ic.instrumenter.reflect.InstrumentedMethod;
import ec.EvolutionState;
import ec.Evolve;
import ec.Problem;
import ec.Subpopulation;
import ec.util.ParameterDatabase;
import ec.vector.DoubleVectorIndividual;
import ec.vector.FloatVectorIndividual;
import ec.vector.IntegerVectorIndividual;

public class GEOEvolve {

    /**
     * Our problem that will be instantied here. Later we override the instance created by ECJ inside the setup() of the
     * evaluator (our GEOEvaluator).
     */
    public static Problem              problem;

    /**
     * These are used by the our problem inside the ECJ. Take a look inside the {@link IntTestProblem} or
     * {@link DoubleTestProblem}.
     */
    public static List<Integer>        targetPath;
    public static InstrumentedInstance iInstance;
    public static Method               method;

    /**
     * The return list has three values:<br>
     * - the first one is an integer pointing the number of generations needed.<br>
     * - the second is a boolean indicating if the algorithm successfully found the target path.<br>
     * - the third is an array of the type {@link Double} with the parameters found.<br>
     * - the fourth is a long showing the time in miliseconds spend in the calculation.
     */
    public List<Object> evolveDouble(final float tau, final int tauIteration, final float max, final float min, final int generations,
        final List<Integer> targetPath, final String seed, final File local, final String className, final String methodName,
        final Class<?>[] parameters, final Class<?> returnType) throws URISyntaxException, FileNotFoundException, IOException {

        final int popsize = prepare(local, className, methodName, parameters, returnType);

        GEOEvolve.problem = new DoubleTestProblem(GEOEvolve.iInstance, GEOEvolve.method, targetPath);

        final ClassLoader contextClassLoader = getClass().getClassLoader();
        final ParameterDatabase pd = new ParameterDatabase(contextClassLoader.getResourceAsStream("META-INF/double.properties"));
        pd.put("eval.tau", String.valueOf(tau));
        pd.put("eval.tau.iteration", String.valueOf(tauIteration));
        pd.put("pop.subpop.0.species.max-gene", String.valueOf(max));
        pd.put("pop.subpop.0.species.min-gene", String.valueOf(min));
        pd.put("pop.subpop.0.size", String.valueOf(popsize));
        pd.put("seed.0", String.valueOf(seed));
        pd.put("pop.subpop.0.species.mutation-stdev", String.valueOf((max - min) / 2));
        pd.put("generations", String.valueOf(generations));

        final Date begin = new Date();
        final EvolutionState state = GEOEvolve.startEvolve(pd);
        final Date end = new Date();

        final double[] params = new double[popsize];
        for (int i = 0; i < popsize; i++) {
            final double x = ((DoubleVectorIndividual) state.population.subpops[0].individuals[i]).genome[0];
            params[i] = x;
        }
        final List<Object> ret = new ArrayList<Object>();
        ret.add(state.generation);
        ret.add(runComplete(state));
        ret.add(params);
        ret.add(end.getTime() - begin.getTime());
        return ret;
    }

    /**
     * The return list has three values:<br>
     * - the first one is an integer pointing the number of generations needed.<br>
     * - the second is a boolean indicating if the algorithm successfully found the target path.<br>
     * - the third is an array of the type {@link Float} with the parameters found.<br>
     * - the fourth is a long showing the time in miliseconds spend in the calculation.
     */
    public List<Object> evolveFloat(final float tau, final int tauIteration, final float max, final float min, final int generations,
        final List<Integer> targetPath, final String seed, final File local, final String className, final String methodName,
        final Class<?>[] parameters, final Class<?> returnType) throws URISyntaxException, FileNotFoundException, IOException {

        final int popsize = prepare(local, className, methodName, parameters, returnType);

        GEOEvolve.problem = new FloatTestProblem(GEOEvolve.iInstance, GEOEvolve.method, targetPath);

        final ClassLoader contextClassLoader = getClass().getClassLoader();
        final ParameterDatabase pd = new ParameterDatabase(contextClassLoader.getResourceAsStream("META-INF/float.properties"));
        pd.put("eval.tau", String.valueOf(tau));
        pd.put("eval.tau.iteration", String.valueOf(tauIteration));
        pd.put("pop.subpop.0.species.max-gene", String.valueOf(max));
        pd.put("pop.subpop.0.species.min-gene", String.valueOf(min));
        pd.put("pop.subpop.0.size", String.valueOf(popsize));
        pd.put("seed.0", String.valueOf(seed));
        pd.put("pop.subpop.0.species.mutation-stdev", String.valueOf((max - min) / 2));
        pd.put("generations", String.valueOf(generations));

        final Date begin = new Date();
        final EvolutionState state = GEOEvolve.startEvolve(pd);
        final Date end = new Date();

        final float[] params = new float[popsize];
        for (int i = 0; i < 3; i++) {
            final float x = ((FloatVectorIndividual) state.population.subpops[0].individuals[i]).genome[0];
            params[i] = x;
        }
        final List<Object> ret = new ArrayList<Object>();
        ret.add(state.generation);
        ret.add(runComplete(state));
        ret.add(params);
        ret.add(end.getTime() - begin.getTime());
        return ret;
    }

    /**
     * The return list has three values:<br>
     * - the first one is an integer pointing the number of generations needed.<br>
     * - the second is a boolean indicating if the algorithm successfully found the target path.<br>
     * - the third is an array of the type {@link Integer} with the parameters found.<br>
     * - the fourth is a long showing the time in miliseconds spend in the calculation.
     */
    public List<Object> evolveInt(final float tau, final int tauIteration, final int max, final int min, final int generations,
        final List<Integer> targetPath, final String seed, final File local, final String className, final String methodName,
        final Class<?>[] parameters, final Class<?> returnType) throws URISyntaxException, FileNotFoundException, IOException {

        final int popsize = prepare(local, className, methodName, parameters, returnType);

        GEOEvolve.problem = new IntTestProblem(GEOEvolve.iInstance, GEOEvolve.method, targetPath);

        final ClassLoader contextClassLoader = getClass().getClassLoader();
        final ParameterDatabase pd = new ParameterDatabase(contextClassLoader.getResourceAsStream("META-INF/int.properties"));
        pd.put("generations", String.valueOf(generations));
        pd.put("eval.tau", String.valueOf(tau));
        pd.put("eval.tau.iteration", String.valueOf(tauIteration));
        pd.put("pop.subpop.0.species.max-gene", String.valueOf(max));
        pd.put("pop.subpop.0.species.min-gene", String.valueOf(min));
        pd.put("pop.subpop.0.size", String.valueOf(popsize));
        pd.put("seed.0", String.valueOf(seed));

        final Date begin = new Date();
        final EvolutionState state = GEOEvolve.startEvolve(pd);
        final Date end = new Date();

        final int[] params = new int[popsize];
        for (int i = 0; i < popsize; i++) {
            final int x = ((IntegerVectorIndividual) state.population.subpops[0].individuals[i]).genome[0];
            params[i] = x;
        }
        final List<Object> ret = new ArrayList<Object>();
        ret.add(state.generation);
        ret.add(runComplete(state));
        ret.add(params);
        ret.add(end.getTime() - begin.getTime());
        return ret;
    }

    private boolean checkIfSupported(final Class<?>[] parameterTypes) {
        for (final Class<?> parameterType : parameterTypes) {
            if (!checkIfSupportedType(parameterType)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfSupportedType(final Class<?> type) {
        if (int.class.equals(type)) {
            return true;
        } else if (Integer.class.equals(type)) {
            return true;
        } else if (double.class.equals(type)) {
            return true;
        } else if (Double.class.equals(type)) {
            return true;
        } else if (float.class.equals(type)) {
            return true;
        } else if (Float.class.equals(type)) {
            return true;
        }
        return false;
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

    private int prepare(final File local, final String className, final String methodName, final Class<?>[] parameters,
        final Class<?> returnType) throws MalformedURLException {

        System.out.println("[" + Thread.currentThread().getName() + "] - Starting to instrument.");
        final URL[] url = new URL[] { local.toURI().toURL() };
        final InstrumentedClass iClass = InstrumenterHelper.newInstance(url).instrumentClass(className);
        final InstrumentedMethod iMethod = iClass.getInstrumentedMethod(methodName, parameters, returnType);
        iMethod.getCFGAndInstrument(false, false);
        GEOEvolve.iInstance = iClass.getInstrumentedInstance();
        final Class<?> clazz = GEOEvolve.iInstance.getInstrumentedInstance().getClass();
        GEOEvolve.method = getMethod(methodName, clazz);
        System.out.println("[" + Thread.currentThread().getName() + "] - Finished to instrument.");

        Class<?>[] parameterTypes = GEOEvolve.method.getParameterTypes();
        if (!checkIfSupported(parameterTypes)) {
            // there is some parameter that is not support.
            // before we throw an error, we check if there is an injection of parameter.
            final ParametersHelper helper = ParametersHelper.newInstance(GEOEvolve.method);
            if (!helper.containsInjection()) {
                // oopps! no injection... so we cannot run this method!!!
                throw new RuntimeException("Method [" + GEOEvolve.method.getName() + "] in class [" + className
                    + "]contains a parameter class not supported!");
            }
            // so we remove that parameters that will be injected
            final int[] indexes = helper.getInjectionIndexes();
            final List<Class<?>> parameterTypesList = new ArrayList<Class<?>>();
            for (Class<?> parameterType : parameterTypes) {
                parameterTypesList.add(parameterType);
            }
            // lets remove the indexes
            for (int index : indexes) {
                // the index count starts at 1
                parameterTypesList.remove(index - 1);
            }
            parameterTypes = parameterTypesList.toArray(new Class<?>[0]);
            // and we check again, now without the injected parameters
            if (checkIfSupported(parameterTypes)) {
                // ok! all parameters supported
                return parameterTypes.length;
            } else {
                // no way... not all parameters are injected (or none are)
                throw new RuntimeException("Method [" + GEOEvolve.method.getName() + "] in class [" + className
                    + "]contains a parameter class not supported!");
            }
        } else {
            return GEOEvolve.method.getParameterTypes().length;
        }
    }

    private boolean runComplete(final EvolutionState state) {
        final Subpopulation subpop = state.population.subpops[0];
        // check the class of the subpopulation.
        if (!(subpop instanceof GEOSubpopulation)) {
            state.output.fatal("SubPopulation should be of class [" + GEOSubpopulation.class.getName() + "]. It is from class ["
                + subpop.getClass().getName() + "]", null);
        }
        final GEOSubpopulation geopop = (GEOSubpopulation) subpop;
        // we seek for any candidate subpopulation that found our solution.
        final List<GEOCandidate> candidates = geopop.getCandidates();
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).isFound()) {
                // found one candidate as solution.
                return true;
            }
        }
        // no candidate is our solution.
        return false;
    }

    public static GEOEvolve newInstance() {
        return new GEOEvolve();
    }

    private static EvolutionState startEvolve(final ParameterDatabase parameters) {
        final int job = 1;
        final GEOEvolutionState state = (GEOEvolutionState) Evolve.initialize(parameters, job);
        state.job = new Object[1];
        state.job[0] = new Integer(job);
        state.run(EvolutionState.C_STARTED_FRESH);
        Evolve.cleanup(state);
        return state;
    }
}
