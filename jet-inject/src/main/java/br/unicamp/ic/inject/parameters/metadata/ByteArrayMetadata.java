package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.ByteArrayParameter;

public class ByteArrayMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(ByteArrayParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(ByteArrayParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(ByteArrayParameter.class);
    }

}
