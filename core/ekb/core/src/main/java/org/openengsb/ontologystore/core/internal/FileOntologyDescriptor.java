package org.openengsb.ontologystore.core.internal;

import java.io.File;

import org.openengsb.ontologystore.core.OntologyDescriptor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class FileOntologyDescriptor extends OntologyDescriptor {

    private File ontologyFile;

    FileOntologyDescriptor(OWLOntologyID ontologyId, File ontologyFile) {
        super(ontologyId.getOntologyIRI(), ontologyId.getVersionIRI());
        this.ontologyFile = ontologyFile;
    }

    @Override
    public OWLOntology getOntology() {
        // load OWLOntology from file
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            return manager.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }

}
