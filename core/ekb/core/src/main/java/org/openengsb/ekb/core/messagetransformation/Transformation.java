package org.openengsb.ekb.core.messagetransformation;


public interface Transformation {

    Object transform(Object input) throws TransformationException;

    Class<?> getTargetType(Object input);

}
