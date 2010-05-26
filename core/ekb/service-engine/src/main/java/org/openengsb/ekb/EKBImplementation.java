package org.openengsb.ekb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.openengsb.core.MethodCallHelper;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.DomainQueryInterface;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.core.endpointmanagement.EndpointManager;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeManager;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.Transformer;

public class EKBImplementation implements EKB {

    private KnowledgeManager knowledgeManager;

    private EKBEndpoint endpoint;

    private MessageProperties messageProperties;

    private EndpointManager endpointManager;

    private Transformer transformer;

    @Override
    public List<Concept<?>> getAllConcepts() {
        return knowledgeManager.getActiveConcepts();
    }

    @Override
    public Concept<?> getConcept(String id) {
        return knowledgeManager.getConcept(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Concept<T> getConcept(String id, Class<T> conceptClass) {
        return (Concept<T>) getConcept(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getData(ConceptSource source, Concept<T> concept) {
        checkProvided(source, concept);

        if (source.getId().equals(concept.getModelPartId())) {
            QName service = new QName(source.getUrn(), source.getService());
            Method method = getQueryMethod();
            Object[] args = new Object[] { concept.getConceptClass() };
            return (List<T>) MethodCallHelper.sendMethodCall(endpoint, service, method, args, messageProperties);
        }

        for (Concept<?> subConcept : concept.getSubConcepts()) {
            List data = getData(source, subConcept);
            if (!data.isEmpty()) {
                return transform(data, concept);
            }
        }
        return new ArrayList<T>();
    }

    private <T> List<T> transform(List<?> data, Concept<T> concept) {
        try {
            List<T> result = new ArrayList<T>();
            for (Object element : data) {
                result.add(transformer.transform(concept, element));
            }
            return result;
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkProvided(ConceptSource source, Concept<?> concept) {
        if (!source.canProvide(concept)) {
            throw new IllegalArgumentException("ConceptSource " + source + " does not provide the concept: " + concept);
        }
    }

    private Method getQueryMethod() {
        try {
            return DomainQueryInterface.class.getMethod("getAll", Class.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ConceptSource> getSources(Concept<?> concept) {
        List<ConceptSource> activeConceptSources = endpointManager.getActiveConceptSources();
        List<ConceptSource> sources = new ArrayList<ConceptSource>();
        for (ConceptSource source : activeConceptSources) {
            if (source.canProvide(concept)) {
                sources.add(source);
            }
        }
        return sources;
    }

    @Override
    public <T, U> List<T> followSoftReference(Concept<U> sourceConcept, U source, Concept<T> targetConcept) {
        // TODO Auto-generated method stub
        return null;
    }

}
