package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class StringToIntegerTransformation implements Transformation {

    private IRI targetConceptIRI;

    public StringToIntegerTransformation(IRI targetConceptIRI) {
        this.targetConceptIRI = targetConceptIRI;
    }

    public Integer transform(String input) throws TransformationException {
        try {
            return Integer.parseInt((String) input);
        } catch (Exception e) {
            throw new TransformationException("Could not parse String to integer", e);
        }
    }

    @Override
    public Integer transform(TransformationMap map, Object input) throws TransformationException {
        return transform(String.valueOf(input));
    }

    @Override
    public IRI getTargetConceptIRI(TransformationMap map, Object input) {
        return this.targetConceptIRI;
    }

    @Override
    public Class<?> getTargetType(TransformationMap map, Object input) {
        return Integer.class;
    }

}
