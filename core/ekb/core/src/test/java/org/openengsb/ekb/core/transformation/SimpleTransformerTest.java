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
import org.openengsb.ekb.core.ConceptImpl;
import org.openengsb.ekb.core.transformation.mappings.IdentityMapping;

public class SimpleTransformerTest {

    private SimpleTransformer transformer;

    @Before
    public void setUp() {
        this.transformer = new SimpleTransformer();
    }

    @Test
    public void testConcept() throws TransformationException {
        ConceptImpl<Target> target = getTarget();
        ConceptImpl<Source> source = getSource(target);
        Source input = new Source();
        Target result = transformer.transform(source, target, input);
        Assert.assertEquals(Target.class, result.getClass());
    }

    @Test
    public void testConceptWithIdentityMappings() throws TransformationException {
        ConceptImpl<Target> target = getTarget();
        ConceptImpl<Source> source = getSource(target);

        IdentityMapping stringMapping = new IdentityMapping();
        stringMapping.setSourceField("someString");
        stringMapping.setTargetField("targetString");
        source.addFieldMapping(stringMapping);

        IdentityMapping intMapping = new IdentityMapping();
        intMapping.setSourceField("someInt");
        intMapping.setTargetField("targetInt");
        source.addFieldMapping(intMapping);

        Source input = new Source();

        Target result = transformer.transform(source, target, input);

        Assert.assertEquals(input.someString, result.getTargetString());
        Assert.assertEquals(input.someInt, result.getTargetInt());
    }

    private ConceptImpl<Target> getTarget() {
        ConceptImpl<Target> target = new ConceptImpl<Target>();
        target.setId("target");
        target.setConceptClass(Target.class);
        return target;
    }

    private ConceptImpl<Source> getSource(ConceptImpl<Target> target) {
        ConceptImpl<Source> source = new ConceptImpl<Source>();
        source.setConceptClass(Source.class);
        source.setId("source");
        source.setSuperConcept(target);
        return source;
    }

    private class Source {
        private String someString = "foo";
        private Integer someInt = new Integer(42);
    }

}

class Target {
    private String targetString;
    private Integer targetInt;

    public Integer getTargetInt() {
        return targetInt;
    }

    public String getTargetString() {
        return targetString;
    }
}