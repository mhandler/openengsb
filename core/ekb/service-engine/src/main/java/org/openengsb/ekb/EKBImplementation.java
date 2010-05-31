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
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.DomainQueryInterface;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.core.conceptsourcemanagement.ConceptSourceManager;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeManager;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.Transformer;

public class EKBImplementation implements EKB {

    private KnowledgeManager knowledgeManager;

    private EKBEndpoint endpoint;

    private MessageProperties messageProperties;

    private ConceptSourceManager conceptSourceManager;

    private Transformer transformer;

    @Override
    public List<Concept<?>> getAllConcepts() {
        return knowledgeManager.getActiveConcepts();
    }

    @Override
    public Concept<?> getConcept(String id) throws NoSuchConceptException {
        return knowledgeManager.getConcept(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Concept<T> getConcept(String id, Class<T> conceptClass) throws NoSuchConceptException {
        Concept<?> concept = getConcept(id);
        checkConceptClass(id, conceptClass, concept);
        return (Concept<T>) concept;
    }

    private void checkConceptClass(String id, Class<?> conceptClass, Concept<?> concept) throws NoSuchConceptException {
        if (concept != null && !concept.getConceptClass().equals(conceptClass)) {
            throw new NoSuchConceptException("The concept stored under the id " + id + " has concept class "
                    + concept.getConceptClass() + " but a concept with concept class " + conceptClass
                    + " was requested.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getData(ConceptSource source, Concept<T> concept) {
        checkProvided(source, concept);

        if (source.canProvide(concept)) {
            Method method = getQueryMethodGetAll();
            Object[] args = new Object[] { concept.getConceptClass() };
            return (List<T>) MethodCallHelper.sendMethodCall(endpoint, source.getService(), method, args,
                    messageProperties);
        }

        for (Concept<?> subConcept : concept.getSubConcepts()) {
            List data = getData(source, subConcept);
            if (!data.isEmpty()) {
                return transform(data, subConcept, concept);
            }
        }
        return new ArrayList<T>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getDataElement(ConceptSource source, Concept<T> concept, String key) {
        checkProvided(source, concept);

        if (source.canProvide(concept)) {
            Method method = getQueryMethodGetByKey();
            Object[] args = new Object[] { concept.getConceptClass(), key };
            return (T) MethodCallHelper.sendMethodCall(endpoint, source.getService(), method, args, messageProperties);
        }

        for (Concept<?> subConcept : concept.getSubConcepts()) {
            Object data = getDataElement(source, subConcept, key);
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
            return DomainQueryInterface.class.getMethod("getByKey", Class.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ConceptSource> getSources(Concept<?> concept) {
        List<ConceptSource> activeConceptSources = conceptSourceManager.getActiveConceptSources();
        List<ConceptSource> sources = new ArrayList<ConceptSource>();
        for (ConceptSource source : activeConceptSources) {
            if (source.canProvide(concept) || source.canProvideSubconcept(concept)) {
                sources.add(source);
            }
        }
        return sources;
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
