package br.unicamp.ic.ecj.problem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.unicamp.ic.ecj.geo.GEOCandidate;
import br.unicamp.ic.ecj.geo.GEOSubpopulation;
import br.unicamp.ic.fitness.CoveredPath;
import br.unicamp.ic.fitness.Similarity;
import br.unicamp.ic.inject.ParametersHelper;
import br.unicamp.ic.instrumenter.reflect.InstrumentedInstance;
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.vector.DoubleVectorIndividual;

/**
 * Class for problems with {@link Double}.
 */
public class DoubleTestProblem extends Problem implements TestProblem {
    private static final long          serialVersionUID = 1L;
    private static final Similarity    SIMILARITY       = Similarity.newInstance();

    private final InstrumentedInstance instance;
    private final Method               method;
    private final List<Integer>        target;
    private final CoveredPath          targetPath;

    // check for test parameter injection.
    private final boolean              injection;
    private int[]                      indexes;
    private Object[]                   values;

    public DoubleTestProblem(final InstrumentedInstance instance, final Method method, final List<Integer> target) {
        this.instance = instance;
        this.method = method;
        this.target = target;
        this.targetPath = CoveredPath.createCoveredPath(target);

        // we check for test parameter injection.
        final ParametersHelper helper = ParametersHelper.newInstance(this.method);
        this.injection = helper.containsInjection();
        if (injection) {
            indexes = helper.getInjectionIndexes();
            values = new Object[indexes.length];
            for (int i = 0; i < indexes.length; i++) {
                values[i] = helper.getInjectionValue(i);
            }
        }
    }

    @Override
    public void evaluate(final EvolutionState state, final GEOSubpopulation geopop) {

        final List<GEOCandidate> candidates = geopop.getCandidates();
        for (int i = 0; i < candidates.size(); i++) {
            final GEOCandidate candidate = candidates.get(i);
            final Individual[] ind = candidate.getIndividuals();

            // just create the parameter (is an array) for our method.
            final List<Object> params = new ArrayList<Object>();
            for (int j = 0; j < ind.length; j++) {
                if (!(ind[j] instanceof DoubleVectorIndividual)) {
                    state.output.fatal("Individual must be of type [" + DoubleVectorIndividual.class.getName() + "]. It is ["
                        + ind[j].getClass().getName() + "]", null);
                }
                final DoubleVectorIndividual dvInd = (DoubleVectorIndividual) ind[j];
                params.add(dvInd.genome[0]);
            }

            // now we see if there is any object to inject in parameters
            if (this.injection) {
                for (int j = 0; j < indexes.length; j++) {
                    params.add(indexes[j], values[j]);
                }
            }

            // lets get our current path.
            final List<Integer> list = run(state, params.toArray());
            // first we check if this is our target.
            candidate.setFound(list.equals(this.target));
            // and then we calculate the fitness.
            final double fit = DoubleTestProblem.SIMILARITY.evaluate(this.targetPath, CoveredPath.createCoveredPath(list));
            candidate.setFitness(fit);
        }
    }

    /**
     * Runs our problem instance to get the fresh current path.
     * 
     * @param state
     *            ECJ state.
     * @param params
     *            parameters for our method.
     * @return path run.
     */
    private List<Integer> run(final EvolutionState state, final Object[] params) {
        // clears any path run before.
        this.instance.clearTrace();
        final Object o = this.instance.getInstrumentedInstance();
        try {
            // run the method so we can have the current path.
            this.method.invoke(o, params);
        } catch (final IllegalArgumentException e) {
            state.output.fatal("Failed to invoke the problem method [" + this.method + "] for instance class of [" + o.getClass().getName()
                + "]");
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            state.output.fatal("Failed to invoke the problem method [" + this.method + "] for instance class of [" + o.getClass().getName()
                + "]");
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            state.output.fatal("Failed to invoke the problem method [" + this.method + "] for instance class of [" + o.getClass().getName()
                + "]");
            e.printStackTrace();
        }
        // get the fresh run path.
        final List<Integer> list = this.instance.getTrace();
        return list;
    }

}
