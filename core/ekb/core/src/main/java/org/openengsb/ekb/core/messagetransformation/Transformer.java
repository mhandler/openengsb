package org.openengsb.ekb.core.messagetransformation;

import org.openengsb.ekb.api.Concept;

public interface Transformer {

    <T> T transform(Concept<T> target, Object input) throws TransformationException;

}
