package org.openengsb.ekb.core.messagetransformation.transformationstore.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationStore;
import org.semanticweb.owlapi.model.OWLOntologyID;

public class InMemoryTransformationStore implements TransformationStore {

    private Map<TransformationMapKey, TransformationMap> maps = new HashMap<TransformationMapKey, TransformationMap>();

    @Override
    public TransformationMap getTransformationMap(OWLOntologyID sourceOntology, OWLOntologyID targetOntology) {
        TransformationMapKey key = new TransformationMapKey(sourceOntology, targetOntology);
        return maps.get(key);
    }

    @Override
    public void putTransformationMap(OWLOntologyID sourceOntology, OWLOntologyID targetOntology, TransformationMap tmap) {
        TransformationMapKey key = new TransformationMapKey(sourceOntology, targetOntology);
        maps.put(key, tmap);
    }

    @Override
    public void removeTransformationMap(OWLOntologyID sourceOntology, OWLOntologyID targetOntology) {
        TransformationMapKey key = new TransformationMapKey(sourceOntology, targetOntology);
        maps.remove(key);
    }

    @Override
    public void removeAllTransformationMapsWithOntology(OWLOntologyID ontology) {
        Set<TransformationMapKey> toRemove = new HashSet<TransformationMapKey>();
        for (TransformationMapKey key : maps.keySet()) {
            if (ontology.equals(key.sourceOntology) || ontology.equals(key.targetOntology)) {
                toRemove.add(key);
            }
        }
        for (TransformationMapKey key : toRemove) {
            maps.remove(key);
        }
    }

    private class TransformationMapKey {
        private OWLOntologyID sourceOntology;

        private OWLOntologyID targetOntology;

        public TransformationMapKey(OWLOntologyID sourceOntology, OWLOntologyID targetOntology) {
            this.sourceOntology = sourceOntology;
            this.targetOntology = targetOntology;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((sourceOntology == null) ? 0 : sourceOntology.hashCode());
            result = prime * result + ((targetOntology == null) ? 0 : targetOntology.hashCode());
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
            TransformationMapKey other = (TransformationMapKey) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (sourceOntology == null) {
                if (other.sourceOntology != null)
                    return false;
            } else if (!sourceOntology.equals(other.sourceOntology))
                return false;
            if (targetOntology == null) {
                if (other.targetOntology != null)
                    return false;
            } else if (!targetOntology.equals(other.targetOntology))
                return false;
            return true;
        }

        private InMemoryTransformationStore getOuterType() {
            return InMemoryTransformationStore.this;
        }

    }

}
