package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;

public interface ArgumentTransformationDescriptor {

    public abstract Value getArgument(MethodCall inCall, TransformationMap map) throws TransformationException;

}
