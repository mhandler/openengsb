package org.openengsb.ekb.core.messagetransformation.transformationstore;

import org.semanticweb.owlapi.model.OWLOntologyID;

public interface TransformationStore {

    void putTransformationMap(OWLOntologyID sourceOntology, OWLOntologyID targetOntology, TransformationMap tmap);

    void removeTransformationMap(OWLOntologyID sourceOntology, OWLOntologyID targetOntology);

    TransformationMap getTransformationMap(OWLOntologyID sourceOntology, OWLOntologyID targetOntology);

    void removeAllTransformationMapsWithOntology(OWLOntologyID ontology);

}
