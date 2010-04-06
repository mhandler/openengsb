package org.openengsb.ekb.core.messagetransformation;

import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

public interface Transformation {

    Object transform(TransformationMap map, Object input) throws TransformationException;

    /**
     * Get the concept IRI of the target concept (the concept IRI of the object
     * that would be returned if transform was called with the same parameters).
     * 
     * @param map
     * @param input
     * @return
     */
    IRI getTargetConceptIRI(TransformationMap map, Object input);

    Class<?> getTargetType(TransformationMap map, Object input);

}
