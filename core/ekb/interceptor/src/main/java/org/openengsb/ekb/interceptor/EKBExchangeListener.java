package org.openengsb.ekb.interceptor;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;

import org.apache.servicemix.jbi.jaxp.SourceTransformer;
import org.apache.servicemix.nmr.api.Endpoint;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Role;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.event.ExchangeListener;
import org.apache.servicemix.nmr.api.internal.InternalExchange;

public class EKBExchangeListener implements ExchangeListener {

    @Override
    public void exchangeDelivered(Exchange exchange) {
        // do nothing
    }

    @Override
    public void exchangeFailed(Exchange exchange) {
        // do nothing
    }

    @Override
    public void exchangeSent(Exchange exchange) {
        if (exchange == null) {
            return;
        }
        if (exchange instanceof InternalExchange) {
            InternalExchange iex = (InternalExchange) exchange;
            internalExchangeSent(iex);
        }
    }

    private void internalExchangeSent(InternalExchange iex) {
        if (iex.getStatus() != Status.Active) {
            return;
        }

        if (isInCall(iex)) {
            handleInCall(iex);
        } else {
            handleReturnCall(iex);
        }
    }

    private boolean isInCall(InternalExchange iex) {
        String operation = iex.getOperation().getLocalPart();
        return (iex.getRole() == Role.Consumer && operation.equals("methodcall")) || operation.equals("event");
    }

    private void handleInCall(InternalExchange iex) {
        try {
            System.out.println("handle in call...");

            String sourceService = (String) iex.getSource().getMetaData().get(Endpoint.SERVICE_NAME);
            System.out.println("source service " + sourceService);

            QName targetService = iex.getProperty("javax.jbi.ServiceName", QName.class);
            System.out.println("target service " + targetService);

            String inXml = new SourceTransformer().toString(iex.getIn().getBody(Source.class));
            System.out.println("In Message xml " + inXml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleReturnCall(InternalExchange iex) {
        try {
            System.out.println("handle return call...");

            // for the return call source and destination are switched
            String sourceService = (String) iex.getDestination().getMetaData().get(Endpoint.SERVICE_NAME);
            System.out.println("target service " + sourceService);

            String targetService = (String) iex.getSource().getMetaData().get(Endpoint.SERVICE_NAME);
            System.out.println("source service " + targetService);

            String outXml = new SourceTransformer().toString(iex.getOut().getBody(Source.class));
            System.out.println("Out Message xml: " + outXml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
