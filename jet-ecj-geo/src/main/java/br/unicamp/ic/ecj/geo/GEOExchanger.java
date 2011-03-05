package br.unicamp.ic.ecj.geo;

import ec.EvolutionState;
import ec.Exchanger;
import ec.Population;
import ec.steadystate.SteadyStateExchangerForm;
import ec.util.Parameter;

public final class GEOExchanger extends Exchanger implements SteadyStateExchangerForm {
    private static final long serialVersionUID = 1L;

    /** Doesn't do anything. */
    @Override
    public void closeContacts(final EvolutionState state, final int result) {
        // don't care
        return;
    }

    /** Doesn't do anything. */
    @Override
    public void initializeContacts(final EvolutionState state) {
        // don't care
        return;
    }

    /** Simply returns state.population. */
    @Override
    public Population postBreedingExchangePopulation(final EvolutionState state) {
        // don't care
        return state.population;
    }

    /** Simply returns state.population. */
    @Override
    public Population preBreedingExchangePopulation(final EvolutionState state) {
        // don't care
        return state.population;
    }

    /** Doesn't do anything. */
    @Override
    public void reinitializeContacts(final EvolutionState state) {
        // don't care
        return;
    }

    /** Always returns null */
    @Override
    public String runComplete(final EvolutionState state) {
        return null;
    }

    public void setup(final EvolutionState state, final Parameter base) {
    }

}
