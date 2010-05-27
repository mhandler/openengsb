package org.openengsb.ekb.api;

public interface ReferenceableConcept<T> extends Concept<T> {

    String getReferenceRegex();

    String extractKey(String reference);

}
