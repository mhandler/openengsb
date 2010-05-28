package org.openengsb.ekb.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.core.ConceptImpl;

public class ConceptCache {

    private Map<String, ConceptImpl<?>> concepts = new HashMap<String, ConceptImpl<?>>();

    private Set<ConceptCacheListener> listeners = new HashSet<ConceptCacheListener>();

    public ConceptImpl<?> getConcept(String id) {
        return concepts.get(id);
    }

    public void storeConcept(ConceptImpl<?> concept) {
        this.concepts.put(concept.getId(), concept);
        notifyAboutStore(concept);
    }

    public void clearCache() {
        this.concepts.clear();
    }

    public void addListener(ConceptCacheListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ConceptCacheListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyAboutStore(Concept<?> concept) {
        for (ConceptCacheListener listener : listeners) {
            listener.conceptStored(concept);
        }
    }

}
