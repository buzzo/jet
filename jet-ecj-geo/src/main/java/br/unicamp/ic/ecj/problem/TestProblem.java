package br.unicamp.ic.ecj.problem;

import br.unicamp.ic.ecj.geo.GEOSubpopulation;
import ec.EvolutionState;

/**
 * Problem interface for our problems.
 */
public interface TestProblem {

    public void evaluate(final EvolutionState state, final GEOSubpopulation geopop);

}
