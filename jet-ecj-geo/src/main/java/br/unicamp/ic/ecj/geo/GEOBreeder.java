package br.unicamp.ic.ecj.geo;

import ec.Breeder;
import ec.EvolutionState;
import ec.Individual;
import ec.Population;
import ec.Subpopulation;
import ec.util.Parameter;
import ec.vector.VectorIndividual;

public class GEOBreeder extends Breeder {
    private static final long serialVersionUID = 1L;

    @Override
    public Population breedPopulation(final EvolutionState state) {
        final Subpopulation subpop = state.population.subpops[0];
        // check the class of the subpopulation.
        if (!(subpop instanceof GEOSubpopulation)) {
            state.output.fatal("SubPopulation should be of class [" + GEOSubpopulation.class.getName() + "]. It is from class ["
                + subpop.getClass().getName() + "]", null);
        }
        final GEOSubpopulation geopop = (GEOSubpopulation) subpop;

        // the current individuals.
        final Individual[] inds = geopop.individuals;
        // clear candidates.
        geopop.getCandidates().clear();

        for (int i = 0; i < inds.length; i++) {
            // we clone the original individuals so they remain untouched.
            final Individual[] vInds = clone(inds);
            final Individual ind = vInds[i];

            if (!(ind instanceof VectorIndividual)) {
                state.output.fatal("Individual must extends [" + VectorIndividual.class.getName() + "]. It is from class ["
                    + ind.getClass().getName() + "]", null);
            }
            final VectorIndividual vInd = (VectorIndividual) ind;
            // perturb the current individual.
            vInd.defaultMutate(state, 0);

            // stores our candidate population in geosubpop.
            geopop.getCandidates().add(i, new GEOCandidate(vInds));
        }
        return state.population;
    }

    /**
     * Clone the {@link Individual} array.
     */
    public Individual[] clone(final Individual[] inds) {
        final Individual[] cloned = new Individual[inds.length];
        for (int i = 0; i < inds.length; i++) {
            cloned[i] = (Individual) inds[i].clone();
        }
        return cloned;
    }

    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        if (state.breedthreads != 1) {
            state.output.fatal("Current class [" + GEOSubpopulation.class.getName()
                + "] only support one thread! Please set parameter [breedthreads] to 1", null);
        }
        if (state.parameters.getInt(new Parameter("pop").push("subpops"), null) != 1) {
            state.output.fatal("Current class [" + GEOSubpopulation.class.getName()
                + "] only support one SubPopulation! Please set parameter [pop.subpops] to 1", null);
        }
    }

}
