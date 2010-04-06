package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;

public class NewValueArgumentTransformationDescriptor implements ArgumentTransformationDescriptor {

    private Value newValue;

    public NewValueArgumentTransformationDescriptor(Value newValue) {
        this.newValue = newValue;
    }

    @Override
    public Value getArgument(MethodCall inCall, TransformationMap map) {
        return newValue;
    }

}
