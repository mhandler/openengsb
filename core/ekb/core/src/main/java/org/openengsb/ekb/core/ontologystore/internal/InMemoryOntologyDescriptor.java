package org.openengsb.ekb.core.ontologystore.internal;

import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;
import org.semanticweb.owlapi.model.OWLOntology;

public class InMemoryOntologyDescriptor extends OntologyDescriptor {

    private OWLOntology ontology;

    InMemoryOntologyDescriptor(OWLOntology ontology) {
        super(ontology.getOntologyID().getOntologyIRI(), ontology.getOntologyID().getVersionIRI());
        this.ontology = ontology;
    }

    @Override
    public OWLOntology getOntology() {
        return this.ontology;
    }

}
