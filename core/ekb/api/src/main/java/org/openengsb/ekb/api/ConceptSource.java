package org.openengsb.ekb.api;

import javax.xml.namespace.QName;

public interface ConceptSource {

    String getId();

    QName getService();

    boolean canProvide(Concept<?> concept);

    @Override
    String toString();
}
