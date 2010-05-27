package org.openengsb.ekb.core.endpointmanagement;

import java.util.List;

import org.openengsb.ekb.api.ConceptSource;

public interface EndpointManager {

    void activate(ConceptSource source);

    void deactivate(ConceptSource source);

    List<ConceptSource> getActiveConceptSources();

}
