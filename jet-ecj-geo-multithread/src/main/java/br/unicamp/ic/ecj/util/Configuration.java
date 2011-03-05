package br.unicamp.ic.ecj.util;

import java.io.File;

public class Configuration {

    private final File       local;
    private final String     className;
    private final String     methodName;
    private final Class<?>[] parameters;
    private final Class<?>   returnType;
    private final float      tau;
    private final int        max;
    private final int        min;
    private final int        generations;

    public Configuration(final float tau, final int max, final int min, final int generations, final File local, final String className,
        final String methodName, final Class<?>[] parameters, final Class<?> returnType) {
        this.tau = tau;
        this.max = max;
        this.min = min;
        this.generations = generations;
        this.local = local;
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public String getClassName() {
        return this.className;
    }

    public int getGenerations() {
        return this.generations;
    }

    public File getLocal() {
        return this.local;
    }

    public int getMax() {
        return this.max;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public int getMin() {
        return this.min;
    }

    public Class<?>[] getParameters() {
        return this.parameters;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public float getTau() {
        return this.tau;
    }

}
