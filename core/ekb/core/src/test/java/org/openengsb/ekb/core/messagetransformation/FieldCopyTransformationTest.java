package org.openengsb.ekb.core.messagetransformation;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.core.messagetransformation.internal.FieldCopyTransfomation;
import org.openengsb.ekb.core.messagetransformation.internal.StringToIntTransformation;

public class FieldCopyTransformationTest {

    private TestBeanA testBeanA;

    @Before
    public void setUp() {
        testBeanA = new TestBeanA();
    }

    @Test
    public void testDirectCopy() throws TransformationException {
        FieldCopyTransfomation<TestBeanB> t = getTransformation(TestBeanB.class);
        TestBeanB result = t.transform(testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), result.getFoo());
    }

    @Test
    public void testFieldNameMapping() throws TransformationException {
        FieldCopyTransfomation<TestBeanC> t = getTransformation(TestBeanC.class);
        t.setFieldNameMapping(createFieldNameMapping("foo", "bar"));
        TestBeanC result = t.transform(testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), result.getBar());
    }

    @Test(expected = TransformationException.class)
    public void testFieldNameMappingFailure() throws TransformationException {
        FieldCopyTransfomation<TestBeanC> t = getTransformation(TestBeanC.class);
        t.transform(testBeanA);
    }

    @Test
    public void testFieldTransformation() throws TransformationException {
        FieldCopyTransfomation<TestBeanD> t = getTransformation(TestBeanD.class);
        t.setFieldTransformators(createTransfomations("foo", new StringToIntTransformation()));
        TestBeanD result = t.transform(testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), String.valueOf(result.getFoo()));
    }

    @Test
    public void testFieldNameMappingAndTransformation() throws TransformationException {
        FieldCopyTransfomation<TestBeanE> t = getTransformation(TestBeanE.class);
        t.setFieldNameMapping(createFieldNameMapping("foo", "bar"));
        t.setFieldTransformators(createTransfomations("foo", new StringToIntTransformation()));
        TestBeanE result = t.transform(testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), String.valueOf(result.getBar()));

    }

    private <T> FieldCopyTransfomation<T> getTransformation(Class<T> clazzToken) {
        FieldCopyTransfomation<T> t = new FieldCopyTransfomation<T>();
        t.setTargetClassToken(clazzToken);
        return t;
    }

    private Map<String, Transformation> createTransfomations(String key, Transformation transformation) {
        Map<String, Transformation> transformations = new HashMap<String, Transformation>();
        transformations.put(key, transformation);
        return transformations;
    }

    private Map<String, String> createFieldNameMapping(String key, String value) {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(key, value);
        return mapping;
    }

}

class TestBeanA {
    private String foo = "42";

    public String getFoo() {
        return foo;
    }
}

class TestBeanB {

    private String foo;

    public String getFoo() {
        return foo;
    }
}

class TestBeanC {
    private String bar;

    public String getBar() {
        return bar;
    }
}

class TestBeanD {
    private int foo;

    public int getFoo() {
        return foo;
    }
}

class TestBeanE {
    private int bar;

    public int getBar() {
        return bar;
    }
}
