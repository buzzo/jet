package br.unicamp.ic.inject.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ByteArrayParameter {
    /**
     * starts at 1
     */
    int index();

    byte[] value();

}
