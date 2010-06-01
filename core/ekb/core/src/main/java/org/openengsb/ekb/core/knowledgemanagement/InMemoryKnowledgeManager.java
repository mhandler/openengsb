package org.openengsb.ekb.core.knowledgemanagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.core.conceptsourcemanagement.ConceptSourceStatusListener;

public class InMemoryKnowledgeManager implements KnowledgeManager, ConceptSourceStatusListener {

    private Set<Concept<?>> activeConcepts = new HashSet<Concept<?>>();

    private Set<Concept<?>> inactiveConcepts = new HashSet<Concept<?>>();

    @Override
    public void activateConcepts(List<Concept<?>> concepts) {
        inactiveConcepts.removeAll(concepts);
        activeConcepts.addAll(concepts);
    }

    @Override
    public void deactivateConcepts(List<Concept<?>> concepts) {
        activeConcepts.removeAll(concepts);
        inactiveConcepts.addAll(concepts);
    }

    @Override
    public List<Concept<?>> getActiveConcepts() {
        return new ArrayList<Concept<?>>(activeConcepts);
    }

    @Override
    public List<Concept<?>> getActiveConcepts(ConceptSource source) {
        return removeNotProvidedConcepts(source, getActiveConcepts());
    }

    @Override
    public Concept<?> getActiveConcept(String id) throws NoSuchConceptException {
        for (Concept<?> concept : getActiveConcepts()) {
            if (concept.getId().equals(id)) {
                return concept;
            }
        }
        throw new NoSuchConceptException("No concept stored with id " + id);
    }

    @Override
    public List<Concept<?>> getInactiveConcepts() {
        return new ArrayList<Concept<?>>(inactiveConcepts);
    }

    @Override
    public List<Concept<?>> getInactiveConcepts(ConceptSource source) {
        return removeNotProvidedConcepts(source, getInactiveConcepts());
    }

    @Override
    public void setConcepts(List<Concept<?>> concepts) {
        activeConcepts.clear();
        inactiveConcepts.clear();
        inactiveConcepts.addAll(concepts);
    }

    @Override
    public void activated(ConceptSource source) {
        activateConcepts(getInactiveConcepts(source));
    }

    @Override
    public void deactivated(ConceptSource source) {
        deactivateConcepts(getActiveConcepts(source));
    }

    private List<Concept<?>> removeNotProvidedConcepts(ConceptSource source, List<Concept<?>> concepts) {
        ListIterator<Concept<?>> listIterator = concepts.listIterator();
        while (listIterator.hasNext()) {
            Concept<?> concept = listIterator.next();
            if (!source.canProvide(concept) && !source.canProvideSubconcept(concept)) {
                listIterator.remove();
            }
        }
        return concepts;
    }

}
