package org.openengsb.ekb;

import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;

public class EKBImplementation implements EKB {

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
    public <T> List<ConceptSource> getSources(Concept<T> concept) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<Concept<?>> getSubConcepts(Concept<T> superConcept) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<Concept<?>> getSoftReferences(Concept<T> concept) {
        // TODO Auto-generated method stub
        return null;
    }
}
