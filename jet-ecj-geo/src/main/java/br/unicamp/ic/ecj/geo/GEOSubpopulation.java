package br.unicamp.ic.ecj.geo;

import java.util.ArrayList;
import java.util.List;

import ec.EvolutionState;
import ec.Subpopulation;
import ec.util.Parameter;

public class GEOSubpopulation extends Subpopulation {
    private static final long  serialVersionUID = 1L;

    private List<GEOCandidate> candidates;

    public List<GEOCandidate> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(final List<GEOCandidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        // we get the size of the population from the length individuals.
        this.candidates = new ArrayList<GEOCandidate>(this.individuals.length);
    }

}
