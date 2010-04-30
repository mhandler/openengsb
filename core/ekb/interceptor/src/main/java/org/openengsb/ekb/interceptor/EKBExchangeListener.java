package org.openengsb.ekb.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.event.ExchangeListener;
import org.apache.servicemix.nmr.api.internal.InternalExchange;

public class EKBExchangeListener implements ExchangeListener {

    private Set<QName> blackList;

    public EKBExchangeListener() {
        blackList = new HashSet<QName>();
        blackList.add(EKBInterceptorUtil.getEKBService());
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
            return;
        }
        TransformationConnector transformationConnector = new TransformationConnector(iex);
        if (transformationConnector.isInCall()) {
            transformationConnector.handleInCall();
        } else {
            transformationConnector.handleReturnCall();
        }
    }

    private boolean isOnBlackList(InternalExchange iex) {
        QName targetService = iex.getProperty("javax.jbi.ServiceName", QName.class);
        return blackList.contains(targetService);
    }

}
