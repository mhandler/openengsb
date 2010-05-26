package org.openengsb.ekb.core.softreferences;

import java.util.List;

import org.openengsb.ekb.api.Concept;

public interface DeReferencer {

    <T, U> List<T> deref(Concept<U> sourceConcept, U source, Concept<T> targetConcept);

}
