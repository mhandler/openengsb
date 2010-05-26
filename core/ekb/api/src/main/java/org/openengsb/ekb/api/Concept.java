package org.openengsb.ekb.api;

import java.util.List;

public interface Concept<T> {

    public String getId();

    public Class<T> getConceptClass();

    public String getModelPartId();

    public List<Concept<?>> getSoftReferences();

    public List<Concept<?>> getSubConcepts();

    public Concept<?> getSuperConcept();

    @Override
    public String toString();

}
