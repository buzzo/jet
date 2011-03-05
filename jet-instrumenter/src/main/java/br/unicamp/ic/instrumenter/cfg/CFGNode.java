package br.unicamp.ic.instrumenter.cfg;

import soot.jimple.Stmt;

/**
 * Class that represent the node from our CFG.
 */
public class CFGNode {
    private final int  index;
    private final Stmt stmt;

    /**
     * Creates an instance of {@link CFGNode}.
     * 
     * @param index
     *            index (number) of this node.
     * @param stmt
     *            first statement of this block (node).
     */
    public CFGNode(final int index, final Stmt stmt) {
        this.index = index;
        this.stmt = stmt;
    }

    /**
     * Gets the index of this node.
     * 
     * @return index of this node.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Gets the first statement of this block (node).
     * 
     * @return the first statement of this block (node).
     */
    public Stmt getStmt() {
        return this.stmt;
    }

    @Override
    public String toString() {
        return "Index:[" + this.index + "] Stmt:[" + this.stmt + "]";
    }
}
