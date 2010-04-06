package org.openengsb.ekb.core.messagetransformation;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.core.model.MethodCall;
import org.openengsb.core.model.Value;
import org.openengsb.ekb.core.messagetransformation.internal.ArgumentTransformationDescriptor;
import org.openengsb.ekb.core.messagetransformation.internal.DerivedArgumentTransformationDescriptor;
import org.openengsb.ekb.core.messagetransformation.internal.MethodCallTransformation;
import org.openengsb.ekb.core.messagetransformation.internal.NewValueArgumentTransformationDescriptor;
import org.openengsb.ekb.core.messagetransformation.internal.StringToIntegerTransformation;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.openengsb.ekb.core.messagetransformation.transformationstore.internal.InMemoryTransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class MethodCallTransformationTest {

    private TransformationMap transformationMap;

    private MethodCallTransformation methodCallTransformation;

    @Before
    public void setUp() {
        transformationMap = new InMemoryTransformationMap();
        methodCallTransformation = new MethodCallTransformation() {

            @Override
            public IRI getTargetConceptIRI(TransformationMap map, Object input) {
                return IRI.create("targetMethodConcept");
            }

            @Override
            protected String transformName(MethodCall inCall) {
                return "targetMethod";
            }
        };
    }

    @Test
    public void testTransformMethodName() throws TransformationException {
        MethodCall inCall = getInCall();
        MethodCall outCall = methodCallTransformation.transform(transformationMap, inCall);
        check(outCall, new Value[0]);
    }

    @Test
    public void testDropArguments() throws TransformationException {
        Value value42 = new Value("42", String.class, "42Concept");
        MethodCall inCall = getInCall(new Value("foo", String.class, "fooConcept"), new Value("bar", String.class,
                "barConcept"), value42);
        methodCallTransformation
                .setArgumentDescriptors(createDescriptors(new DerivedArgumentTransformationDescriptor(2)));
        MethodCall outCall = methodCallTransformation.transform(transformationMap, inCall);
        check(outCall, new Value[] { value42 });
    }

    @Test
    public void testTransformArguments() throws TransformationException {
        Value value42 = new Value("42", String.class, "42Concept");
        MethodCall inCall = getInCall(value42);
        transformationMap.putTransformation(IRI.create("42Concept"), new StringToIntegerTransformation(IRI
                .create("42AsIntegerConcept")));
        methodCallTransformation
                .setArgumentDescriptors(createDescriptors(new DerivedArgumentTransformationDescriptor(0)));
        MethodCall outCall = methodCallTransformation.transform(transformationMap, inCall);
        check(outCall, new Value[] { new Value(42, Integer.class, "42AsIntegerConcept") });
    }

    @Test
    public void testAddArguments() throws TransformationException {
        MethodCall inCall = getInCall();
        Value newValue = new Value("foo", String.class, "fooConcept");
        methodCallTransformation.setArgumentDescriptors(createDescriptors(new NewValueArgumentTransformationDescriptor(
                newValue)));
        MethodCall outCall = methodCallTransformation.transform(transformationMap, inCall);
        check(outCall, new Value[] { newValue });
    }

    @Test
    public void testDropTransformAddArguments() throws TransformationException {
        Value argToAdd = new Value("foo", String.class, "fooConcept");
        MethodCall inCall = getInCall(new Value("42", String.class, "42Concept"), new Value("bar", String.class,
                "barConcept"));
        transformationMap.putTransformation(IRI.create("42Concept"), new StringToIntegerTransformation(IRI
                .create("42AsIntegerConcept")));
        methodCallTransformation.setArgumentDescriptors(createDescriptors(new NewValueArgumentTransformationDescriptor(
                argToAdd), new DerivedArgumentTransformationDescriptor(0)));
        MethodCall outCall = methodCallTransformation.transform(transformationMap, inCall);
        check(outCall, new Value[] { argToAdd, new Value(42, Integer.class, "42AsIntegerConcept") });
    }

    private void check(MethodCall outCall, Value[] expectedArguments) {
        Assert.assertEquals(outCall.getMethodName(), "targetMethod");
        Assert.assertTrue(Arrays.equals(expectedArguments, outCall.getArguments()));
    }

    private MethodCall getInCall(Value... arguments) {
        return new MethodCall("sourceMethod", arguments);
    }

    private List<ArgumentTransformationDescriptor> createDescriptors(ArgumentTransformationDescriptor... descriptors) {
        return Arrays.asList(descriptors);
    }

}
