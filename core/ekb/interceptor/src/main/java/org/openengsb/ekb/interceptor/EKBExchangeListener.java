package org.openengsb.ekb.interceptor;

import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.event.ExchangeListener;
import org.apache.servicemix.nmr.api.internal.InternalExchange;

public class EKBExchangeListener implements ExchangeListener {

    private TransformationConnector transformationConnector;

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
        if (transformationConnector.isInCall(iex)) {
            transformationConnector.handleInCall(iex);
        } else {
            transformationConnector.handleReturnCall(iex);
        }
    }

}
