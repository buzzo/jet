package br.unicamp.ic.inject.parameters.metadata;

import java.lang.reflect.Method;

import br.unicamp.ic.inject.parameters.StringArrayParameter;

public class StringArrayMetadata implements MetadataParameter {

    @Override
    public int getPosition(Method method) {
        return method.getAnnotation(StringArrayParameter.class).index();
    }

    @Override
    public Object getValue(Method method) {
        return method.getAnnotation(StringArrayParameter.class).value();
    }

    @Override
    public boolean isAnnotationPresent(Method method) {
        return method.isAnnotationPresent(StringArrayParameter.class);
    }

}
