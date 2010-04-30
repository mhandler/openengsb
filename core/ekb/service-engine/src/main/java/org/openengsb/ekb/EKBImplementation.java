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
import org.openengsb.ekb.core.messagetransformation.transformationstore.internal.InMemoryTransformationStore;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;
import org.openengsb.util.serialization.SerializationException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

public class EKBImplementation implements EKB {

    private TransformationStore transformationStore = new InMemoryTransformationStore();

    @Override
    public String transformEvent(String senderQName, String receiverQName, String xml) {
        try {
            MessageTransformator transformator = createTransformator(senderQName, receiverQName);
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
    public String transformMethodCall(String senderQName, String receiverQName, String xml) {
        try {
            MessageTransformator transformator = createTransformator(senderQName, receiverQName);
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
    public String transformReturnValue(String senderQName, String receiverQName, String xml) {
        try {
            MessageTransformator transformator = createTransformator(senderQName, receiverQName);
            ReturnValue returnValue = Transformer.toReturnValue(xml);
            ReturnValue result = transformator.transformReturnValue(returnValue);
            return Transformer.toXml(result);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    public QName toQName(String qName) {
        return QName.valueOf(qName);
    }

    private MessageTransformator createTransformator(String senderQName, String receiverQName) {
        QName sender = toQName(senderQName);
        QName receiver = toQName(receiverQName);
        MessageTransformator transformator = new SimpleMessageTransformator();
        transformator.setSourceOntology(getOntology(sender));
        transformator.setTargetOntology(getOntology(receiver));
        transformator.setTransformationStore(transformationStore);
        return transformator;
    }

    private OntologyDescriptor getOntology(QName endpoint) {
        // TODO replace dummy implementation
        return new OntologyDescriptor(IRI.create("test-" + endpoint.getLocalPart()), IRI.create("1.0")) {

            @Override
            public OWLOntology getOntology() {
                return null;
            }
        };
    }
}
