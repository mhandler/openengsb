package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class DerivedArgumentTransformationDescriptor implements ArgumentTransformationDescriptor {

    private final int sourceMethodCallArgumentIndex;

    public DerivedArgumentTransformationDescriptor(int sourceMethodCallArgumentIndex) {
        this.sourceMethodCallArgumentIndex = sourceMethodCallArgumentIndex;
    }

    @Override
    public Value getArgument(MethodCall inCall, TransformationMap map) throws TransformationException {
        Value sourceArg = inCall.getArguments()[sourceMethodCallArgumentIndex];
        Transformation transformation = map.getTransformation(IRI.create(sourceArg.getConceptIRI()));
        if (transformation == null) {
            return sourceArg;
        }
        String targetConceptIRI = transformation.getTargetConceptIRI(map, sourceArg.getValue()).toString();
        Class<?> targetType = transformation.getTargetType(map, sourceArg.getValue());
        Object targetValue = transformation.transform(map, sourceArg.getValue());
        return new Value(targetValue, targetType, targetConceptIRI);
    }

}
