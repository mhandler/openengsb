package org.openengsb.ekb.api;

public interface Transformer {
    <TARGETTYPE> TARGETTYPE transform(Object source, Class<TARGETTYPE> targetType);
}
