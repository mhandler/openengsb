package org.openengsb.ontologystore.core;

import java.util.Set;

public abstract class OntologyStore {

    public abstract void addOntology(OntologyDescriptor ontology) throws OntologyStoreException;

    public abstract void deleteOntology(OntologyDescriptor ontology) throws OntologyStoreException;

    public abstract Set<OntologyDescriptor> getOntologyVersions(String ontologyUri);

    protected void setVersionNumber(OntologyDescriptor ontology) {
        Set<OntologyDescriptor> versions = getOntologyVersions(ontology.getOntologyUri());
        int versionNumber = getNextVersionNumber(versions);
        ontology.setVersion(versionNumber);
    }

    private int getNextVersionNumber(Set<OntologyDescriptor> set) {
        int max = 1;
        for (OntologyDescriptor ontology : set) {
            if (ontology.getVersion() >= max) {
                max = ontology.getVersion() + 1;
            }
        }
        return max;
    }
}
