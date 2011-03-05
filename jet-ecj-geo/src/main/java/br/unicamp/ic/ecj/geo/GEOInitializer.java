package br.unicamp.ic.ecj.geo;

import ec.EvolutionState;
import ec.Initializer;
import ec.Population;
import ec.util.Parameter;

public class GEOInitializer extends Initializer {
    private static final long serialVersionUID = 1L;

    @Override
    public Population initialPopulation(final EvolutionState state, final int thread) {
        final Population p = setupPopulation(state, thread);
        p.populate(state, thread);
        return p;
    }

    public void setup(final EvolutionState state, final Parameter base) {
    }

    @Override
    public Population setupPopulation(final EvolutionState state, final int thread) {
        final Parameter base = new Parameter(Initializer.P_POP);
        final Population p = (Population) state.parameters.getInstanceForParameterEq(base, null, Population.class); // Population.class
        p.setup(state, base);
        return p;
    }
}
