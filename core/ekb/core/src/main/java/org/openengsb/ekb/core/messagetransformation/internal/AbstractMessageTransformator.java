package org.openengsb.ekb.core.messagetransformation.internal;

import org.openengsb.ekb.core.messagetransformation.MessageTransformator;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationStore;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;

public abstract class AbstractMessageTransformator implements MessageTransformator {

    private OntologyDescriptor sourceOntology;

    private OntologyDescriptor targetOntology;

    private TransformationStore transformationStore;

    @Override
    public void setSourceOntology(OntologyDescriptor sourceOntology) {
        this.sourceOntology = sourceOntology;
    }

    @Override
    public void setTargetOntology(OntologyDescriptor targetOntology) {
        this.targetOntology = targetOntology;
    }

    protected TransformationMap getTransformationMap() {
        return transformationStore.getTransformationMap(sourceOntology.getOntologyID(), targetOntology.getOntologyID());
    }

    @Override
    public void setTransformationStore(TransformationStore transformationStore) {
        this.transformationStore = transformationStore;
    }

    protected OntologyDescriptor getSourceOntology() {
        return sourceOntology;
    }

    protected OntologyDescriptor getTargetOntology() {
        return targetOntology;
    }

    protected TransformationStore getTransformationStore() {
        return transformationStore;
    }

}
