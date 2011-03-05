package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.DoubleArrayParameter;

public class DoubleArrayMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(DoubleArrayParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(DoubleArrayParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(DoubleArrayParameter.class);
    }

}
