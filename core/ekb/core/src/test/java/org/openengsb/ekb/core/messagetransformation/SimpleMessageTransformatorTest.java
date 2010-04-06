package org.openengsb.ekb.core.messagetransformation;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.core.messagetransformation.internal.SimpleMessageTransformator;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationStore;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;

public class SimpleMessageTransformatorTest {

    private MessageTransformator messageTransformator;

    @Before
    public void setUp() {
        messageTransformator = new SimpleMessageTransformator();
        messageTransformator.setSourceOntology(getSourceOntology());
        messageTransformator.setTargetOntology(getTargetOntology());
        messageTransformator.setTransformationStore(getTransformationStore());
    }

    @Test
    public void testTransformEvent() {
        // TODO testTransformEvent
    }

    @Test
    public void testTransformMethodCall() {
        // TODO testTransformMethodCall
    }

    @Test
    public void testTransformReturnValue() {
        // TODO testTransformReturnValue
    }

    private TransformationStore getTransformationStore() {
        // TODO getTransformationStore
        return null;
    }

    private OntologyDescriptor getTargetOntology() {
        // TODO getTargetOntology
        return null;
    }

    private OntologyDescriptor getSourceOntology() {
        // TODO getSourceOntology
        return null;
    }

}
