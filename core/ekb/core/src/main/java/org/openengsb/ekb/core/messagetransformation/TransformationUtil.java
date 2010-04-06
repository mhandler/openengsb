package org.openengsb.ekb.core.messagetransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.openengsb.core.model.ConceptIRI;
import org.semanticweb.owlapi.model.IRI;

public class TransformationUtil {

    public static Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> noArgConstructor = clazz.getDeclaredConstructor();
            boolean accessible = noArgConstructor.isAccessible();
            noArgConstructor.setAccessible(true);
            Object o = noArgConstructor.newInstance();
            noArgConstructor.setAccessible(accessible);
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static IRI extractConceptIRI(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        return extractConceptIRI(clazz);
    }

    public static IRI extractConceptIRI(Class<?> clazz) {
        ConceptIRI iriAnnotation = clazz.getAnnotation(ConceptIRI.class);
        if (iriAnnotation == null) {
            throw new IllegalStateException("Missing ConceptIRI annotation on object of type: " + clazz);
        }
        String iri = iriAnnotation.value();
        return IRI.create(iri);
    }

    public static IRI extractConceptIRI(Field field) {
        ConceptIRI iriAnnotation = field.getAnnotation(ConceptIRI.class);
        if (iriAnnotation == null) {
            throw new IllegalStateException("Missing ConceptIRI annotation on field of type: " + field);
        }
        String iri = iriAnnotation.value();
        return IRI.create(iri);
    }
}
