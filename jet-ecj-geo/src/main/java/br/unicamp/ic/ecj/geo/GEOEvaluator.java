package br.unicamp.ic.ecj.geo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.unicamp.ic.ecj.GEOEvolve;
import br.unicamp.ic.ecj.problem.TestProblem;
import ec.ECDefaults;
import ec.Evaluator;
import ec.EvolutionState;
import ec.Subpopulation;
import ec.util.Parameter;

public class GEOEvaluator extends Evaluator {
    private static final long  serialVersionUID = 1L;

    public static final String ITERATION        = "iteration";
    public static final String TAU              = "tau";
    public float               tau;
    public int                 maxIteration;

    @Override
    public void evaluatePopulation(final EvolutionState state) {
        // already checked in setup().
        final TestProblem problem = (TestProblem) this.p_problem;

        final Subpopulation subpop = state.population.subpops[0];
        // check the class of the subpopulation.
        if (!(subpop instanceof GEOSubpopulation)) {
            state.output.fatal("SubPopulation should be of class [" + GEOSubpopulation.class.getName() + "]. It is from class ["
                + subpop.getClass().getName() + "]", null);
        }
        final GEOSubpopulation geopop = (GEOSubpopulation) subpop;
        // evaluate all candidates subpopulation.
        problem.evaluate(state, geopop);

        // now we choose the new subpopulation from candidates.
        // first, we sort using the fitness value. Sort from the greater to the smaller fitness.
        Collections.sort(geopop.getCandidates(), new GeoCandidateSorter());

        // here we choose the next population (individuals). Notice that canditates with greater fitness have more
        // chance to be choosen than those with smaller fitness.
        int counter = 0;
        final int size = geopop.getCandidates().size();
        while (counter < this.maxIteration) {
            final int k = state.random[0].nextInt(size) + 1;
            final double ran = state.random[0].nextDouble();
            final double pik = Math.pow(k, -1 * this.tau);
            if (pik >= ran) {
                geopop.individuals = geopop.getCandidates().get(k - 1).getIndividuals();
                // System.out.println("[" + k + "]" + geopop.getCandidates().get(k - 1).getFitness());
                break;
            }
            counter++;
        }
        if (counter >= this.maxIteration) {
            state.output.fatal("Could not calculate next population. Ceiling loop calculations of [k pow -tau] reached. "
                + "Try a bigger number of parameter [eval.tau.iteration]", null);
        }
    }

    @Override
    public boolean runComplete(final EvolutionState state) {
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

    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        // now we override the problem loaded with our problem full prepared.
        this.p_problem = GEOEvolve.problem;
        // checks our interface.
        if (!(this.p_problem instanceof TestProblem)) {
            state.output.fatal("[" + this.getClass() + "] used, but the Problem is not of [" + TestProblem.class.getName() + "]", base
                .push(Evaluator.P_PROBLEM));
        }
        // checks that we support only one thread.
        if (state.evalthreads != 1) {
            state.output.fatal("Current class [" + GEOEvaluator.class.getName()
                + "] only support one thread! Please set parameter [evalthreads] to 1", null);
        }
        // loads tau value.
        this.tau = state.parameters.getFloat(base.push(GEOEvaluator.TAU), ECDefaults.base().push(GEOEvaluator.TAU));
        this.maxIteration = state.parameters.getInt(base.push(GEOEvaluator.TAU).push(GEOEvaluator.ITERATION), null);
    }

    private static class GeoCandidateSorter implements Comparator<GEOCandidate> {
        @Override
        public int compare(final GEOCandidate o1, final GEOCandidate o2) {
            final Double d = o2.getFitness() - o1.getFitness();
            return d.intValue();
        }
    }
}
