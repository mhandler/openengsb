package org.openengsb.ekb.core.messagetransformation.internal;

import java.util.ArrayList;
import java.util.List;

import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;

public abstract class MethodCallTransformation implements Transformation {

    private List<ArgumentTransformationDescriptor> argumentDescriptors = new ArrayList<ArgumentTransformationDescriptor>();

    @Override
    public MethodCall transform(TransformationMap map, Object input) throws TransformationException {
        if (!(input instanceof MethodCall)) {
            throw new IllegalArgumentException("Only MethodCall objects allowed as input.");
        }
        MethodCall inCall = (MethodCall) input;

        return new MethodCall(transformName(inCall), getNewArguments(inCall, map));
    }

    private Value[] getNewArguments(MethodCall inCall, TransformationMap map) throws TransformationException {
        Value[] result = new Value[argumentDescriptors.size()];
        for (int i = 0; i < result.length; i++) {
            ArgumentTransformationDescriptor descr = argumentDescriptors.get(i);
            result[i] = descr.getArgument(inCall, map);
        }
        return result;
    }

    @Override
    public Class<?> getTargetType(TransformationMap map, Object input) {
        return MethodCall.class;
    }

    protected abstract String transformName(MethodCall inCall);

    public void setArgumentDescriptors(List<ArgumentTransformationDescriptor> argumentDescriptors) {
        this.argumentDescriptors = argumentDescriptors;
    }

}
