package br.unicamp.ic.instrumenter.reflect;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JIdentityStmt;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;
import br.unicamp.ic.instrumenter.InstrumenterHelper;
import br.unicamp.ic.instrumenter.cfg.CFG;
import br.unicamp.ic.instrumenter.cfg.CFGEdge;
import br.unicamp.ic.instrumenter.cfg.CFGNode;

/**
 * Class that represents the instrumented bytecode of a method.<br>
 * <br>
 * This class is not thread safe!
 */
public final class InstrumentedMethod {

    private final SootMethod        sMethod;
    private final InstrumentedClass iClass;

    /**
     * Instantiates the {@link InstrumentedMethod} and do the internal method instrumentation.
     * 
     * @param iClass
     *            {@link InstrumentedClass} for instrument information.
     * @param sMethod
     *            {@link SootMethod} for instrumentation.
     */
    private InstrumentedMethod(final InstrumentedClass iClass, final SootMethod sMethod) {
        // the base of the instrumentation is the SootMethod.
        this.iClass = iClass;
        this.sMethod = sMethod;
    }

    /**
     * Creates and return the CFG of this method. It already instruments every node of the CFG.
     * 
     * @param stdout
     *            <code>true</code> indicates that the instrumentation should include the 'System.out.println(int)' in
     *            every node of the CFG. When the method is called every node executed will print it block index.
     * @param exceptionsCFG
     *            <code>true</code> indicates that the CFG must count the try/catch blocks.
     * 
     * @return CFG of this method.
     */
    public CFG getCFGAndInstrument(final boolean stdout, final boolean exceptionsCFG) {
        // gets the body (bytecode) of the method.
        final Body sBody = this.sMethod.retrieveActiveBody();

        // -------- here we create all local variables that we will use in future instrumentation --------
        // create a local variable to hold the (System.out).
        final Local sysoLocal = Jimple.v().newLocal(InstrumentedClass.SYSO_LOCAL, RefType.v(InstrumenterHelper.JAVA_IO_PRINT_STREAM));
        sBody.getLocals().add(sysoLocal);
        // create a local variable to hold the list of the indexes of the CFG nodes (blocks) path when this method is
        // executed.
        final Local listLocal = Jimple.v().newLocal(InstrumentedClass.LIST_LOCAL, RefType.v(InstrumenterHelper.JAVA_UTIL_LIST));
        sBody.getLocals().add(listLocal);
        // ----------------------------------------------------------------------------------------------

        /*
         * now we are going to put instances inside the local variables but there is a problem: the bytecode is
         * different for each type of method. i.e: if the method is not static it has a extra bytecode line. i.e.2: for
         * each parameter there is a extra line before the method bytecode body. What we will do at the next lines is to
         * store all this bytecode lines (example just above) and create instances (using the right bytecode) to our
         * local variables. After that we re-insert all the original stored bytecode lines.
         */
        int counter = prepareLocals(sysoLocal, listLocal);

        // if we want to instrument with System.out.println then we need to jump another statement at the fist cfg node.
        if (stdout) {
            counter++;
        }

        // now we walk the blocks of the 'Soot CFG' to construct our own CFG.
        final CFG cfg = walkCFG(sysoLocal, listLocal, counter, stdout, exceptionsCFG);
        this.sMethod.setActiveBody(sBody);

        return cfg;
    }

    /**
     * Instruments the CFG blocks.
     * 
     * @param sysoLocal
     *            the local variable for (System.out.println).
     * @param listLocal
     *            the local variable for (java.util.List).
     * @param intLocal
     *            the local variable for (java.lang.Integer).
     * @param nodes
     *            list of nodes of the CFG.
     * @param stdout
     *            <code>true</code> indicates that the nodes of the CFG should be instrumented with
     *            (System.out.println).
     */
    private void instrumentCFGBlocks(final Local sysoLocal, final Local listLocal, final Local intLocal, final List<CFGNode> nodes,
        final List<CFGNode> heads, final boolean stdout) {
        // gets the body (bytecode) of the method.
        final Body sBody = this.sMethod.retrieveActiveBody();

        final SootMethod stdCall = Scene.v().getMethod(InstrumenterHelper.JAVA_IO_PRINT_STREAM_PRINT);
        final SootMethod addCall = Scene.v().getMethod(InstrumenterHelper.JAVA_UTIL_LIST_ADD);
        final SootMethod intCall = Scene.v().getMethod(InstrumenterHelper.JAVA_LANG_INTEGER_VALUEOF);

        Stmt stmt;
        for (final CFGNode node : nodes) {
            // to call the 'java.util.List.add(int)' method we need first to put the integer inside a local variable
            // then put the local inside the List.
            stmt = Jimple.v().newAssignStmt(intLocal, Jimple.v().newStaticInvokeExpr(intCall.makeRef(), IntConstant.v(node.getIndex())));
            sBody.getUnits().insertBefore(stmt, node.getStmt());
            // now put the local (with the integer) inside the List.
            stmt = Jimple.v().newInvokeStmt(Jimple.v().newInterfaceInvokeExpr(listLocal, addCall.makeRef(), intLocal));
            sBody.getUnits().insertBefore(stmt, node.getStmt());
            if (stdout) {
                stmt = Jimple.v().newInvokeStmt(
                                                Jimple.v().newVirtualInvokeExpr(
                                                                                sysoLocal,
                                                                                stdCall.makeRef(),
                                                                                StringConstant.v(String
                                                                                    .valueOf("[" + node.getIndex() + "]"))));
                sBody.getUnits().insertBefore(stmt, node.getStmt());
            }
        }
    }

    /**
     * Prepare the initial statements of the method creating the local variables in right place.
     * 
     * @param sysoLocal
     *            the local variable for (System.out.println).
     * @param listLocal
     *            the local variable for (java.util.List).
     * @return a counter indicating the number of statements needed to jump before inserting the instrumentation for the
     *         first block of the method.
     */
    private int prepareLocals(final Local sysoLocal, final Local listLocal) {
        // gets the body (bytecode) of the method.
        final Body sBody = this.sMethod.retrieveActiveBody();
        // get a kind of 'linked list' bytecode of the body.
        final PatchingChain<Unit> units = sBody.getUnits();

        // stack where we are going to store all the 'special' bytecode lines from the source. 'special' means that
        // lines that are flexible depending on the method. This is explained in the comment just above.
        final Deque<Unit> stack = new LinkedList<Unit>();

        // if the method is not static we need to store a (soot.jimple.internal.IdentityRefBox) and later we put it
        // back. i.e. of bytecode: (r0 := @this: pck1.pck2.ClassName);
        Unit identityRefBox = null;
        if (!this.sMethod.isStatic()) {
            identityRefBox = units.getFirst();
            units.removeFirst();
        }

        // list where we will store the statement that we will remove later from the PatchingChain<Unit>. We cannot
        // remove then right inside of the 'for loop' or we will have an exception.
        final List<Unit> unitParameters = new ArrayList<Unit>();
        // this 'for loop' look for 'special statements'.
        for (final Unit unit : units) {
            if (unit instanceof JIdentityStmt) {
                final JIdentityStmt stmt = (JIdentityStmt) unit;
                if (stmt.getRightOp() != null && stmt.getRightOp().toString().contains("parameter")) {
                    // it is a 'special statement'!
                    unitParameters.add(stmt);
                    // put in the top of the stack.
                    stack.add(stmt);
                }
            }
        }
        // now we can remove all 'special statements' from the PatchingChain<Unit>.
        for (final Unit unitParameter : unitParameters) {
            units.remove(unitParameter);
        }

        // done storing 'special statements'. Now create statements to instantiate our local variables.
        int specialStmtCounter = 0;

        // creates a instance for (System.out) local variable.
        final Stmt stmtSysoInstance = Jimple.v()
            .newAssignStmt(sysoLocal,
                           Jimple.v().newStaticFieldRef(Scene.v().getField(InstrumenterHelper.JAVA_IO_PRINT_STREAM_OUT).makeRef()));
        units.addFirst(stmtSysoInstance);
        specialStmtCounter++;

        // gets the static List field.
        final SootField field = this.iClass.getInternalClass().getField(this.iClass.getUniqueName(),
                                                                        RefType.v(InstrumenterHelper.JAVA_UTIL_LIST));

        // assings the static field to the local variable.
        final Stmt stmtListInstance = Jimple.v().newAssignStmt(listLocal, Jimple.v().newStaticFieldRef(field.makeRef()));
        units.addFirst(stmtListInstance);
        specialStmtCounter++;

        // now we put back the 'special statements'.
        while (!stack.isEmpty()) {
            units.addFirst(stack.removeFirst());
            specialStmtCounter++;
        }

        // and we put back the 'special statement' for a method that is not static.
        if (!this.sMethod.isStatic() && identityRefBox != null) {
            units.addFirst(identityRefBox);
            specialStmtCounter++;
        }

        return specialStmtCounter;
    }

    /**
     * Recursive method for stepping in each block of the CFG.
     * 
     * @param currentBlock
     *            current CFG block.
     * @param nodes
     *            list of CFG nodes already discovered.
     * @param indexes
     *            indexes already visited of the CFG.
     * @param edges
     *            list of edges already discovered.
     */
    private void step(final Block currentBlock, final List<CFGNode> nodes, final List<Integer> indexes, final List<CFGEdge> edges) {
        // if we hit the end of the CFG or already visited it (it is inside the indexes list) then return.
        if (currentBlock == null || indexes.contains(currentBlock.getIndexInMethod())) {
            return;
        }
        // stores the index of this block so we never visit it again.
        indexes.add(currentBlock.getIndexInMethod());
        // get the first statement.
        final Stmt stmt = (Stmt) currentBlock.getHead();
        // and creates a new node.
        nodes.add(new CFGNode(currentBlock.getIndexInMethod(), stmt));
        // for each successor of this block, visit...
        final List<Block> sucs = currentBlock.getSuccs();
        for (final Block suc : sucs) {
            // but first stores the edge.
            edges.add(new CFGEdge(currentBlock.getIndexInMethod(), suc.getIndexInMethod()));
            // recursive call.
            step(suc, nodes, indexes, edges);
        }
    }

    private CFG walkCFG(final Local sysoLocal, final Local listLocal, final int counter, final boolean stdout, final boolean exceptionsCFG) {
        // gets the body (bytecode) of the method.
        final Body sBody = this.sMethod.retrieveActiveBody();

        // create a local variable to hold the index of the current CFG block. This index (integer) is placed inside a
        // local variable before adds it to the List.
        final Local intLocal = Jimple.v().newLocal(InstrumentedClass.INT_LOCAL, RefType.v(InstrumenterHelper.JAVA_LANG_INTEGER));
        sBody.getLocals().add(intLocal);

        final List<CFGNode> nodes = new ArrayList<CFGNode>();
        final List<CFGEdge> edges = new ArrayList<CFGEdge>();
        final List<Integer> indexes = new ArrayList<Integer>();
        final List<CFGNode> cfgHeads = new ArrayList<CFGNode>();

        List<Block> heads = null;
        if (exceptionsCFG) {
            // complete CFG, includes try/catch blocks.
            @SuppressWarnings("unchecked")
            final DirectedGraph<Block> dg = CFGGraphType.COMPLETE_BLOCK_GRAPH.buildGraph(sBody);
            heads = dg.getHeads();
        } else {
            // do not considered try/catch blocks.
            @SuppressWarnings("unchecked")
            final DirectedGraph<Block> dg = CFGGraphType.BRIEF_BLOCK_GRAPH.buildGraph(sBody);
            heads = dg.getHeads();
        }

        for (int c = 0; c < heads.size(); c++) {
            // current head block.
            final Block currentHead = heads.get(c);
            Unit unit = currentHead.getHead();

            if (c == 0) {
                // we will instrument the first block but it contains that 'special statements' that we removed and put
                // back. We cannot insert the instrument statement in the first position or we will get an exception. We
                // need to move down after the last 'special statement'. Where we do that using the counter.
                for (int i = 0; i < counter - 1; i++) {
                    unit = currentHead.getSuccOf(unit);
                }
            }

            // creates the first node of our CFG.
            final CFGNode head = new CFGNode(currentHead.getIndexInMethod(), (Stmt) unit);
            nodes.add(head);
            cfgHeads.add(head);
            indexes.add(currentHead.getIndexInMethod());

            // now we do a recursive step inside each block (node) of the CFG so we can create the edges/nodes.
            final List<Block> sucs = currentHead.getSuccs();
            for (final Block suc : sucs) {
                edges.add(new CFGEdge(currentHead.getIndexInMethod(), suc.getIndexInMethod()));
                step(suc, nodes, indexes, edges);
            }

        }

        final CFG cfg = new CFG(nodes, edges, cfgHeads);

        // we have our CFG, now instruments each node.
        instrumentCFGBlocks(sysoLocal, listLocal, intLocal, nodes, cfgHeads, stdout);

        // returns our instrumented CFG
        return cfg;
    }

    /**
     * Gets a new fresh instance of {@link InstrumentedMethod}.
     * 
     * @param iClass
     *            instrumented class of this method.
     * @param sMethod
     *            {@link SootMethod} necessary for the instrumentation.
     * 
     * @return fresh instance of {@link InstrumentedClass}.
     */
    public static InstrumentedMethod newInstance(final InstrumentedClass iClass, final SootMethod sMethod) {
        return new InstrumentedMethod(iClass, sMethod);
    }
}
