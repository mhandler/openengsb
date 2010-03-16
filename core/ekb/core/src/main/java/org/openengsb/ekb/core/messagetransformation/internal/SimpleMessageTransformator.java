package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.core.model.Concept;
import org.openengsb.core.model.Event;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.ReturnValue;
import org.openengsb.ekb.core.messagetransformation.MessageTransformator;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;
import org.openengsb.ekb.core.transformations.TransformationMap;
import org.openengsb.ekb.core.transformations.TransformationStore;
import org.semanticweb.owlapi.model.IRI;

public class SimpleMessageTransformator implements MessageTransformator {

    private OntologyDescriptor sourceOntology;

    private OntologyDescriptor targetOntology;

    private TransformationStore transformationStore;

    @Override
    public void setSourceOntology(OntologyDescriptor sourceOntology) {
        this.sourceOntology = sourceOntology;
    }

    @Override
    public void setTargetOntology(OntologyDescriptor targetOntology) {
        this.targetOntology = targetOntology;
    }

    @Override
    public void setTransformationStore(TransformationStore transformationStore) {
        this.transformationStore = transformationStore;
    }

    @Override
    public Event transformEvent(Event event) throws TransformationException {
        return (Event) transform(event); // TODO
    }

    @Override
    public MethodCall transformMethodCall(MethodCall methodCall) throws TransformationException {
        return methodCall; // TODO
    }

    @Override
    public ReturnValue transformReturnValue(ReturnValue returnValue) throws TransformationException {
        Concept value = (Concept) returnValue.getValue();
        Object newValue = transform(value);
        return new ReturnValue(newValue, newValue.getClass());
    }

    private Object transform(Concept source) throws TransformationException {
        IRI sourceIRI = IRI.create(source.getConceptIRI());
        TransformationMap map = transformationStore.getTransformationMap(sourceOntology.getOntologyID(), targetOntology
                .getOntologyID());
        if (map == null) {
            return source;
        }

        Transformation transformation = map.getTransformation(sourceIRI);
        if (transformation == null) {
            return source;
        }
        return transformation.transform(source);
    }
}
