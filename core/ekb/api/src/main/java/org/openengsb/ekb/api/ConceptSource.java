package org.openengsb.ekb.api;

public class ConceptSource {

    private String id;

    private String service;

    private String urn;

    public String getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public String getUrn() {
        return urn;
    }

    public boolean canProvide(Concept<?> concept) {
        if (concept.getModelPartId().equals(id)) {
            return true;
        }
        for (Concept<?> subConcept : concept.getSubConcepts()) {
            if (canProvide(subConcept)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return id;
    }

}
