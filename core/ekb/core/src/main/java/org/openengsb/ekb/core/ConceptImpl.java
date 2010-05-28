package org.openengsb.ekb.core;

import java.util.ArrayList;
import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.FieldMapping;
import org.openengsb.ekb.api.SoftReference;

public class ConceptImpl<TYPE> implements Concept<TYPE> {

    private String id;

    private Class<TYPE> conceptClass;

    private List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>();

    private List<SoftReference<TYPE, ?>> references = new ArrayList<SoftReference<TYPE, ?>>();

    private List<Concept<?>> subConcepts = new ArrayList<Concept<?>>();

    private ConceptImpl<?> superConcept;

    @Override
    public Class<TYPE> getConceptClass() {
        return conceptClass;
    }

    @Override
    public List<FieldMapping> getFieldMappings(Concept<?> targetConcept) {
        return new ArrayList<FieldMapping>(fieldMappings);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<SoftReference<TYPE, ?>> getSoftReferences() {
        return new ArrayList<SoftReference<TYPE, ?>>(references);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TARGET> List<SoftReference<TYPE, TARGET>> getSoftReferences(Concept<TARGET> targetConcept) {
        List<SoftReference<TYPE, ?>> softReferences = getSoftReferences();
        List<SoftReference<TYPE, TARGET>> result = new ArrayList<SoftReference<TYPE, TARGET>>();
        for (SoftReference<TYPE, ?> ref : softReferences) {
            if (ref.getTargetConcept().equals(targetConcept)) {
                result.add((SoftReference<TYPE, TARGET>) ref);
            }
        }
        return result;
    }

    @Override
    public List<Concept<?>> getSubConcepts() {
        return subConcepts;
    }

    @Override
    public Concept<?> getSuperConcept() {
        return superConcept;
    }

    public void setId(String id) {
        this.id = id;

    }

    public void setConceptClass(Class<TYPE> conceptClass) {
        this.conceptClass = conceptClass;
    }

    public void setSuperConcept(ConceptImpl<?> superConcept) {
        if (this.superConcept != null) {
            this.superConcept.removeSubConcept(this);
        }
        this.superConcept = superConcept;
        superConcept.addSubConcept(this);
    }

    public void addSoftReference(SoftReference<TYPE, ?> ref) {
        this.references.add(ref);
    }

    public void addFieldMapping(FieldMapping mapping) {
        this.fieldMappings.add(mapping);
    }

    private void addSubConcept(Concept<?> subConcept) {
        this.subConcepts.add(subConcept);
    }

    private void removeSubConcept(Concept<?> subConcept) {
        this.subConcepts.remove(subConcept);
    }

}
