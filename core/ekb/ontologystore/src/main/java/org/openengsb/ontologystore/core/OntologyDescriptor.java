package org.openengsb.ontologystore.core;

import org.semanticweb.owlapi.model.OWLOntology;

public abstract class OntologyDescriptor {

    private String ontologyUri;

    private int version = -1;

    public OntologyDescriptor(String ontologyUri) {
        this.ontologyUri = ontologyUri;
    }

    public String getOntologyUri() {
        return ontologyUri;
    }

    public int getVersion() {
        return version;
    }

    void setVersion(int version) {
        if (isVersionSet()) {
            throw new IllegalStateException("Version already set");
        }
        this.version = version;
    }

    public boolean isVersionSet() {
        return this.version != -1;
    }

    public abstract OWLOntology getOntology();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ontologyUri == null) ? 0 : ontologyUri.hashCode());
        result = prime * result + version;
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
        if (ontologyUri == null) {
            if (other.ontologyUri != null)
                return false;
        } else if (!ontologyUri.equals(other.ontologyUri))
            return false;
        if (version != other.version)
            return false;
        return true;
    }

}
