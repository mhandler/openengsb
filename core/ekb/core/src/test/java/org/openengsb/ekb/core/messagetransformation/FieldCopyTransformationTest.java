package org.openengsb.ekb.core.messagetransformation;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.core.model.ConceptIRI;
import org.openengsb.ekb.core.messagetransformation.internal.FieldCopyTransfomation;
import org.openengsb.ekb.core.messagetransformation.internal.StringToIntTransformation;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.openengsb.ekb.core.messagetransformation.transformationstore.internal.InMemoryTransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class FieldCopyTransformationTest {

    private TestBeanA testBeanA;

    private TransformationMap map;

    @Before
    public void setUp() {
        testBeanA = new TestBeanA();
        map = new InMemoryTransformationMap();
    }

    @Test
    public void testDirectCopy() throws TransformationException {
        FieldCopyTransfomation<TestBeanB> t = getTransformation(TestBeanB.class);
        TestBeanB result = t.transform(map, testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), result.getFoo());
    }

    @Test
    public void testFieldNameMapping() throws TransformationException {
        FieldCopyTransfomation<TestBeanC> t = getTransformation(TestBeanC.class);
        t.setFieldNameMapping(createFieldNameMapping("foo", "bar"));
        TestBeanC result = t.transform(map, testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), result.getBar());
    }

    @Test(expected = TransformationException.class)
    public void testFieldNameMappingFailure() throws TransformationException {
        FieldCopyTransfomation<TestBeanC> t = getTransformation(TestBeanC.class);
        t.transform(map, testBeanA);
    }

    @Test
    public void testFieldTransformation() throws TransformationException {
        FieldCopyTransfomation<TestBeanD> t = getTransformation(TestBeanD.class);
        map.putTransformation(IRI.create("A_foo"), new StringToIntTransformation());
        TestBeanD result = t.transform(map, testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), String.valueOf(result.getFoo()));
    }

    @Test
    public void testFieldNameMappingAndTransformation() throws TransformationException {
        FieldCopyTransfomation<TestBeanE> t = getTransformation(TestBeanE.class);
        t.setFieldNameMapping(createFieldNameMapping("foo", "bar"));
        map.putTransformation(IRI.create("A_foo"), new StringToIntTransformation());
        TestBeanE result = t.transform(map, testBeanA);
        Assert.assertEquals(testBeanA.getFoo(), String.valueOf(result.getBar()));
    }

    private <T> FieldCopyTransfomation<T> getTransformation(Class<T> clazzToken) {
        FieldCopyTransfomation<T> t = new FieldCopyTransfomation<T>();
        t.setTargetClassToken(clazzToken);
        return t;
    }

    private Map<String, String> createFieldNameMapping(String key, String value) {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(key, value);
        return mapping;
    }

}

@ConceptIRI("A")
class TestBeanA {

    @ConceptIRI("A_foo")
    private String foo = "42";

    public String getFoo() {
        return foo;
    }
}

@ConceptIRI("B")
class TestBeanB {

    @ConceptIRI("B_foo")
    private String foo;

    public String getFoo() {
        return foo;
    }
}

@ConceptIRI("C")
class TestBeanC {

    @ConceptIRI("C_bar")
    private String bar;

    public String getBar() {
        return bar;
    }
}

@ConceptIRI("D")
class TestBeanD {

    @ConceptIRI("D_foo")
    private int foo;

    public int getFoo() {
        return foo;
    }
}

@ConceptIRI("E")
class TestBeanE {

    @ConceptIRI("E_bar")
    private int bar;

    public int getBar() {
        return bar;
    }
}
