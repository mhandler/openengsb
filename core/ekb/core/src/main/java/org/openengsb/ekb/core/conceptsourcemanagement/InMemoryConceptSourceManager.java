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
package org.openengsb.ekb.core.conceptsourcemanagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openengsb.ekb.api.conceptSource.ConceptSource;
import org.openengsb.ekb.api.conceptSource.ConceptSourceManager;
import org.openengsb.ekb.api.conceptSource.ConceptSourceStatusListener;

public class InMemoryConceptSourceManager implements ConceptSourceManager {

    private Set<ConceptSource> activeSources = new HashSet<ConceptSource>();

    private Set<ConceptSourceStatusListener> listeners = new HashSet<ConceptSourceStatusListener>();

    public InMemoryConceptSourceManager(Set<ConceptSourceStatusListener> listeners) {
        this.listeners.addAll(listeners);
    }

    @Override
    public void activate(ConceptSource source) {
        activeSources.add(source);
        notifyListeners(source, true);
    }

    @Override
    public void deactivate(ConceptSource source) {
        activeSources.remove(source);
        notifyListeners(source, false);
    }

    @Override
    public List<ConceptSource> getActiveConceptSources() {
        return new ArrayList<ConceptSource>(activeSources);
    }

    @Override
    public void addListener(ConceptSourceStatusListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ConceptSourceStatusListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(ConceptSource source, boolean activated) {
        for (ConceptSourceStatusListener listener : listeners) {
            if (activated) {
                listener.activated(source);
            } else {
                listener.deactivated(source);
            }
        }
    }

}
