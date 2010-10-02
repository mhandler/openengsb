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
package org.openengsb.ekb.core;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.SimpleConceptSource;

public class ConceptSourceTest {

    private ConceptSource source;

    @Before
    public void setUp() {
        this.source = new TestSource();
    }

    @Test
    public void testCanProvideSubconcept() {
        ConceptImpl<String> concept = getConcept("conceptA-1");
        ConceptImpl<String> superConcept = getConcept("conceptB-1");
        concept.setSuperConcept(superConcept);

        Assert.assertTrue(source.canProvide(concept));
        Assert.assertFalse(source.canProvide(superConcept));
        Assert.assertTrue(source.canProvideSubconcept(superConcept));
        Assert.assertFalse(source.canProvideSubconcept(concept));
    }

    @Test
    public void testCanProvideSubSubconcept() {
        ConceptImpl<String> concept = getConcept("conceptA-1");

        ConceptImpl<String> superConcept = getConcept("conceptB-1");
        concept.setSuperConcept(superConcept);

        ConceptImpl<String> superSuperConcept = getConcept("conceptC-1");
        superConcept.setSuperConcept(superSuperConcept);

        Assert.assertTrue(source.canProvideSubconcept(superSuperConcept));
        Assert.assertTrue(source.canProvideSubconcept(superConcept));
        Assert.assertFalse(source.canProvideSubconcept(concept));
    }

    private ConceptImpl<String> getConcept(String id) {
        ConceptImpl<String> concept = new ConceptImpl<String>();
        concept.setKey(new ConceptKey(id, UUID.randomUUID().toString()));
        return concept;
    }

    private class TestSource extends SimpleConceptSource {

        @Override
        public boolean canProvide(Concept<?> concept) {
            return concept.getKey().getId().startsWith("conceptA-");
        }

    }

}
