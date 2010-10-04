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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openengsb.core.MethodCallHelper;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.NoSuchConceptSourceException;
import org.openengsb.ekb.api.NoSuchSoftRefernceException;
import org.openengsb.ekb.api.SoftReference;
import org.openengsb.ekb.api.conceptSource.ConceptSource;
import org.openengsb.ekb.api.conceptSource.ConceptSourceManager;
import org.openengsb.ekb.api.conceptSource.DomainQueryInterface;
import org.openengsb.ekb.api.mapping.TransformationException;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeManager;
import org.openengsb.ekb.core.transformation.Transformer;

public class EKBImplementation implements EKB {

    private KnowledgeManager knowledgeManager;

    private EKBEndpoint endpoint;

    private MessageProperties messageProperties;

    private ConceptSourceManager conceptSourceManager;

    private Transformer transformer;

    @Override
    public List<ConceptKey> getAllConcepts() {
        List<Concept<?>> concepts = getConcepts();
        List<ConceptKey> keys = new ArrayList<ConceptKey>();
        for (Concept<?> concept : concepts) {
            keys.add(concept.getKey());
        }
        return keys;
    }

    private List<Concept<?>> getConcepts() {
        return knowledgeManager.getActiveConcepts();
    }

    private Concept<?> getConcept(ConceptKey key) throws NoSuchConceptException {
        return knowledgeManager.getActiveConcept(key);
    }

    @SuppressWarnings("unchecked")
    private <T> Concept<T> getConcept(ConceptKey key, Class<T> conceptClass) throws NoSuchConceptException {
        Concept<?> concept = getConcept(key);
        checkConceptClass(key, conceptClass, concept);
        return (Concept<T>) concept;
    }

    private void checkConceptClass(ConceptKey key, Class<?> conceptClass, Concept<?> concept)
            throws NoSuchConceptException {
        if (concept != null && !concept.getConceptClass().equals(conceptClass)) {
            throw new NoSuchConceptException("The concept stored under the key " + key + " has concept class "
                    + concept.getConceptClass() + " but a concept with concept class " + conceptClass
                    + " was requested.");
        }
    }

    @Override
    public <TYPE> List<TYPE> getData(String sourceId, ConceptKey key, Class<TYPE> conceptClass)
            throws NoSuchConceptException, NoSuchConceptSourceException {
        Concept<TYPE> concept = getConcept(key, conceptClass);
        ConceptSource source = getSource(sourceId);
        return internalGetData(source, concept);
    }

    @Override
    public List<?> getData(String sourceId, ConceptKey conceptKey) throws NoSuchConceptException,
            NoSuchConceptSourceException {
        Concept<?> concept = getConcept(conceptKey);
        ConceptSource source = getSource(sourceId);
        return internalGetData(source, concept);
    }

    @SuppressWarnings({ "unchecked" })
    private <TYPE> List<TYPE> internalGetData(ConceptSource source, Concept<TYPE> concept) {
        checkProvided(source, concept);

        if (source.canProvide(concept)) {
            Method method = getQueryMethodGetAll();
            Object[] args = new Object[] { concept.getConceptClass() };
            return (List<TYPE>) MethodCallHelper.sendMethodCall(endpoint, source.getService(), method, args,
                    messageProperties);
        }

        for (Concept<?> subConcept : concept.getSubConcepts()) {
            List<?> data = internalGetData(source, subConcept);
            if (!data.isEmpty()) {
                return transform(data, subConcept, concept);
            }
        }
        return new ArrayList<TYPE>();
    }

    @Override
    public <TYPE> TYPE getDataElement(String sourceId, ConceptKey conceptKey, Class<TYPE> conceptClass, Object key)
            throws NoSuchConceptException, NoSuchConceptSourceException {
        Concept<TYPE> concept = getConcept(conceptKey, conceptClass);
        ConceptSource source = getSource(sourceId);
        return internalGetDataElement(source, concept, key);
    }

    @Override
    public Object getDataElement(String sourceId, ConceptKey conceptKey, Object key) throws NoSuchConceptException,
            NoSuchConceptSourceException {
        Concept<?> concept = getConcept(conceptKey);
        ConceptSource source = getSource(sourceId);
        return internalGetDataElement(source, concept, key);
    }

    @SuppressWarnings("unchecked")
    private <T> T internalGetDataElement(ConceptSource source, Concept<T> concept, Object key) {
        checkProvided(source, concept);

        if (source.canProvide(concept)) {
            Method method = getQueryMethodGetByKey();
            Object[] args = new Object[] { concept.getConceptClass(), key };
            return (T) MethodCallHelper.sendMethodCall(endpoint, source.getService(), method, args, messageProperties);
        }

        for (Concept<?> subConcept : concept.getSubConcepts()) {
            Object data = internalGetDataElement(source, subConcept, key);
            if (data != null) {
                return transform(Collections.singletonList(data), subConcept, concept).get(0);
            }
        }
        return null;
    }

    private <T> List<T> transform(List<?> data, Concept<?> sourceConcept, Concept<T> targetConcept) {
        try {
            List<T> result = new ArrayList<T>();
            for (Object element : data) {
                result.add(transformer.transform(sourceConcept, targetConcept, element));
            }
            return result;
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkProvided(ConceptSource source, Concept<?> concept) {
        if (!source.canProvide(concept) && !source.canProvideSubconcept(concept)) {
            throw new IllegalArgumentException("ConceptSource " + source + " does not provide the concept: " + concept
                    + " or any subconcepts of it.");
        }
    }

    private Method getQueryMethodGetAll() {
        try {
            return DomainQueryInterface.class.getMethod("getAll", Class.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getQueryMethodGetByKey() {
        try {
            return DomainQueryInterface.class.getMethod("getByKey", Class.class, Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private ConceptSource getSource(String id) throws NoSuchConceptSourceException {
        List<ConceptSource> activeConceptSources = conceptSourceManager.getActiveConceptSources();
        for (ConceptSource source : activeConceptSources) {
            if (source.getId().equals(id)) {
                return source;
            }
        }
        throw new NoSuchConceptSourceException("No active concept source with id " + id);
    }

    @Override
    public List<String> getSourceIds(ConceptKey key) throws NoSuchConceptException {
        List<ConceptSource> activeConceptSources = conceptSourceManager.getActiveConceptSources();
        List<String> sourceIds = new ArrayList<String>();
        Concept<?> concept = getConcept(key);
        for (ConceptSource source : activeConceptSources) {
            if (source.canProvide(concept) || source.canProvideSubconcept(concept)) {
                sourceIds.add(source.getId());
            }
        }
        return sourceIds;
    }

    @Override
    public List<String> getSoftReferenceIds(ConceptKey sourceConcept) throws NoSuchConceptException {
        Concept<?> concept = getConcept(sourceConcept);
        List<String> ids = new ArrayList<String>();
        for (SoftReference<?, ?> softRef : concept.getSoftReferences()) {
            ids.add(softRef.getId());
        }
        return ids;
    }

    @Override
    public List<String> getSoftReferenceIds(ConceptKey sourceConcept, ConceptKey targetConcept)
            throws NoSuchConceptException {
        Concept<?> concept = getConcept(sourceConcept);
        Concept<?> target = getConcept(targetConcept);
        List<String> ids = new ArrayList<String>();
        for (SoftReference<?, ?> softRef : concept.getSoftReferences(target)) {
            ids.add(softRef.getId());
        }
        return ids;
    }

    @Override
    public <SOURCETYPE, TARGETTYPE> List<TARGETTYPE> follow(ConceptKey sourceConcept, Class<SOURCETYPE> sourceType,
            String softReferenceId, SOURCETYPE source, ConceptKey targetConcept, Class<TARGETTYPE> targetType)
            throws NoSuchConceptException, NoSuchSoftRefernceException {
        Concept<SOURCETYPE> concept = getConcept(sourceConcept, sourceType);
        Concept<TARGETTYPE> target = getConcept(targetConcept, targetType);
        for (SoftReference<SOURCETYPE, TARGETTYPE> softRef : concept.getSoftReferences(target)) {
            if (softRef.getId().equals(softReferenceId)) {
                return softRef.follow(this, source);
            }
        }
        return null;
    }

    public void setKnowledgeManager(KnowledgeManager knowledgeManager) {
        this.knowledgeManager = knowledgeManager;
    }

    public void setEndpoint(EKBEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    public void setConceptSourceManager(ConceptSourceManager conceptSourceManager) {
        this.conceptSourceManager = conceptSourceManager;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

}
