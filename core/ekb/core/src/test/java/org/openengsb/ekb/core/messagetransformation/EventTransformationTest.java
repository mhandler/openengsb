package org.openengsb.ekb.core.messagetransformation;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.core.model.ConceptIRI;
import org.openengsb.core.model.Event;
import org.openengsb.ekb.core.messagetransformation.internal.EventTransformation;
import org.openengsb.ekb.core.messagetransformation.internal.FieldCopyTransfomation;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.openengsb.ekb.core.messagetransformation.transformationstore.internal.InMemoryTransformationMap;
import org.semanticweb.owlapi.model.IRI;

public class EventTransformationTest {

    private TestEventA testEventA;
    private TestEventB testEventB;
    private TestEventC testEventC;
    private TransformationMap map;
    private EventTransformation et;

    @Before
    public void setUp() {
        this.testEventA = new TestEventA();
        testEventA.init();
        this.testEventB = new TestEventB();
        testEventB.init();
        this.testEventC = new TestEventC();
        testEventC.init();
        map = new InMemoryTransformationMap();
        et = new EventTransformation() {

            @Override
            protected String transformToolConnector(Event inEvent) {
                return "toolConnectorTarget";
            }

            @Override
            protected String transformName(Event inEvent) {
                return "nameTarget";
            }

            @Override
            protected String transformDomain(Event inEvent) {
                return "domainTarget";
            }

            @Override
            protected Class<? extends Event> getTargetEventClass(Event inEvent) {
                return TestEventTarget.class;
            }
        };
    }

    @Test
    public void testSimpleEvent() throws TransformationException {
        Event result = et.transform(map, testEventA);
        checkResult(result);
    }

    @Test
    public void testEventWithPrimitives() throws TransformationException {
        et.setElementKeyMapping(getKeyMapping("foo", "bar"));
        Event result = et.transform(map, testEventB);
        checkResult(result);
        Assert.assertNotNull(result.getValue("bar"));
    }

    @Test(expected = IllegalStateException.class)
    public void testEventWithPrimitivesWithoutGetter() throws TransformationException {
        testEventA.setValue("foo", "test");
        et.transform(map, testEventA);
    }

    @Test
    public void testEventWithObjects() throws TransformationException {
        et.setElementKeyMapping(getKeyMapping("foo", "bar"));
        FieldCopyTransfomation<TestConcept2> transformation = new FieldCopyTransfomation<TestConcept2>();
        transformation.setTargetClassToken(TestConcept2.class);
        map.putTransformation(IRI.create("TestConcept"), transformation);
        Event result = et.transform(map, testEventC);
        checkResult(result);
        Assert.assertEquals(TestConcept2.class, result.getValue("bar").getClass());
    }

    private Map<String, String> getKeyMapping(String key, String value) {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(key, value);
        return mapping;
    }

    private void checkResult(Event result) {
        Assert.assertEquals("nameTarget", result.getName());
        Assert.assertEquals("domainTarget", result.getDomain());
        Assert.assertEquals("toolConnectorTarget", result.getToolConnector());
    }

}

@ConceptIRI("TestEventA")
class TestEventA extends Event {
    public TestEventA() {
        super("domainA", "nameA");
    }

    public void init() {
        setDomain("domainA");
        setName("nameA");
        setToolConnector("toolConnectorA");
    }
}

class TestEventTarget extends Event {
    public TestEventTarget() {
        super("", "");
    }

}

@ConceptIRI("TestEventB")
class TestEventB extends Event {
    public TestEventB() {
        super("", "");
    }

    public void init() {
        setDomain("domainB");
        setName("nameB");
        setToolConnector("toolConnectorB");
        setValue("foo", "someValue");
    }

    @ConceptIRI("B_foo")
    public String getFoo() {
        return (String) getValue("foo");
    }
}

@ConceptIRI("TestEventC")
class TestEventC extends Event {
    public TestEventC() {
        super("", "");
    }

    public void init() {
        setDomain("domainC");
        setName("nameC");
        setToolConnector("toolConnectorC");
        setValue("foo", new TestConcept());
    }

}

@ConceptIRI("TestConcept")
class TestConcept {

}

class TestConcept2 {

}