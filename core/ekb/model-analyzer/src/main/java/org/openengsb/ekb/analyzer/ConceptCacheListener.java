package org.openengsb.ekb.analyzer;

import org.openengsb.ekb.api.Concept;

public interface ConceptCacheListener {

    void conceptStored(Concept<?> concept);

}
