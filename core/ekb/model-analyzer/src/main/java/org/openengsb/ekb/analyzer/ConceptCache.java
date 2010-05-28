package org.openengsb.ekb.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.openengsb.ekb.core.ConceptImpl;

public class ConceptCache {

    private Map<String, ConceptImpl<?>> concepts = new HashMap<String, ConceptImpl<?>>();

    public ConceptImpl<?> getConcept(String id) {
        return concepts.get(id);
    }

    public void storeConcept(ConceptImpl<?> concept) {
        this.concepts.put(concept.getId(), concept);
    }

    public void clearCache() {
        this.concepts.clear();
    }

}
