package org.openengsb.ontologystore.core.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openengsb.ontologystore.core.OntologyDescriptor;
import org.openengsb.ontologystore.core.OntologyStore;
import org.openengsb.ontologystore.core.OntologyStoreException;

public class InMemoryOntologyStore extends OntologyStore {

    private Map<String, Set<OntologyDescriptor>> ontologies = new HashMap<String, Set<OntologyDescriptor>>();

    @Override
    public void addOntology(OntologyDescriptor ontology) throws OntologyStoreException {
        Set<OntologyDescriptor> set = ontologies.get(ontology.getOntologyUri());
        if (set == null) {
            set = new HashSet<OntologyDescriptor>();
            ontologies.put(ontology.getOntologyUri(), set);
        }
        setVersionNumber(ontology);
        set.add(ontology);
    }

    @Override
    public void deleteOntology(OntologyDescriptor ontology) throws OntologyStoreException {
        Set<OntologyDescriptor> set = ontologies.get(ontology.getOntologyUri());
        if (set == null || !set.contains(ontology)) {
            throw new OntologyStoreException("No such ontology '" + ontology + "' in this store.");
        }
        set.remove(ontology);
    }

    @Override
    public Set<OntologyDescriptor> getOntologyVersions(String ontologyUri) {
        Set<OntologyDescriptor> set = ontologies.get(ontologyUri);
        if (set == null) {
            return new HashSet<OntologyDescriptor>();
        }
        return set;
    }

}
