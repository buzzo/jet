package br.unicamp.ic.ecj.problem;

import br.unicamp.ic.ecj.geo.GEOSubpopulation;
import ec.EvolutionState;
import ec.Problem;

/**
 * Just a fake implementation for the ECJ instantiate.
 */
public class FakeProblem extends Problem implements TestProblem {
    private static final long serialVersionUID = 1L;

    @Override
    public void evaluate(final EvolutionState state, final GEOSubpopulation geopop) {
    }

}
