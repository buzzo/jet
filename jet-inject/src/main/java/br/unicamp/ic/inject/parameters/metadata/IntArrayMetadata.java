package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.IntArrayParameter;

public class IntArrayMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(IntArrayParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(IntArrayParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(IntArrayParameter.class);
    }

}
