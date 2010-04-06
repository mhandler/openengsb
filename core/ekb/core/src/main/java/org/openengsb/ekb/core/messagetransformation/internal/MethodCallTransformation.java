package org.openengsb.ekb.core.messagetransformation.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

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

    private Value[] transformArguments(MethodCall inCall, TransformationMap map, Value[] args)
            throws TransformationException {
        Value[] result = new Value[args.length];
        for (int i = 0; i < args.length; i++) {
            Value arg = args[i];

            Object transformed = transform(map, arg.getConceptIRI(), arg.getValue());
            String targetConceptIRI = getTargetConceptIRI(map, arg.getConceptIRI(), arg.getValue());
            Class<?> targetType = getTargetType(map, arg.getConceptIRI(), arg.getValue(), arg.getType());
            result[i] = new Value(transformed, targetType, targetConceptIRI);
        }
        return result;
    }

    private Object transform(TransformationMap map, String conceptIRI, Object value) throws TransformationException {
        Transformation transformation = map.getTransformation(IRI.create(conceptIRI));
        if (transformation == null) {
            return value;
        }
        return transformation.transform(map, value);
    }

    private String getTargetConceptIRI(TransformationMap map, String conceptIRI, Object value)
            throws TransformationException {
        Transformation transformation = map.getTransformation(IRI.create(conceptIRI));
        if (transformation == null) {
            return conceptIRI;
        }
        return transformation.getTargetConceptIRI(map, value).toString();
    }

    private Class<?> getTargetType(TransformationMap map, String conceptIRI, Object value, Class<?> sourceType)
            throws TransformationException {
        Transformation transformation = map.getTransformation(IRI.create(conceptIRI));
        if (transformation == null) {
            return sourceType;
        }
        return transformation.getTargetType(map, value);
    }

    private Value[] addNewArguments(MethodCall inCall, Value[] transformed) {
        List<Value> result = new ArrayList<Value>();
        for (int i = 0; i < transformed.length; i++) {
            result.addAll(Arrays.asList(addArgumentsAtIndex(i, inCall)));
            result.add(transformed[i]);
        }
        return result.toArray(new Value[result.size()]);
    }

    protected abstract Value[] addArgumentsAtIndex(int index, MethodCall inCall);

    protected abstract String transformName(MethodCall inCall);

}
