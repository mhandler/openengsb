package org.openengsb.core.model;

public abstract class Concept {

    private String conceptIRI;

    public Concept(String conceptIRI) {
        this.conceptIRI = conceptIRI;
    }

    public String getConceptIRI() {
        return conceptIRI;
    }

}
