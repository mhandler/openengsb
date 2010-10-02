package org.openengsb.ekb.api;

public interface FieldMapping {

    String getSourceFieldName();

    String getTargetFieldName();

    <TARGETTYPE> TARGETTYPE transform(Object source, Class<TARGETTYPE> targetType) throws TransformationException;

}
