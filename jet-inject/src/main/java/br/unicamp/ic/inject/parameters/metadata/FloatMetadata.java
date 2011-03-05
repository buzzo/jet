package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.FloatParameter;

public class FloatMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(FloatParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(FloatParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(FloatParameter.class);
    }

}
