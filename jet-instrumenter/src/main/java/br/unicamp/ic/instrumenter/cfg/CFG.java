package br.unicamp.ic.instrumenter.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a CFG with nodes and edges.
 */
public class CFG {
    private final List<CFGNode> nodes;
    private final List<CFGEdge> edges;
    private final List<CFGNode> heads;

    /**
     * Creates a CFG with nodes and edges.
     * 
     * @param nodes
     *            list of nodes of the CFG.
     * @param edges
     *            list of edges of the CFG.
     * @param heads
     *            list of heads of the CFG.
     */
    public CFG(final List<CFGNode> nodes, final List<CFGEdge> edges, final List<CFGNode> heads) {
        this.nodes = nodes;
        this.edges = edges;
        this.heads = heads;
    }

    /**
     * Avoid using this method. A loop will lock it!
     */
    @Deprecated
    public void calculatePaths() {
        final Map<Integer, List<Integer>> m = new HashMap<Integer, List<Integer>>();

        for (final CFGEdge edge : this.edges) {
            final int pred = edge.getPred();
            final List<Integer> list = m.get(pred);
            if (list == null) {
                final List<Integer> succs = new ArrayList<Integer>();
                succs.add(edge.getSucc());
                m.put(pred, succs);
            } else {
                list.add(edge.getSucc());
            }
        }

        final Deque<Integer> queue = new LinkedList<Integer>();

        System.out.println("----- paths -----");
        // first call for a recursive visit
        visitPath(this.heads.get(0).getIndex(), m, queue);
        System.out.println("-----------------");
    }

    /**
     * Get edges of this CFG.
     * 
     * @return edges of this CFG.
     */
    public List<CFGEdge> getEdges() {
        return Collections.unmodifiableList(this.edges);
    }

    /**
     * Get the list of heads for this CFG.
     * 
     * @return the list of heads for this CFG.
     */
    public List<CFGNode> getHead() {
        return this.heads;
    }

    /**
     * Get nodes of this CFG.
     * 
     * @return nodes of this CFG.
     */
    public List<CFGNode> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    /**
     * Recursive call to visit all paths of a CFG.
     * 
     * @param current
     *            current node.
     * @param m
     *            {@link Map} of edges.
     * @param list
     *            list of visited nodes.
     */
    private void visitPath(final int current, final Map<Integer, List<Integer>> m, final Deque<Integer> list) {
        final List<Integer> succs = m.get(current);

        list.addLast(current);

        if (succs == null) {
            // reached the end of the path (the last node has no successors).
            System.out.println(list);
            return;
        }

        for (final Integer succ : succs) {
            visitPath(succ, m, list);
            list.removeLast();
        }

    }

}
