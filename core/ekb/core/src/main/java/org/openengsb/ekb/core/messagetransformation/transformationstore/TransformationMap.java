package org.openengsb.ekb.core.messagetransformation.transformationstore;

import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.semanticweb.owlapi.model.IRI;

public interface TransformationMap {

    void putTransformation(IRI sourceConcept, Transformation t);

    void removeTransformation(IRI sourceConcept);

    Transformation getTransformation(IRI sourceConcept);

}
