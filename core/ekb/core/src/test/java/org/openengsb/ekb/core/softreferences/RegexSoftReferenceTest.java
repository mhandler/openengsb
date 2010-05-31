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
package org.openengsb.ekb.core.softreferences;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.core.AbstractConceptSource;
import org.openengsb.ekb.core.ConceptImpl;

public class RegexSoftReferenceTest {

    private RegexSoftReference<Source, Target> ref;

    private Target target = new Target("42");

    private Target target2 = new Target("43");

    private Target target3 = new Target("44");

    private EKB ekb;

    @Before
    public void setUp() {
        ekb = new MockEKB();
        ref = new RegexSoftReference<Source, Target>();
        ref.setReferenceField("refField");
        ref.setRegex("#ref\\(([\\d]+)\\)");
        ref.setSourceConcept(getSourceConcept());
        ref.setTargetConcept(getTargetConcept());
    }

    @Test
    public void testFollowSingleRefOnlyRef() {
        Source source = new Source("#ref(42)");
        List<Target> targetList = ref.follow(ekb, source);
        Assert.assertEquals(1, targetList.size());
        Assert.assertEquals(target, targetList.get(0));
    }

    @Test
    public void testFollowSingleRefPrefix() {
        Source source = new Source("#ref(42) some Text");
        List<Target> targetList = ref.follow(ekb, source);
        Assert.assertEquals(1, targetList.size());
        Assert.assertEquals(target, targetList.get(0));
    }

    @Test
    public void testFollowSingleRefPostfix() {
        Source source = new Source("some Text #ref(42)");
        List<Target> targetList = ref.follow(ekb, source);
        Assert.assertEquals(1, targetList.size());
        Assert.assertEquals(target, targetList.get(0));
    }

    @Test
    public void testFollowSingleRefInfix() {
        Source source = new Source("some Text #ref(42) some Text");
        List<Target> targetList = ref.follow(ekb, source);
        Assert.assertEquals(1, targetList.size());
        Assert.assertEquals(target, targetList.get(0));
    }

    @Test
    public void testFollowMultiRef() {
        Source source = new Source("some Text #ref(42) some Text #ref(43) #ref(44)");
        List<Target> targetList = ref.follow(ekb, source);
        Assert.assertEquals(3, targetList.size());
        Assert.assertEquals(target, targetList.get(0));
        Assert.assertEquals(target2, targetList.get(1));
        Assert.assertEquals(target3, targetList.get(2));
    }

    private ConceptImpl<Source> getSourceConcept() {
        ConceptImpl<Source> source = new ConceptImpl<Source>();
        source.setConceptClass(Source.class);
        source.setId("source");
        source.addSoftReference(ref);
        return source;
    }

    private ConceptImpl<Target> getTargetConcept() {
        ConceptImpl<Target> target = new ConceptImpl<Target>();
        target.setConceptClass(Target.class);
        target.setId("target");
        return target;
    }

    private class Source {
        @SuppressWarnings("unused")
        private String refField;

        public Source(String refField) {
            this.refField = refField;
        }

    }

    private class Target {

        private String id;

        public Target(String id) {
            this.id = id;
        }
    }

    private class MockEKB implements EKB {

        @Override
        public List<Concept<?>> getAllConcepts() {
            return null;
        }

        @Override
        public Concept<?> getConcept(String id) throws NoSuchConceptException {
            return null;
        }

        @Override
        public <TYPE> Concept<TYPE> getConcept(String id, Class<TYPE> conceptClass) throws NoSuchConceptException {
            return null;
        }

        @Override
        public <TYPE> List<TYPE> getData(ConceptSource source, Concept<TYPE> concept) {
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <TYPE> TYPE getDataElement(ConceptSource source, Concept<TYPE> concept, String key) {
            if (key.equals(target.id)) {
                return (TYPE) target;
            } else if (key.equals(target2.id)) {
                return (TYPE) target2;
            } else if (key.equals(target3.id)) {
                return (TYPE) target3;
            }
            return null;
        }

        @Override
        public List<ConceptSource> getSources(Concept<?> concept) {
            ConceptSource source = new MockSource();
            return Collections.singletonList(source);
        }
    }

    private class MockSource extends AbstractConceptSource {

        @Override
        public boolean canProvide(Concept<?> concept) {
            return concept.getId().equals("target");
        }

    }

}
