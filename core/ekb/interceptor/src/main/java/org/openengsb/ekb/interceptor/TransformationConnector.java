package org.openengsb.ekb.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jbi.messaging.MessagingException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.apache.servicemix.jbi.jaxp.SourceTransformer;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.Endpoint;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Message;
import org.apache.servicemix.nmr.api.Pattern;
import org.apache.servicemix.nmr.api.Role;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.internal.InternalExchange;
import org.apache.servicemix.nmr.core.PropertyMatchingReference;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.api.EKB;

public class TransformationConnector {

    boolean isInCall(InternalExchange iex) {
        String operation = iex.getOperation().getLocalPart();
        return (iex.getRole() == Role.Consumer && operation.equals("methodcall")) || operation.equals("event");
    }

    void handleInCall(InternalExchange iex) {
        try {
            String source = (String) iex.getSource().getMetaData().get(Endpoint.SERVICE_NAME);
            QName targetService = iex.getProperty("javax.jbi.ServiceName", QName.class);
            String target = targetService.toString();

            String inXml = new SourceTransformer().toString(iex.getIn().getBody(Source.class));
            String operation = iex.getOperation().getLocalPart();

            Channel channel = iex.getSource().getChannel().getNMR().createChannel();
            Method transformationMethod = getTransformationMethod(operation);

            Object[] args = new Object[] { source, target, inXml };
            MessageProperties msgProperties = getMessageProperties(iex);
            String transformed = (String) sendMethodCall(channel, getEKBService(), transformationMethod, args,
                    msgProperties);
            iex.getIn().setBody(new StringSource(transformed));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void handleReturnCall(InternalExchange iex) {
        try {
            // for the return call source and destination are switched
            String source = (String) iex.getDestination().getMetaData().get(Endpoint.SERVICE_NAME);
            String target = (String) iex.getSource().getMetaData().get(Endpoint.SERVICE_NAME);

            String outXml = new SourceTransformer().toString(iex.getOut().getBody(Source.class));

            Channel channel = iex.getSource().getChannel().getNMR().createChannel();
            Method transformationMethod = getTransformationMethod("returnValue");

            Object[] args = new Object[] { source, target, outXml };
            MessageProperties msgProperties = getMessageProperties(iex);

            String transformed = (String) sendMethodCall(channel, getEKBService(), transformationMethod, args,
                    msgProperties);
            iex.getOut().setBody(new StringSource(transformed));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MessageProperties getMessageProperties(InternalExchange iex) {
        return new MessageProperties("", UUID.randomUUID().toString());
    }

    QName getEKBService() {
        return new QName("urn:openengsb:ekb", "ekbService");
    }

    private Method getTransformationMethod(String operation) {
        try {
            if (operation.equals("event")) {
                return EKB.class.getMethod("transformEvent", String.class, String.class, String.class);
            } else if (operation.equals("methodcall")) {
                return EKB.class.getMethod("transformMethodCall", String.class, String.class, String.class);
            } else {
                return EKB.class.getMethod("transformReturnValue", String.class, String.class, String.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object sendMethodCall(Channel channel, QName service, Method method, Object[] args,
            MessageProperties msgProperties) {
        Object[] arguments = checkArgs(args);
        try {
            Exchange inout = channel.createExchange(Pattern.InOut);
            createInMessage(service, method, msgProperties, arguments, inout);
            channel.sendSync(inout);
            checkFailure(inout, method);
            return handleOutMessage(inout);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createInMessage(QName service, Method method, MessageProperties msgProperties, Object[] arguments,
            Exchange inout) throws MessagingException {
        inout.setProperty("javax.jbi.ServiceName", service);
        inout.setOperation(new QName("methodcall"));
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("ENDPOINT_NAME", "ekbEndpoint");
        properties.put("SERVICE_NAME", getEKBService().toString());
        inout.setTarget(new PropertyMatchingReference(properties));

        Message msg = inout.getIn();
        applyPropertiesToMessage(msg, msgProperties);

        MethodCall call = new MethodCall(method, arguments);

        String xml = toXml(call);

        msg.setBody(new StringSource(xml));
    }

    private String toXml(MethodCall call) {
        String methodCall = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<XMLMethodCall><methodName>"
                + call.getMethodName() + "</methodName>";
        int id = 0;
        for (Value arg : call.getArguments()) {
            String argText = arg.getValue().toString();
            argText = encode(argText);
            methodCall += "<args><type>java.lang.String</type><conceptIRI>" + arg.getConceptIRI()
                    + "</conceptIRI><value><primitive><string>" + argText + "</string></primitive><id>" + id
                    + "</id></value></args>";
            id++;
        }
        methodCall += "</XMLMethodCall>";
        return methodCall;
    }

    private void applyPropertiesToMessage(Message msg, MessageProperties msgProperties) {
        msg.setHeader("contextId", msgProperties.getContextId());
        msg.setHeader("correlationId", msgProperties.getCorrelationId());
        if (msgProperties.getWorkflowId() != null) {
            msg.setHeader("workflowId", msgProperties.getWorkflowId());
        }
        if (msgProperties.getWorkflowInstanceId() != null) {
            msg.setHeader("workflowInstanceId", msgProperties.getWorkflowInstanceId());
        }
    }

    private void checkFailure(Exchange inout, Method method) {
        if (inout.getStatus() != Status.Error) {
            return;
        }
        Exception e = inout.getError();
        if (e != null) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Failure invoking method " + method + ". No result delivered.");
    }

    private Object[] checkArgs(Object[] args) {
        if (args != null) {
            return args;
        }
        return new Object[0];
    }

    private Object handleOutMessage(Exchange inout) throws TransformerException {
        String outXml = new SourceTransformer().toString(inout.getOut().getBody(Source.class));
        return getResult(outXml);
    }

    private String getResult(String outXml) {
        String startElement = "<value><primitive><string>";
        String endElement = "</string></primitive>";
        int startIndex = outXml.indexOf(startElement) + startElement.length();
        int endIndex = outXml.lastIndexOf(endElement);
        return decode(outXml.substring(startIndex, endIndex));
    }

    private String encode(String xml) {
        return xml.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    private String decode(String xml) {
        return xml.replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&apos;", "'")
                .replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }
}
