package org.openengsb.ontologystore.core.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openengsb.ontologystore.core.OntologyDescriptor;
import org.openengsb.ontologystore.core.OntologyStore;
import org.openengsb.ontologystore.core.OntologyStoreException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

public class InMemoryOntologyStore implements OntologyStore {

    private Map<IRI, Set<OntologyDescriptor>> ontologies = new HashMap<IRI, Set<OntologyDescriptor>>();

    @Override
    public void addOntology(OWLOntology ontology) throws OntologyStoreException {
        Set<OntologyDescriptor> set = ontologies.get(ontology.getOntologyID().getOntologyIRI());

        if (set == null) {
            set = new HashSet<OntologyDescriptor>();
            ontologies.put(ontology.getOntologyID().getOntologyIRI(), set);
        }

        InMemoryOntologyDescriptor descriptor = new InMemoryOntologyDescriptor(ontology);
        if (set.contains(descriptor)) {
            throw new OntologyStoreException("This ontology is already stored in this store: " + ontology);
        }
        set.add(descriptor);
    }

    @Override
    public void deleteOntology(OntologyDescriptor ontology) throws OntologyStoreException {
        Set<OntologyDescriptor> set = ontologies.get(ontology.getOntologyIRI());
        if (set == null || !set.contains(ontology)) {
            throw new OntologyStoreException("No such ontology '" + ontology + "' in this store.");
        }
        set.remove(ontology);
    }

    @Override
    public Set<OntologyDescriptor> getOntologyVersions(IRI ontologyIRI) {
        Set<OntologyDescriptor> set = ontologies.get(ontologyIRI);
        if (set == null) {
            return new HashSet<OntologyDescriptor>();
        }
        return set;
    }

    @Override
    public OntologyDescriptor getOntology(IRI ontologyIRI, IRI versionIRI) {
        Set<OntologyDescriptor> versions = getOntologyVersions(ontologyIRI);
        for (OntologyDescriptor desc : versions) {
            if (saveCompare(versionIRI, desc.getVersionIRI())) {
                return desc;
            }
        }
        return null;
    }

    private boolean saveCompare(IRI versionIRI, IRI versionIRI2) {
        if (versionIRI == null) {
            return versionIRI2 == null;
        }
        return versionIRI.equals(versionIRI2);
    }

}
