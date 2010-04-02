package org.openengsb.ekb.core.messagetransformation.internal;

import java.util.ArrayList;
import java.util.List;

import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;

public abstract class MethodCallTransformation implements Transformation {

    @Override
    public MethodCall transform(TransformationMap map, Object input) throws TransformationException {
        if (!(input instanceof MethodCall)) {
            throw new IllegalArgumentException("Only MethodCall objects allowed as input.");
        }
        MethodCall inCall = (MethodCall) input;

        Value[] dropped = dropArguments(inCall, inCall.getArguments());
        Value[] transformed = transformArguments(inCall, map, dropped);
        Value[] added = addNewArguments(inCall, transformed);

        return new MethodCall(transformName(inCall), added);
    }

    private Value[] dropArguments(MethodCall inCall, Value[] args) {
        List<Value> result = new ArrayList<Value>();
        for (int i = 0; i < args.length; i++) {
            if (!dropArgument(i, inCall)) {
                result.add(args[i]);
            }
        }
        return result.toArray(new Value[result.size()]);
    }

    protected abstract boolean dropArgument(int pos, MethodCall inCall);

    private Value[] transformArguments(MethodCall inCall, TransformationMap map, Value[] args) {
        for (int i = 0; i < args.length; i++) {
            Value arg = args[i];

        }
        return null;
    }

    private Value[] addNewArguments(MethodCall inCall, Value[] transformed) {
        // TODO Auto-generated method stub
        return null;
    }

    protected abstract String transformName(MethodCall inCall);

}
