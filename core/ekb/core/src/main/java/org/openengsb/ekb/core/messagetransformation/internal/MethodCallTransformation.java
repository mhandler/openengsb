package org.openengsb.ekb.core.messagetransformation.internal;

import java.util.ArrayList;
import java.util.List;

import org.openengsb.core.model.MethodCall;
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

        Argument args[] = copyArgs(inCall);
        Argument[] dropped = dropArguments(inCall, args);
        Argument[] transformed = transformArguments(inCall, map, dropped);
        Argument[] added = addNewArguments(inCall, transformed);

        return createMethodCall(transformName(inCall), added);
    }

    private Argument[] dropArguments(MethodCall inCall, Argument[] args) {
        List<Argument> result = new ArrayList<Argument>();
        for (int i = 0; i < args.length; i++) {
            if (!dropArgument(i, inCall)) {
                result.add(args[i]);
            }
        }
        return result.toArray(new Argument[result.size()]);
    }

    protected abstract boolean dropArgument(int pos, MethodCall inCall);

    private Argument[] transformArguments(MethodCall inCall, TransformationMap map, Argument[] args) {
        for (int i = 0; i < args.length; i++) {
            Argument arg = args[i];

        }
        return null;
    }

    private Argument[] addNewArguments(MethodCall inCall, Argument[] transformed) {
        // TODO Auto-generated method stub
        return null;
    }

    private MethodCall createMethodCall(String methodName, Argument[] arguments) {
        Object[] args = new Object[arguments.length];
        Class<?>[] types = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].value;
            types[i] = arguments[i].type;
        }
        return new MethodCall(methodName, args, types);
    }

    private Argument[] copyArgs(MethodCall inCall) {
        Argument[] args = new Argument[inCall.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            args[i] = new Argument(inCall.getArgs()[i], inCall.getTypes()[i]);
        }
        return args;
    }

    protected abstract String transformName(MethodCall inCall);

    private class Argument {
        private Object value;
        private Class<?> type;

        public Argument(Object value, Class<?> type) {
            this.value = value;
            this.type = type;
        }
    }
}
