package org.openengsb.ekb.core.messagetransformation;

import org.openengsb.core.model.Event;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.ReturnValue;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;

public interface MessageTransformator {

    void setSourceOntology(OntologyDescriptor sourceOntology);

    void setTargetOntology(OntologyDescriptor targetOntology);

    MethodCall transformMethodCall(MethodCall methodCall);

    ReturnValue transformReturnValue(ReturnValue returnValue);

    Event transformEvent(Event event);

}
