package org.openengsb.ekb.core.endpointmanagement;

import java.util.List;

import org.openengsb.ekb.api.ConceptSource;

public interface EndpointManager {

    void activated(ConceptSource source);

    void deActivated(ConceptSource source);

    List<ConceptSource> getActiveConceptSources();

}
