package org.openengsb.ekb.interceptor;

import javax.xml.namespace.QName;

public class EKBInterceptorUtil {

    public static QName getEKBService() {
        return new QName("urn:openengsb:ekb", "ekbService");
    }

}
