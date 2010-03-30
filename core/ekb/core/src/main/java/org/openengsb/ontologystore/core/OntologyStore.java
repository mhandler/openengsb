package org.openengsb.ekb.core.ontologystore;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

public interface OntologyStore {

    void addOntology(OWLOntology ontology) throws OntologyStoreException;

    void deleteOntology(OntologyDescriptor ontology) throws OntologyStoreException;

    Set<OntologyDescriptor> getOntologyVersions(IRI ontologyIRI);

    OntologyDescriptor getOntology(IRI ontologyIRI, IRI versionIRI);

}
