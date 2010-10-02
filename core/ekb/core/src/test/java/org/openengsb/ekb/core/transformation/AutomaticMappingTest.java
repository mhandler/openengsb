/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ekb.core.transformation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.TransformationException;
import org.openengsb.ekb.core.transformation.mappings.AutomaticMapping;

public class AutomaticMappingTest {

    private AutomaticMapping automaticMapping;

    @Before
    public void setUp() {
        automaticMapping = new AutomaticMapping();
        automaticMapping.setSourceField("source");
        automaticMapping.setTargetField("target");
    }

    @Test
    public void testNull() throws TransformationException {
        String result = automaticMapping.transform(null, String.class);
        Assert.assertNull(result);
    }

    @Test
    public void testIdentity() throws TransformationException {
        String result = automaticMapping.transform("blub", String.class);
        Assert.assertEquals("blub", result);
    }

    @Test
    public void testCast() throws TransformationException {
        TestObject result = automaticMapping.transform(new TestSubObject(), TestObject.class);
        Assert.assertTrue(TestObject.class.isAssignableFrom(result.getClass()));
    }

    @Test
    public void testToString() throws TransformationException {
        String result = automaticMapping.transform(new TestObject(), String.class);
        Assert.assertEquals("testObject", result);
    }

    @Test
    public void testParseToPrimitive() throws TransformationException {
        Integer result = automaticMapping.transform(new Integer(3).toString(), Integer.class);
        Assert.assertEquals(new Integer(3), result);

        Boolean result2 = automaticMapping.transform(Boolean.TRUE.toString(), Boolean.class);
        Assert.assertEquals(Boolean.TRUE, result2);

        Short result3 = automaticMapping.transform("3", Short.class);
        Assert.assertEquals(new Short("3"), result3);

        Long result4 = automaticMapping.transform(new Long(3).toString(), Long.class);
        Assert.assertEquals(new Long(3), result4);

        Character result5 = automaticMapping.transform("a", Character.class);
        Assert.assertEquals(new Character('a'), result5);

        Float result6 = automaticMapping.transform("4.2", Float.class);
        Assert.assertEquals(new Float("4.2"), result6);

        Double result7 = automaticMapping.transform("4.2", Double.class);
        Assert.assertEquals(new Double("4.2"), result7);
    }

    @Test
    public void testParseToBoolean() throws TransformationException {
        Boolean result = automaticMapping.transform(new Integer(3), Boolean.class);
        Assert.assertEquals(Boolean.TRUE, result);

        result = automaticMapping.transform(new Integer(-1), Boolean.class);
        Assert.assertEquals(Boolean.FALSE, result);

        result = automaticMapping.transform(new Character('t'), Boolean.class);
        Assert.assertEquals(Boolean.TRUE, result);

        result = automaticMapping.transform(new Character('f'), Boolean.class);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testParseToInteger() throws TransformationException {
        Integer result = automaticMapping.transform(Boolean.FALSE, Integer.class);
        Assert.assertEquals(new Integer(0), result);

        result = automaticMapping.transform(Boolean.TRUE, Integer.class);
        Assert.assertEquals(new Integer(1), result);

        result = automaticMapping.transform(new Character('1'), Integer.class);
        Assert.assertEquals(new Integer(1), result);

        result = automaticMapping.transform(new Double(2.5d), Integer.class);
        Assert.assertEquals(new Integer(new Double(2.5d).intValue()), result);
    }

    @Test
    public void testParseToCharacter() throws TransformationException {
        Character result = automaticMapping.transform(Boolean.FALSE, Character.class);
        Assert.assertEquals(new Character('F'), result);

        result = automaticMapping.transform(Boolean.TRUE, Character.class);
        Assert.assertEquals(new Character('T'), result);

        result = automaticMapping.transform(new Integer(5), Character.class);
        Assert.assertEquals(new Character('5'), result);
    }

    public static class TestObject {

        @Override
        public String toString() {
            return "testObject";
        }

    }

    public static class TestSubObject extends TestObject {

    }

}
