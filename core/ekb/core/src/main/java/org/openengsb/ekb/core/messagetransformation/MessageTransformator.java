package org.openengsb.ekb.core.messagetransformation;

import org.openengsb.core.model.Event;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.ReturnValue;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationStore;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;

public interface MessageTransformator {

    void setSourceOntology(OntologyDescriptor sourceOntology);

    void setTargetOntology(OntologyDescriptor targetOntology);

    void setTransformationStore(TransformationStore transformationStore);

    MethodCall transformMethodCall(MethodCall methodCall) throws TransformationException;

    ReturnValue transformReturnValue(ReturnValue returnValue) throws TransformationException;

    Event transformEvent(Event event) throws TransformationException;

}
