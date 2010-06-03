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
package org.openengsb.ekb.analyzer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.core.ConceptImpl;

public class ConceptCache {

    private Map<String, ConceptImpl<?>> concepts = new ConcurrentHashMap<String, ConceptImpl<?>>();

    private Set<ConceptCacheListener> listeners = new HashSet<ConceptCacheListener>();

    public ConceptImpl<?> getConcept(String id) {
        return concepts.get(id);
    }

    public void storeConcept(ConceptImpl<?> concept) {
        this.concepts.put(concept.getId(), concept);
        notifyAboutStore(concept);
    }

    public void addListener(ConceptCacheListener listener) {
        synchronized (listeners) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(ConceptCacheListener listener) {
        synchronized (listeners) {
            this.listeners.remove(listener);
        }
    }

    private void notifyAboutStore(Concept<?> concept) {
        Set<ConceptCacheListener> listenersCopy = new HashSet<ConceptCacheListener>();
        synchronized (listeners) {
            listenersCopy.addAll(listeners);
        }
        for (ConceptCacheListener listener : listenersCopy) {
            listener.conceptStored(concept);
        }
    }

}
