package org.openengsb.ekb;

import javax.xml.namespace.QName;

import org.openengsb.core.model.Event;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.ReturnValue;
import org.openengsb.core.transformation.Transformer;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.core.messagetransformation.MessageTransformator;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.internal.SimpleMessageTransformator;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationStore;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;
import org.openengsb.util.serialization.SerializationException;

public class EKBImplementation implements EKB {

    private TransformationStore transformationStore;

    @Override
    public String transformEvent(QName sender, QName receiver, String xml) {
        try {
            MessageTransformator transformator = createTransformator(sender, receiver);
            Event event = Transformer.toEvent(xml);
            Event result = transformator.transformEvent(event);
            return Transformer.toXml(result);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String transformMethodCall(QName sender, QName receiver, String xml) {
        try {
            MessageTransformator transformator = createTransformator(sender, receiver);
            MethodCall methodCall = Transformer.toMethodCall(xml);
            MethodCall result = transformator.transformMethodCall(methodCall);
            return Transformer.toXml(result);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String transformReturnValue(QName sender, QName receiver, String xml) {
        try {
            MessageTransformator transformator = createTransformator(sender, receiver);
            ReturnValue returnValue = Transformer.toReturnValue(xml);
            ReturnValue result = transformator.transformReturnValue(returnValue);
            return Transformer.toXml(result);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageTransformator createTransformator(QName sender, QName receiver) {
        MessageTransformator transformator = new SimpleMessageTransformator();
        transformator.setSourceOntology(getOntology(sender));
        transformator.setTargetOntology(getOntology(receiver));
        transformator.setTransformationStore(transformationStore);
        return transformator;
    }

    private OntologyDescriptor getOntology(QName endpoint) {
        // TODO: endpoint - ontology mapping (with respect to ontology and
        // enpoint lifecycle)
        return null;
    }

}
