package org.openengsb.ekb.interceptor;

import org.apache.servicemix.nmr.api.Exchange;
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
        if (exchange instanceof InternalExchange) {
            InternalExchange iex = (InternalExchange) exchange;
            internalExchangeSent(iex);
        }
        System.out.println("EKBExchangeListener - Exchange sent - not an internal exchange");
    }

    private void internalExchangeSent(InternalExchange iex) {
        System.out.println("EKBExchangeListener - Exchange sent - destination id: " + iex.getDestination().getId());
    }

}
