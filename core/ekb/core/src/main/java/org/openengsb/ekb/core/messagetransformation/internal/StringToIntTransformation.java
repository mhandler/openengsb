package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;

public class StringToIntTransformation implements Transformation {

    public Integer transform(String input) throws TransformationException {
        try {
            return Integer.parseInt((String) input);
        } catch (Exception e) {
            throw new TransformationException("Could not parse String to integer", e);
        }
    }

    @Override
    public Integer transform(Object input) throws TransformationException {
        return transform(String.valueOf(input));
    }

}
