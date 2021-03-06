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
package org.openengsb.ekb.core.knowledgemanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.core.ConceptImpl;
import org.openengsb.ekb.core.SimpleConceptSource;

public class InMemoryKnowledgeManagerTest {

    private InMemoryKnowledgeManager manager;

    @Before
    public void setUp() {
        manager = new InMemoryKnowledgeManager();
    }

    @Test
    public void testSetConcepts() {
        Assert.assertEquals(0, manager.getActiveConcepts().size());
        Assert.assertEquals(0, manager.getInactiveConcepts().size());
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);

        Assert.assertEquals(0, manager.getActiveConcepts().size());
        Assert.assertEquals(concepts.size(), manager.getInactiveConcepts().size());
        for (Concept<?> concept : concepts) {
            Assert.assertTrue(manager.getInactiveConcepts().contains(concept));
        }
    }

    @Test
    public void testActivateAllConcepts() {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.activateConcepts(concepts);

        Assert.assertEquals(concepts.size(), manager.getActiveConcepts().size());
        for (Concept<?> concept : concepts) {
            Assert.assertTrue(manager.getActiveConcepts().contains(concept));
        }
    }

    @Test
    public void testActivateSingleConcept() {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.activateConcepts(concepts.subList(0, 1));

        Assert.assertEquals(1, manager.getActiveConcepts().size());
        Assert.assertTrue(manager.getActiveConcepts().contains(concepts.get(0)));
    }

    @Test
    public void testDeactivateAllConcepts() {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.activateConcepts(concepts);
        manager.deactivateConcepts(concepts);

        Assert.assertEquals(concepts.size(), manager.getInactiveConcepts().size());
        for (Concept<?> concept : concepts) {
            Assert.assertTrue(manager.getInactiveConcepts().contains(concept));
        }
    }

    @Test
    public void testDeactivateSingleConcept() {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.activateConcepts(concepts);
        manager.deactivateConcepts(concepts.subList(1, 2));

        Assert.assertEquals(1, manager.getInactiveConcepts().size());
        Assert.assertTrue(manager.getInactiveConcepts().contains(concepts.get(1)));
    }

    @Test(expected = NoSuchConceptException.class)
    public void testGetConceptByIdFail() throws NoSuchConceptException {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.getActiveConcept(new ConceptKey("42", "1.0.0"));
    }

    @Test
    public void testGetConceptById() throws NoSuchConceptException {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.activateConcepts(concepts);
        manager.getActiveConcept(new ConceptKey("42", "1.0.0"));
        manager.getActiveConcept(new ConceptKey("foo", "1.0.0"));
        manager.getActiveConcept(new ConceptKey("bar", "1.0.0"));
        manager.getActiveConcept(new ConceptKey("buz", "1.0.0"));
    }

    @Test
    public void testGetActiveConceptsBySource() {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        manager.activateConcepts(concepts.subList(0, 1));
        List<Concept<?>> activeConcepts = manager.getActiveConcepts(new MockConceptSource(getList("42", "foo")));
        Assert.assertEquals(1, activeConcepts.size());
        Assert.assertTrue(activeConcepts.contains(concepts.get(0)));

    }

    @Test
    public void testGetInactiveConceptsBySource() {
        List<Concept<?>> concepts = createConcepts();
        manager.setConcepts(concepts);
        List<Concept<?>> inactiveConcepts = manager.getInactiveConcepts(new MockConceptSource(getList("42", "foo")));
        Assert.assertEquals(2, inactiveConcepts.size());
        Assert.assertTrue(inactiveConcepts.contains(concepts.get(0)));
        Assert.assertTrue(inactiveConcepts.contains(concepts.get(1)));
    }

    private List<String> getList(String... strings) {
        return Arrays.asList(strings);
    }

    private List<Concept<?>> createConcepts() {
        List<Concept<?>> concepts = new ArrayList<Concept<?>>();
        concepts.add(createConcept("42"));
        concepts.add(createConcept("foo"));
        concepts.add(createConcept("bar"));
        concepts.add(createConcept("buz"));
        return concepts;
    }

    private Concept<?> createConcept(String id) {
        ConceptImpl<SomeConcept> concept = new ConceptImpl<SomeConcept>();
        concept.setConceptClass(SomeConcept.class);
        concept.setKey(new ConceptKey(id, "1.0.0"));
        return concept;
    }

    private class SomeConcept {

    }

    private class MockConceptSource extends SimpleConceptSource {

        private List<String> ids;

        public MockConceptSource(List<String> ids) {
            this.ids = ids;
        }

        @Override
        public boolean canProvide(Concept<?> concept) {
            return ids.contains(concept.getKey().getId());
        }
    }
}
