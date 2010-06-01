package org.openengsb.ekb.core.transformation.mappings;

import org.openengsb.ekb.api.FieldMapping;

public class IdentityMapping implements FieldMapping {

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

    @Override
    public Object transform(Object source) {
        return source;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

}
