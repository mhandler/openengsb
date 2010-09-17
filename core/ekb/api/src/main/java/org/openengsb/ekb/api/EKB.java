/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE\-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.openengsb.ekb.api;

import java.util.List;

public interface EKB {

    Concept<?> getConcept(ConceptKey key) throws NoSuchConceptException;

    List<Concept<?>> getAllConcepts();

    <TYPE> Concept<TYPE> getConcept(ConceptKey key, Class<TYPE> conceptClass) throws NoSuchConceptException;

    List<ConceptSource> getSources(Concept<?> concept);

    <TYPE> List<TYPE> getData(ConceptSource source, Concept<TYPE> concept);

    <TYPE> TYPE getDataElement(ConceptSource source, Concept<TYPE> concept, Object key);

}
