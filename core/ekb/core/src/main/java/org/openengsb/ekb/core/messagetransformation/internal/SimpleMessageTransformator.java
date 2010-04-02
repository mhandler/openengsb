package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.core.model.Event;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.ReturnValue;
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
        Object value = returnValue.getValue();
        Object newValue = handleConcept(value);

        Class<?> clazz = void.class;
        if (newValue != null) {
            clazz = newValue.getClass();
        }

        return new ReturnValue(newValue, clazz);
    }

    private Object handleConcept(Object concept) throws TransformationException {
        TransformationMap map = getTransformationMap();
        if (map == null) {
            return concept;
        }
        IRI iri = TransformationUtil.extractConceptIRI(concept);
        Transformation transformation = map.getTransformation(iri);

        if (transformation == null) {
            return concept;
        }

        return transformation.transform(map, concept);
    }

}
