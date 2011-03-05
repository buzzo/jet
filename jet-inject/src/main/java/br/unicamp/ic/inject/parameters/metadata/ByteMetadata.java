package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.ByteParameter;

public class ByteMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(ByteParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(ByteParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(ByteParameter.class);
    }

}
