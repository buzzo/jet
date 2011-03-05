package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.StringParameter;

public class StringMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(StringParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(StringParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(StringParameter.class);
    }

}
