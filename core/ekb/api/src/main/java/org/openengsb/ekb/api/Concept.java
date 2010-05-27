package org.openengsb.ekb.api;

import java.util.List;

public interface Concept<T> {

    String getId();

    Class<T> getConceptClass();

    String getModelPartId();

    List<SoftReference<T, ?>> getSoftReferences();

    <U> List<SoftReference<T, U>> getSoftReferences(Concept<U> targetConcept);

    List<Concept<?>> getSubConcepts();

    Concept<?> getSuperConcept();

    @Override
    String toString();

}
