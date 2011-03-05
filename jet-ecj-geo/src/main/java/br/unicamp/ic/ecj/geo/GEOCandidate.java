package br.unicamp.ic.ecj.geo;

import ec.Individual;

public class GEOCandidate {

    private Individual[] individuals;
    private double       fitness;
    private boolean      found;

    public GEOCandidate() {
    }

    public GEOCandidate(final Individual[] inds) {
        this.individuals = inds;
    }

    public double getFitness() {
        return this.fitness;
    }

    public Individual[] getIndividuals() {
        return this.individuals;
    }

    public boolean isFound() {
        return this.found;
    }

    public void setFitness(final double fitness) {
        this.fitness = fitness;
    }

    public void setFound(final boolean found) {
        this.found = found;
    }

    public void setIndividuals(final Individual[] individuals) {
        this.individuals = individuals;
    }

}
