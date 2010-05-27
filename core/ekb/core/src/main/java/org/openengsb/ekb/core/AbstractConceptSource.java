package org.openengsb.ekb.core;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;

public abstract class AbstractConceptSource implements ConceptSource {

    @Override
    public boolean canProvide(Concept<?> concept) {
        if (getId().equals(concept.getModelPartId())) {
            return true;
        }
        for (Concept<?> subConcept : concept.getSubConcepts()) {
            if (canProvide(subConcept)) {
                return true;
            }
        }
        return false;
    }
}
