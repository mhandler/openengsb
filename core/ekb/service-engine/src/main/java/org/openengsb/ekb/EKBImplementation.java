package org.openengsb.ekb;

import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;

public class EKBImplementation implements EKB {

    @Override
    public List<Concept<?>> getAllConcepts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Concept<?> getConcept(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Concept<T> getConcept(String id, Class<T> conceptClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<T> getData(ConceptSource source, Concept<T> concept) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Concept<?>> getSoftReferences(Concept<?> concept) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ConceptSource> getSources(Concept<?> concept) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Concept<?>> getSubConcepts(Concept<?> superConcept) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T, U> List<T> followSoftReference(Concept<U> sourceConcept, U source, Concept<T> targetConcept) {
        // TODO Auto-generated method stub
        return null;
    }

}
