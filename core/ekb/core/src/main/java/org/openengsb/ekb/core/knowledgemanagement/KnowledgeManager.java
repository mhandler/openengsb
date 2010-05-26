package org.openengsb.ekb.core.knowledgemanagement;

import java.util.List;

import org.openengsb.ekb.api.Concept;

public interface KnowledgeManager {

    public List<Concept<?>> getActiveConcepts();

    public List<Concept<?>> getActiveConcepts(String domain);

    public List<Concept<?>> getInactiveConcepts();

    public List<Concept<?>> getInactiveConcepts(String domain);

    public void storeConcepts(List<Concept<?>> concepts);

    public void deactivateConcepts(List<Concept<?>> concept);

    public void activateConcepts(List<Concept<?>> concept);

}
