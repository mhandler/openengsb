package org.openengsb.ekb.api;

public interface FieldMapping {

    String getSourceFieldName();

    String getTargetFieldName();

    Object transform(Object source);

}
