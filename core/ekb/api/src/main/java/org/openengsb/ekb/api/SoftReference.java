package org.openengsb.ekb.api;

import java.util.List;

public interface SoftReference<U, T> {

    List<T> follow(EKB ekb, U sourceObject);

    Concept<U> getSourceConcept();

    Concept<T> getTargetConcept();

}
