package br.unicamp.ic.ecj.vector;

import ec.EvolutionState;
import ec.util.MersenneTwisterFast;
import ec.vector.IntegerVectorIndividual;
import ec.vector.IntegerVectorSpecies;

/**
 * Overrides {@link IntegerVectorIndividual} for a better perturbation at method
 * {@link #defaultMutate(EvolutionState, int)}.
 */
public class GEOIntegerVectorIndividual extends IntegerVectorIndividual {
    private static final long serialVersionUID = 1L;

    @Override
    public void defaultMutate(final EvolutionState state, final int thread) {
        final IntegerVectorSpecies s = (IntegerVectorSpecies) this.species;
        if (s.mutationProbability > 0.0) {
            for (int x = 0; x < this.genome.length; x++) {
                if (state.random[thread].nextBoolean(s.mutationProbability)) {
                    final int current = this.genome[x];
                    final int min = (int) s.minGene(x);
                    final int max = (int) s.maxGene(x);
                    final MersenneTwisterFast random = state.random[thread];
                    this.genome[x] = perturbGene(current, min, max, random);
                }
            }
        }
    }

    private int perturbGene(final int current, final int min, final int max, final MersenneTwisterFast random) {
        // multiplied by domain for a good perturbation.
        final int domain = max - min;

        final double pertubation = random.nextGaussian() * domain / 2;
        int value = new Double(current + pertubation).intValue();

        // check and keep the perturbation inside domain.
        while (value > max || value < min) {
            if (value > max) {
                // circular domain. if greater than max it loops to min.
                value = value - max;
            } else if (value < min) {
                // circular domain. if less than min it loops to max.
                value = max + value;
            }
        }

        // simple perturbation, keeps value inside domain.
        // int value;
        // do {
        // final double pertubation = random.nextGaussian() * domain / 2;
        // value = new Double(current + pertubation).intValue();
        // } while (value > max || value < min);

        // inside de domain
        return value;
    }
}
