package br.unicamp.ic.ecj.geo;

import ec.Fitness;
import ec.util.Parameter;

public class GEOFitness extends Fitness {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean betterThan(final Fitness fitness) {
        return false;
    }

    @Override
    public Parameter defaultBase() {
        return null;
    }

    @Override
    public boolean equivalentTo(final Fitness fitness) {
        return false;
    }

    @Override
    public float fitness() {
        return 0;
    }

    @Override
    public boolean isIdealFitness() {
        return false;
    }

}
