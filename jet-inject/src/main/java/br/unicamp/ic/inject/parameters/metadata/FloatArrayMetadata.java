package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.FloatArrayParameter;

public class FloatArrayMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(FloatArrayParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(FloatArrayParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(FloatArrayParameter.class);
    }

}
