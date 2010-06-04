/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ekb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.core.conceptsourcemanagement.ConceptSourceManager;
import org.openengsb.ekb.core.conceptsourcemanagement.ConceptSourceStatusListener;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeChangeListener;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeManager;

public class ConsistencyControl implements KnowledgeChangeListener, ConceptSourceStatusListener {

    private KnowledgeManager knowledgeManager;

    private ConceptSourceManager conceptSourceManager;

    public ConsistencyControl(KnowledgeManager knowledgeManager, ConceptSourceManager conceptSourceManager) {
        this.knowledgeManager = knowledgeManager;
        this.conceptSourceManager = conceptSourceManager;
    }

    @Override
    public void conceptsChanged() {
        List<Concept<?>> inactiveConcepts = knowledgeManager.getInactiveConcepts();
        List<Concept<?>> activeConcepts = knowledgeManager.getActiveConcepts();
        List<Concept<?>> allConcepts = new ArrayList<Concept<?>>(activeConcepts.size() + inactiveConcepts.size());
        allConcepts.addAll(inactiveConcepts);
        allConcepts.addAll(activeConcepts);
        checkConsistency(allConcepts, conceptSourceManager.getActiveConceptSources());
    }

    private void checkConsistency(List<Concept<?>> concepts, List<ConceptSource> sources) {
        List<Inconsistency> inconsistencies = getInconsistencies(concepts, sources);
        if (inconsistencies.isEmpty()) {
            return;
        }
        for (Inconsistency inconsistency : inconsistencies) {
            conceptSourceManager.deactivate(inconsistency.getSource());
        }
    }

    @Override
    public void activated(ConceptSource source) {
        List<Concept<?>> candidates = knowledgeManager.getInactiveConcepts(source);
        List<Inconsistency> inconsistencies = getInconsistencies(candidates, Collections.singletonList(source));
        for (Inconsistency inconsistency : inconsistencies) {
            candidates.remove(inconsistency.getModelConcept());
        }
        knowledgeManager.activateConcepts(candidates);
    }

    private List<Inconsistency> getInconsistencies(List<Concept<?>> concepts, List<ConceptSource> sources) {
        List<Inconsistency> inconsistencies = new ArrayList<Inconsistency>();
        for (ConceptSource source : sources) {
            for (Concept<?> concept : concepts) {
                for (ConceptKey key : source.getProvidedConcepts()) {
                    if (concept.getKey().getId().equals(key.getId())
                            && !concept.getKey().getVersion().equals(key.getVersion())) {
                        inconsistencies.add(new Inconsistency(source, concept));
                    }
                }
            }
        }
        return inconsistencies;
    }

    @Override
    public void deactivated(ConceptSource source) {
        List<Concept<?>> candidates = knowledgeManager.getActiveConcepts(source);
        for (ConceptSource otherSource : conceptSourceManager.getActiveConceptSources()) {
            candidates.removeAll(knowledgeManager.getActiveConcepts(otherSource));
        }
        knowledgeManager.deactivateConcepts(candidates);
    }

    @Override
    public void conceptsActivated() {
        // do nothing
    }

    @Override
    public void conceptsDeactivated() {
        // do nothing
    }

    public void setConceptSourceManager(ConceptSourceManager conceptSourceManager) {
        if (this.conceptSourceManager != null) {
            this.conceptSourceManager.removeListener(this);
        }
        this.conceptSourceManager = conceptSourceManager;
        if (conceptSourceManager != null) {
            conceptSourceManager.addListener(this);
        }
    }

    public void setKnowledgeManager(KnowledgeManager knowledgeManager) {
        this.knowledgeManager = knowledgeManager;
    }

    private class Inconsistency {
        private Concept<?> modelConcept;

        private ConceptSource source;

        public Inconsistency(ConceptSource source, Concept<?> modelConcept) {
            this.source = source;
            this.modelConcept = modelConcept;
        }

        public Concept<?> getModelConcept() {
            return modelConcept;
        }

        public ConceptSource getSource() {
            return source;
        }

    }

}
