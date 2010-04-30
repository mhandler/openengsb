package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.core.model.Event;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.ReturnValue;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.TransformationUtil;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class SimpleMessageTransformator extends AbstractMessageTransformator {

    @Override
    public Event transformEvent(Event event) throws TransformationException {
        return (Event) handleConcept(event);
    }

    @Override
    public MethodCall transformMethodCall(MethodCall methodCall) throws TransformationException {
        return (MethodCall) handleConcept(methodCall);
    }

    @Override
    public ReturnValue transformReturnValue(ReturnValue returnValue) throws TransformationException {
        Object value = returnValue.getValue().getValue();
        IRI iri = IRI.create(returnValue.getValue().getConceptIRI());
        Object newValue = transform(value, getTransformationMap(), iri);

        Class<?> clazz = void.class;
        if (newValue != null) {
            clazz = newValue.getClass();
        }

        return new ReturnValue(new Value(newValue, clazz, getTargetIRI(value, getTransformationMap(), iri)));
    }

    private Object handleConcept(Object concept) throws TransformationException {
        TransformationMap map = getTransformationMap();
        IRI iri = TransformationUtil.extractConceptIRI(concept);
        return transform(concept, map, iri);
    }

    private Object transform(Object concept, TransformationMap map, IRI iri) throws TransformationException {
        if (map == null) {
            return concept;
        }

        Transformation transformation = map.getTransformation(iri);

        if (transformation == null) {
            return concept;
        }

        return transformation.transform(map, concept);
    }

    private String getTargetIRI(Object concept, TransformationMap map, IRI iri) throws TransformationException {
        if (map == null) {
            return iri.toString();
        }

        Transformation transformation = map.getTransformation(iri);

        if (transformation == null) {
            return iri.toString();
        }

        return transformation.getTargetConceptIRI(map, concept).toString();
    }

}
