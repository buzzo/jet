package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.IntParameter;

public class IntMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(IntParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(IntParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(IntParameter.class);
    }

}
