package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.DoubleParameter;

public class DoubleMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(DoubleParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(DoubleParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(DoubleParameter.class);
    }

}
