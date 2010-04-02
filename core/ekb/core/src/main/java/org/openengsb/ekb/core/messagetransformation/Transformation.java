package org.openengsb.ekb.core.messagetransformation;

import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;

public interface Transformation {

    Object transform(TransformationMap map, Object input) throws TransformationException;

}
