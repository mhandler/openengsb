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
import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.conceptSource.ConceptSource;
import org.openengsb.ekb.api.conceptSource.ConceptSourceManager;
import org.openengsb.ekb.api.conceptSource.ConceptSourceStatusListener;
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
        activateSupportedConcepts(inactiveConcepts, conceptSourceManager.getActiveConceptSources());
    }

    private void activateSupportedConcepts(List<Concept<?>> inactiveConcepts, List<ConceptSource> activeConceptSources) {
        List<Concept<?>> toActivate = new ArrayList<Concept<?>>();
        for (ConceptSource source : activeConceptSources) {
            for (Concept<?> concept : inactiveConcepts) {
                if (source.canProvide(concept) || source.canProvideSubconcept(concept)) {
                    toActivate.add(concept);
                }
            }
        }
        knowledgeManager.activateConcepts(toActivate);
    }

    @Override
    public void activated(ConceptSource source) {
        knowledgeManager.activateConcepts(knowledgeManager.getInactiveConcepts(source));
    }

    @Override
    public void deactivated(ConceptSource source) {
        List<Concept<?>> candidates = knowledgeManager.getActiveConcepts(source);
        candidates.addAll(getAllSuperConcepts(candidates));
        for (ConceptSource otherSource : conceptSourceManager.getActiveConceptSources()) {
            candidates.removeAll(knowledgeManager.getActiveConcepts(otherSource));
        }
        knowledgeManager.deactivateConcepts(candidates);
    }

    private List<Concept<?>> getAllSuperConcepts(List<Concept<?>> concepts) {
        List<Concept<?>> superConcepts = new ArrayList<Concept<?>>();
        for (Concept<?> concept : concepts) {
            superConcepts.addAll(getAllSuperConcepts(concept));
        }
        return superConcepts;
    }

    private List<? extends Concept<?>> getAllSuperConcepts(Concept<?> concept) {
        List<Concept<?>> superConcepts = new ArrayList<Concept<?>>();
        Concept<?> superConcept = concept.getSuperConcept();
        while (superConcept != null) {
            superConcepts.add(superConcept);
            superConcept = superConcept.getSuperConcept();
        }
        return superConcepts;
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

}
