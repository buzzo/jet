package br.unicamp.ic.ecj.geo;

import ec.EvolutionState;
import ec.simple.SimpleEvolutionState;
import ec.util.Checkpoint;

/**
 * The main difference in this {@link GEOEvolutionState} from {@link SimpleEvolutionState} is that we breed first and
 * then we evaluate the population.
 */
public class GEOEvolutionState extends EvolutionState {
    private static final long serialVersionUID = 1L;

    @Override
    public int evolve() {
        // PRE-BREEDING EXCHANGING
        this.statistics.prePreBreedingExchangeStatistics(this);
        this.population = this.exchanger.preBreedingExchangePopulation(this);
        this.statistics.postPreBreedingExchangeStatistics(this);

        final String exchangerWantsToShutdown = this.exchanger.runComplete(this);
        if (exchangerWantsToShutdown != null) {
            this.output.message(exchangerWantsToShutdown);
            /*
             * Don't really know what to return here. The only place I could find where runComplete ever returns
             * non-null is IslandExchange. However, that can return non-null whether or not the ideal individual was
             * found (for example, if there was a communication error with the server).
             * 
             * Since the original version of this code didn't care, and the result was initialized to R_SUCCESS before
             * the while loop, I'm just going to return R_SUCCESS here.
             */

            return EvolutionState.R_SUCCESS;
        }

        // BREEDING
        this.statistics.preBreedingStatistics(this);

        this.population = this.breeder.breedPopulation(this);

        // POST-BREEDING EXCHANGING
        this.statistics.postBreedingStatistics(this);

        // POST-BREEDING EXCHANGING
        this.statistics.prePostBreedingExchangeStatistics(this);
        this.population = this.exchanger.postBreedingExchangePopulation(this);
        this.statistics.postPostBreedingExchangeStatistics(this);

        // EVALUATION
        this.statistics.preEvaluationStatistics(this);
        this.evaluator.evaluatePopulation(this);
        this.statistics.postEvaluationStatistics(this);

        // SHOULD WE QUIT?
        if (this.evaluator.runComplete(this) && this.quitOnRunComplete) {
            // this.output.message("Found Ideal Individual");
            return EvolutionState.R_SUCCESS;
        }

        // SHOULD WE QUIT?
        if (this.generation == this.numGenerations - 1) {
            return EvolutionState.R_FAILURE;
        }

        // INCREMENT GENERATION AND CHECKPOINT
        this.generation++;
        if (this.checkpoint && this.generation % this.checkpointModulo == 0) {
            this.output.message("Checkpointing");
            this.statistics.preCheckpointStatistics(this);
            Checkpoint.setCheckpoint(this);
            this.statistics.postCheckpointStatistics(this);
        }

        return EvolutionState.R_NOTDONE;
    }

    /**
     * @param result
     */
    @Override
    public void finish(final int result) {
        // Output.message("Finishing");
        /* finish up -- we completed. */
        this.statistics.finalStatistics(this, result);
        this.finisher.finishPopulation(this, result);
        this.exchanger.closeContacts(this, result);
        this.evaluator.closeContacts(this, result);
    }

    @Override
    public void startFresh() {
        setup(this, null); // a garbage Parameter

        // POPULATION INITIALIZATION
        this.statistics.preInitializationStatistics(this);
        this.population = this.initializer.initialPopulation(this, 0); // unthreaded
        this.statistics.postInitializationStatistics(this);

        // INITIALIZE CONTACTS -- done after initialization to allow
        // a hook for the user to do things in Initializer before
        // an attempt is made to connect to island models etc.
        this.exchanger.initializeContacts(this);
        this.evaluator.initializeContacts(this);
    }

}
