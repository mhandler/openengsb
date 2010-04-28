package org.openengsb.ekb.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.event.ExchangeListener;
import org.apache.servicemix.nmr.api.internal.InternalExchange;

public class EKBExchangeListener implements ExchangeListener {

    private TransformationConnector transformationConnector;

    private Set<QName> blackList;

    public EKBExchangeListener() {
        transformationConnector = new TransformationConnector();
        blackList = new HashSet<QName>();
        blackList.add(transformationConnector.getEKBService());
    }

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
        if (isOnBlackList(iex)) {
            System.out.println("Ignored message exchange because it is blacklisted.");
            return;
        }
        System.out.println("not on black list...");
        if (transformationConnector.isInCall(iex)) {
            transformationConnector.handleInCall(iex);
        } else {
            transformationConnector.handleReturnCall(iex);
        }
    }

    private boolean isOnBlackList(InternalExchange iex) {
        QName targetService = iex.getProperty("javax.jbi.ServiceName", QName.class);
        return blackList.contains(targetService);
    }

}
