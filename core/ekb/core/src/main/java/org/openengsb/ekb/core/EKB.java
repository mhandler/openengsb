package org.openengsb.ekb.core;

import javax.xml.namespace.QName;

public interface EKB {

    String transformMethodCall(QName sender, QName receiver, String xml);

    String transformReturnValue(QName sender, QName receiver, String xml);

    String transformEvent(QName sender, QName receiver, String xml);

}
