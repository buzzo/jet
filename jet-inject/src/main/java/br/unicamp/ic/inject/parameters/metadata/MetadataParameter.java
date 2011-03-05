package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

public interface MetadataParameter {

    boolean isAnnotationPresent(final Method method);
    
    int getPosition(final Method method);
    
    Object getValue(final Method method);
}
