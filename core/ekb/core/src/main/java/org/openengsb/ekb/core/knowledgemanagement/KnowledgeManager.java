package org.openengsb.ekb.core.knowledgemanagement;

import java.util.List;

import org.openengsb.ekb.api.Concept;

public interface KnowledgeManager {

    public List<Concept<?>> getActiveConcepts();

    public Concept<?> getConcept(String id);

    public List<Concept<?>> getActiveConcepts(String modelPartId);

    public List<Concept<?>> getInactiveConcepts();

    public List<Concept<?>> getInactiveConcepts(String modelPartId);

    public void storeConcepts(List<Concept<?>> concepts);

    public void deactivateConcepts(List<Concept<?>> concept);

    public void activateConcepts(List<Concept<?>> concept);

}
