package org.openengsb.ontologystore.core;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

public abstract class OntologyDescriptor {

    private IRI ontologyIRI;

    private IRI versionIRI;

    public OntologyDescriptor(IRI ontologyIRI, IRI versionIRI) {
        this.ontologyIRI = ontologyIRI;
        this.versionIRI = versionIRI;
    }

    public IRI getOntologyIRI() {
        return ontologyIRI;
    }

    public IRI getVersionIRI() {
        return versionIRI;
    }

    public abstract OWLOntology getOntology();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ontologyIRI == null) ? 0 : ontologyIRI.hashCode());
        result = prime * result + ((versionIRI == null) ? 0 : versionIRI.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OntologyDescriptor other = (OntologyDescriptor) obj;
        if (ontologyIRI == null) {
            if (other.ontologyIRI != null)
                return false;
        } else if (!ontologyIRI.equals(other.ontologyIRI))
            return false;
        if (versionIRI == null) {
            if (other.versionIRI != null)
                return false;
        } else if (!versionIRI.equals(other.versionIRI))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OntologyDescriptor [ontologyUri=" + ontologyIRI + ", version=" + versionIRI + "]";
    }

}
