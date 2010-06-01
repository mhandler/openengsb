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

import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.NoSuchConceptException;

public interface KnowledgeManager {

    public List<Concept<?>> getActiveConcepts();

    public Concept<?> getActiveConcept(String id) throws NoSuchConceptException;

    public List<Concept<?>> getActiveConcepts(ConceptSource source);

    public List<Concept<?>> getInactiveConcepts();

    public List<Concept<?>> getInactiveConcepts(ConceptSource source);

    public void setConcepts(List<Concept<?>> concepts);

    public void deactivateConcepts(List<Concept<?>> concepts);

    public void activateConcepts(List<Concept<?>> concepts);

}
