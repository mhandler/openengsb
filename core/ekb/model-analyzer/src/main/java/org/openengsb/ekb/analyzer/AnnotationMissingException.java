package org.openengsb.ekb.analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationMissingException extends Exception {

    public AnnotationMissingException(Class<?> annotatedClass, Class<? extends Annotation> annotation) {
        super("Annotation " + annotation + " is missing on class " + annotatedClass);
    }

    public AnnotationMissingException(Field annotatedField, Class<? extends Annotation> annotation) {
        super("Annotation " + annotation + " is missing on field " + annotatedField);
    }

}
