package br.unicamp.ic.instrumenter.reflect;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.internal.JNewExpr;
import br.unicamp.ic.instrumenter.InstrumenterHelper;

/**
 * Class that represents the instrumented bytecode of a class.<br>
 * <br>
 * This class is not thread safe!
 */
public final class InstrumentedClass {
    public static final String LIST_LOCAL = "listRef";
    public static final String INT_LOCAL  = "intRef";
    public static final String SYSO_LOCAL = "sysLocal";

    private final SootClass    sClass;
    private final String       uniqueName;
    private final URL[]        urls;

    /**
     * Instantiates the {@link InstrumentedClass} and do the initial instrumentation.
     * 
     * @param firstTime
     *            <code>true</code> indicates that the static field and methods should be created. <code>false</code>
     *            indicates that this instrumentation should be created.
     * @param baseURLClasspath
     *            paths where are the classes to be instrumented.
     */
    private InstrumentedClass(final SootClass sClass, final URL[] baseClasspath) {
        // the base of the instrumentation is the SootClass.
        this.sClass = sClass;
        // settles the unique name for the static field that will be added. This is used too for the methods that will
        // be added.
        this.uniqueName = checkForUniqueName();
        // we stores the urls where are the original classes.
        this.urls = baseClasspath;
        // creates a static List field inside the class where we will later
        // initiate with a instance of ArrayList.
        createStaticField(this.sClass);
        // creates a static method to initiate the static List field. It only instantiate an ArrayList and assigns to
        // the static field.
        createInitMethod(this.sClass);
        // creates a static method to get the static List field. This is not necessarily needed since you can access the
        // static field directly thought the class.
        createGetMethod(this.sClass);
    }

    /**
     * Gets a instrumented instance of the class.
     * 
     * @return a instrumented instance of the class.
     */
    public InstrumentedInstance getInstrumentedInstance() {
        return InstrumentedInstance.newInstance(this);
    }

    /**
     * Gets the instrumented method by its name. The method has only the initial instrumentation and does not have the
     * CFG calculation/instrumentation yet.
     * 
     * @param name
     *            name of the method.
     * @return the instrumented method.
     * @throws RuntimeException
     *             if there is an ambiguous method inside the class.
     */
    public InstrumentedMethod getInstrumentedMethod(final String name) {
        // gets the method to instrument by the name.
        final SootMethod sMethod = this.sClass.getMethodByName(name);
        // returns the new instrumented method.
        return InstrumentedMethod.newInstance(this, sMethod);
    }

    /**
     * Gets the instrumented method by its name and parameters. The method has only the initial instrumentation and does
     * not have the CFG calculation/instrumentation yet.
     * 
     * @param name
     *            name of the method.
     * @param parametersType
     *            list of parameters type.
     * @return the instrumented method.
     * @throws RuntimeException
     *             if there is an ambiguous method inside the class.
     * @throws IllegalArgumentException
     *             is the list of parameters is empty or <code>null</code>.
     */
    public InstrumentedMethod getInstrumentedMethod(final String name, final Class<?>[] parametersType) {
        Validate.notNull(parametersType, "List of parameters cannot be null.");
        Validate.notEmpty(parametersType, "List of parameters cannot be empty.");
        // gets the method to instrument by the name.
        final SootMethod sMethod = this.sClass.getMethod(name, convertToSootType(parametersType));
        // returns the new instrumented method.
        return InstrumentedMethod.newInstance(this, sMethod);
    }

    /**
     * Gets the instrumented method by its name and parameters. The method has only the initial instrumentation and does
     * not have the CFG calculation/instrumentation yet.
     * 
     * @param name
     *            name of the method.
     * @param parametersType
     *            list of parameters type.
     * @return the instrumented method.
     * @throws RuntimeException
     *             if there is an ambiguous method inside the class.
     * @throws IllegalArgumentException
     *             is the list of parameters is empty or <code>null</code>. Same for the return type.
     */
    public InstrumentedMethod getInstrumentedMethod(final String name, final Class<?>[] parametersType, final Class<?> returnType) {
        Validate.notNull(parametersType, "List of parameters cannot be null.");
        Validate.notEmpty(parametersType, "List of parameters cannot be empty.");
        // gets the method to instrument by the name.
        final SootMethod sMethod = this.sClass.getMethod(name, convertToSootType(parametersType), convertToSootType(returnType));
        // returns the new instrumented method.
        return InstrumentedMethod.newInstance(this, sMethod);
    }

    /**
     * Gets the path to original classes not instrumented.
     * 
     * @return the path to original classes not instrumented.
     */
    protected URL[] getBaseClasspath() {
        return this.urls;
    }

    /**
     * Gets the internal representation class {@link SootClass}.
     * 
     * @return the internal representation class {@link SootClass}.
     */
    protected SootClass getInternalClass() {
        return this.sClass;
    }

    /**
     * Gets the unique name for the static field and static methods of this instrumented class.
     * 
     * @return unique name for the static field and static methods of this instrumented class.
     */
    protected String getUniqueName() {
        return this.uniqueName;
    }

    /**
     * Look for a unique name for the field and methods that will be added to the instrumented class.
     */
    private String checkForUniqueName() {
        // TODO: implementar a busca por nome unico dentro da classe. Precisa verificar se ja não existe um campo
        // chamado 'list' e metodos 'initList' e 'getList'. Se houver gerar um outro nome randomico como 'list432'. A
        // partir dai checar novamente se existe o campo 'list432' e os metodos 'initList432' e 'getList432'.
        return "list";
    }

    /**
     * Makes the convertion between java.lang class type to Soot {@link Type}.
     * 
     * @param type
     *            java.lang class type (i.e.: {@link Integer}, {@link Double}, {@link String}, etc).
     * @return the equivalent type in Soot {@link Type}.
     * @throws IllegalArgumentException
     *             if the converstion is not implemented yet.
     */
    private Type convertToSootType(final Class<?> type) {
        if (Integer.class.equals(type)) {
            return IntType.v();
        } else if (int.class.equals(type)) {
            return IntType.v();
        } else if (Long.class.equals(type)) {
            return LongType.v();
        } else if (long.class.equals(type)) {
            return LongType.v();
        } else if (Double.class.equals(type)) {
            return DoubleType.v();
        } else if (double.class.equals(type)) {
            return DoubleType.v();
        } else if (float.class.equals(type)) {
            return FloatType.v();
        } else if (Float.class.equals(type)) {
            return FloatType.v();
        } else if (String.class.equals(type)) {
            return RefType.v("java.lang.String");
        } else if (Boolean.class.equals(type)) {
            return BooleanType.v();
        } else if (boolean.class.equals(type)) {
            return BooleanType.v();
        } else if (int[].class.equals(type)) {
            return ArrayType.v(IntType.v(), 1);
        } else if (int[][].class.equals(type)) {
            return ArrayType.v(IntType.v(), 2);
        } else if (String[].class.equals(type)) {
            return ArrayType.v(RefType.v("java.lang.String"), 1);
        } else if (double[].class.equals(type)) {
            return ArrayType.v(DoubleType.v(), 1);
        } else if (float[].class.equals(type)) {
            return ArrayType.v(FloatType.v(), 1);
        } else {
            throw new IllegalArgumentException("Unimplemented conversion from java.lang class [" + type + "] to Soot Type.");
        }
    }

    /**
     * Makes the convertion between an array of java.lang class type to Soot {@link Type}.
     * 
     * @param type
     *            java.lang class type (i.e.: {@link Integer}, {@link Double}, {@link String}, etc).
     * @return the equivalent array of types in Soot {@link Type}.
     * @throws IllegalArgumentException
     *             if the converstion is not implemented yet.
     */
    private List<Type> convertToSootType(final Class<?>[] types) {
        final List<Type> ret = new ArrayList<Type>();
        for (final Class<?> type : types) {
            ret.add(convertToSootType(type));
        }
        return ret;
    }

    /**
     * Creates a static method to get the static 'java.util.List' field.
     * 
     * @param sClass
     *            {@link SootClass} representation of this class.
     */
    private void createGetMethod(final SootClass sClass) {
        // creates the static method that get the static field 'java.util.List'.
        final SootMethod getMethod = new SootMethod("get" + StringUtils.capitalize(this.uniqueName), new ArrayList<Object>(), RefType
            .v(InstrumenterHelper.JAVA_UTIL_LIST), Modifier.PUBLIC | Modifier.STATIC);
        // adds it to our class.
        sClass.addMethod(getMethod);
        // now builds the method body (bytecode).
        final Body body = Jimple.v().newBody(getMethod);
        // static List field.
        final SootField list = sClass.getFieldByName(this.uniqueName);
        // creates a local variable.
        final Local listLocal = Jimple.v().newLocal(InstrumentedClass.LIST_LOCAL, RefType.v(InstrumenterHelper.JAVA_UTIL_LIST));
        body.getLocals().add(listLocal);
        // create statements to return the static field.
        final PatchingChain<Unit> units = body.getUnits();
        final Stmt stmtAssign = Jimple.v().newAssignStmt(listLocal, Jimple.v().newStaticFieldRef(list.makeRef()));
        units.add(stmtAssign);
        // returns the local variable that contains the static field.
        units.add(Jimple.v().newReturnStmt(listLocal));
        getMethod.setActiveBody(body);
    }

    /**
     * Creates a static method to fill the 'java.util.List' static field with a instance of 'java.util.ArrayList'.
     * 
     * @param sClass
     *            {@link SootClass} representation of this class.
     */
    private void createInitMethod(final SootClass sClass) {
        // creates the static method that initializes the static field 'java.util.List'.
        final SootMethod initMethod = new SootMethod("init" + StringUtils.capitalize(this.uniqueName), new ArrayList<Object>(), VoidType
            .v(), Modifier.PUBLIC | Modifier.STATIC);
        // adds it to our class.
        sClass.addMethod(initMethod);
        // now builds the method body (bytecode).
        final Body body = Jimple.v().newBody(initMethod);
        // method call to instantiate an ArrayList.
        final SootMethod addCall = Scene.v().getMethod(InstrumenterHelper.JAVA_UTIL_LIST_VOID_INIT);
        // static List field.
        final SootField list = sClass.getFieldByName(this.uniqueName);
        // creates a local variable.
        final Local listLocal = Jimple.v().newLocal(InstrumentedClass.LIST_LOCAL, RefType.v(InstrumenterHelper.JAVA_UTIL_ARRAY_LIST));
        body.getLocals().add(listLocal);
        // add statements to instantiate an ArrayList to the local static List.
        final PatchingChain<Unit> units = body.getUnits();
        final Stmt stmtAssign = Jimple.v().newAssignStmt(listLocal, new JNewExpr(RefType.v(InstrumenterHelper.JAVA_UTIL_ARRAY_LIST)));
        units.add(stmtAssign);
        final Stmt stmtInvoke = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(listLocal, addCall.makeRef(), IntConstant.v(0)));
        units.add(stmtInvoke);
        final Stmt stmtAssignStatic = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(list.makeRef()), listLocal);
        units.add(stmtAssignStatic);
        // return statement.
        units.add(Jimple.v().newReturnVoidStmt());
        initMethod.setActiveBody(body);
    }

    /**
     * Creates a static {@link List} field in the {@link SootClass}.
     * 
     * @param sClass
     *            {@link SootClass} that will have a new {@link List} static field.
     */
    private void createStaticField(final SootClass sClass) {
        // creates a static field with type 'java.util.List'.
        final SootField field = new SootField(this.uniqueName, RefType.v(InstrumenterHelper.JAVA_UTIL_LIST), Modifier.STATIC);
        // adds it to our class.
        sClass.addField(field);
    }

    /**
     * Gets a new fresh instance of {@link InstrumentedClass}.
     * 
     * @param sClass
     *            {@link SootClass} necessary for our class instrumentation.
     * @param baseURLClasspath
     *            paths where are the classes to be instrumented.
     * @return fresh instance of {@link InstrumentedClass}.
     */
    public static InstrumentedClass newInstance(final SootClass sClass, final URL[] baseURLClasspath) {
        return new InstrumentedClass(sClass, baseURLClasspath);
    }

}
