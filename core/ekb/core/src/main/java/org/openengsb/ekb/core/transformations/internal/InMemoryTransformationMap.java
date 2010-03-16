package org.openengsb.ekb.core.transformations.internal;

import java.util.HashMap;
import java.util.Map;

import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.transformations.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class InMemoryTransformationMap implements TransformationMap {

    private Map<IRI, Transformation> transformations = new HashMap<IRI, Transformation>();

    @Override
    public Transformation getTransformation(IRI sourceConcept) {
        return transformations.get(sourceConcept);
    }

    @Override
    public void putTransformation(IRI sourceConcept, Transformation t) {
        transformations.put(sourceConcept, t);
    }

    @Override
    public void removeTransformation(IRI sourceConcept) {
        transformations.remove(sourceConcept);
    }

}
