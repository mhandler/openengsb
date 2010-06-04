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
package org.openengsb.ekb.analyzer;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.core.ConceptImpl;

public class ConceptCacheTest implements ConceptCacheListener {

    private boolean notified = false;

    private ConceptCache cache;

    private String id;

    @Before
    public void setUp() {
        this.notified = false;
        this.cache = new ConceptCache();
        cache.addListener(this);
        this.id = UUID.randomUUID().toString();
    }

    @Test
    public void testStoreAndGet() {
        ConceptImpl<SomeConcept> concept = getConcept();
        Assert.assertFalse(notified);
        cache.storeConcept(concept);
        Assert.assertTrue(notified);
        ConceptImpl<?> cached = cache.getConcept(id);
        Assert.assertEquals(concept, cached);
    }

    private ConceptImpl<SomeConcept> getConcept() {
        ConceptImpl<SomeConcept> concept = new ConceptImpl<SomeConcept>();
        concept.setKey(new ConceptKey(id, UUID.randomUUID().toString()));
        return concept;
    }

    @Override
    public void conceptStored(Concept<?> concept) {
        if (concept.getKey().getId().equals(id)) {
            notified = true;
        }
    }

    private class SomeConcept {
    }

}
