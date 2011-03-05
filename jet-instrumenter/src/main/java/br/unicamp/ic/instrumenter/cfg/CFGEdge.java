package br.unicamp.ic.instrumenter.cfg;

/**
 * Class that represent the edge from our CFG.
 */
public class CFGEdge {
    private final int pred;
    private final int succ;

    /**
     * Creates an instance of {@link CFGNode}.
     * 
     * @param pred
     *            index (number) of the predecessor node (block)
     * @param succ
     *            index (number) of the successor node (block)
     */
    public CFGEdge(final int pred, final int succ) {
        this.pred = pred;
        this.succ = succ;
    }

    /**
     * Gets the index of the predecessor node.
     * 
     * @return index of the predecessor node.
     */
    public int getPred() {
        return this.pred;
    }

    /**
     * Gets the index of the successor node.
     * 
     * @return index of the successor node.
     */
    public int getSucc() {
        return this.succ;
    }

    @Override
    public String toString() {
        return "Pred:[" + this.pred + "] Succ:[" + this.succ + "]";
    }
}
