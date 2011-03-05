package br.unicamp.ic.ecj.geo;

import ec.EvolutionState;
import ec.Finisher;
import ec.util.Parameter;

public class GEOFinisher extends Finisher {
    private static final long serialVersionUID = 1L;

    @Override
    public void finishPopulation(final EvolutionState state, final int result) {
        // don't care
        return;
    }

    public void setup(final EvolutionState state, final Parameter base) {
    }
}
