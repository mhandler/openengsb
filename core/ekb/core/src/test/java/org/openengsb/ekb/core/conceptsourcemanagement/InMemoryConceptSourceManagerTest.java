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
package org.openengsb.ekb.core.conceptsourcemanagement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.core.AbstractConceptSource;

public class InMemoryConceptSourceManagerTest implements ConceptSourceStatusListener {

    private boolean notified;

    private InMemoryConceptSourceManager conceptSourceManager;

    @Before
    public void setUp() {
        Set<ConceptSourceStatusListener> listener = new HashSet<ConceptSourceStatusListener>();
        listener.add(this);
        conceptSourceManager = new InMemoryConceptSourceManager(listener);
        notified = false;
    }

    @Test
    public void testActivateSource() {
        MockConceptSource source = new MockConceptSource();
        conceptSourceManager.activate(source);
        Assert.assertTrue(notified);
        List<ConceptSource> activeSources = conceptSourceManager.getActiveConceptSources();
        Assert.assertEquals(1, activeSources.size());
        Assert.assertEquals(source, activeSources.get(0));
    }

    @Test
    public void testDeactivateSource() {
        MockConceptSource source = new MockConceptSource();
        conceptSourceManager.activate(source);
        notified = false;
        conceptSourceManager.deactivate(source);
        Assert.assertTrue(notified);

        List<ConceptSource> activeSources = conceptSourceManager.getActiveConceptSources();
        Assert.assertEquals(0, activeSources.size());
    }

    @Override
    public void activated(ConceptSource source) {
        notified = true;
    }

    @Override
    public void deactivated(ConceptSource source) {
        notified = true;
    }

    private class MockConceptSource extends AbstractConceptSource {

        public MockConceptSource() {
            super.setId("mock");
        }

        @Override
        public boolean canProvide(Concept<?> concept) {
            return false;
        }

    }

}
