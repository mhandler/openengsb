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

    List<ConceptKey> getAllConcepts();

    List<String> getSourceIds(ConceptKey concept) throws NoSuchConceptException;

    List<?> getData(String sourceId, ConceptKey concept) throws NoSuchConceptException, NoSuchConceptSourceException;

    <TYPE> List<TYPE> getData(String sourceId, ConceptKey concept, Class<TYPE> conceptClass)
            throws NoSuchConceptException, NoSuchConceptSourceException;

    Object getDataElement(String sourceId, ConceptKey concept, Object key) throws NoSuchConceptException,
            NoSuchConceptSourceException;

    <TYPE> TYPE getDataElement(String sourceId, ConceptKey concept, Class<TYPE> conceptClass, Object key)
            throws NoSuchConceptException, NoSuchConceptSourceException;

    List<String> getSoftReferenceIds(ConceptKey sourceConcept) throws NoSuchConceptException;

    List<String> getSoftReferenceIds(ConceptKey sourceConcept, ConceptKey targetConcept) throws NoSuchConceptException;

    public <SOURCETYPE, TARGETTYPE> List<TARGETTYPE> follow(ConceptKey sourceConcept, Class<SOURCETYPE> sourceType,
            String softReferenceId, SOURCETYPE source, ConceptKey targetConcept, Class<TARGETTYPE> targetType)
            throws NoSuchConceptException, NoSuchSoftRefernceException;

}
