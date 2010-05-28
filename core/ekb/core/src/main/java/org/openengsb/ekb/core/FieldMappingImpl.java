package org.openengsb.ekb.core;

import org.openengsb.ekb.api.FieldMapping;

public class FieldMappingImpl implements FieldMapping {

    private String sourceField;

    private String targetField;

    @Override
    public String getSourceFieldName() {
        return this.sourceField;
    }

    @Override
    public String getTargetFieldName() {
        return this.targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

}
