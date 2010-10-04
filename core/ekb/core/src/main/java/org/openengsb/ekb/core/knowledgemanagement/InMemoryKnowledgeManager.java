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
package org.openengsb.ekb.core.knowledgemanagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.conceptSource.ConceptSource;

public class InMemoryKnowledgeManager implements KnowledgeManager {

    private static Log log = LogFactory.getLog(InMemoryKnowledgeManager.class);

    private Set<Concept<?>> activeConcepts = new HashSet<Concept<?>>();

    private Set<Concept<?>> inactiveConcepts = new HashSet<Concept<?>>();

    private Set<KnowledgeChangeListener> listeners = new HashSet<KnowledgeChangeListener>();

    private enum NotificationType {
        ACTIVATION, DEACTIVATION, CHANGE
    }

    @Override
    public void activateConcepts(List<Concept<?>> concepts) {
        inactiveConcepts.removeAll(concepts);
        activeConcepts.addAll(concepts);
        log.info("Activated concepts: " + concepts);
        notifyAllListeners(NotificationType.ACTIVATION);
    }

    @Override
    public void deactivateConcepts(List<Concept<?>> concepts) {
        activeConcepts.removeAll(concepts);
        inactiveConcepts.addAll(concepts);
        log.info("Deactivated concepts: " + concepts);
        notifyAllListeners(NotificationType.DEACTIVATION);
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
    public Concept<?> getActiveConcept(ConceptKey key) throws NoSuchConceptException {
        for (Concept<?> concept : getActiveConcepts()) {
            if (concept.getKey().equals(key)) {
                return concept;
            }
        }
        throw new NoSuchConceptException("No concept stored with key " + key);
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
        log.info("Concepts of knowledge manager have been reset. Concepts: " + concepts);
        notifyAllListeners(NotificationType.CHANGE);
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

    @Override
    public void addListener(KnowledgeChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(KnowledgeChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyAllListeners(NotificationType type) {
        Set<KnowledgeChangeListener> listenerCopy = new HashSet<KnowledgeChangeListener>(listeners);
        for (KnowledgeChangeListener listener : listenerCopy) {
            switch (type) {
            case ACTIVATION:
                listener.conceptsActivated();
                break;
            case DEACTIVATION:
                listener.conceptsDeactivated();
                break;
            case CHANGE:
                listener.conceptsChanged();
                break;
            }
        }
    }

}
