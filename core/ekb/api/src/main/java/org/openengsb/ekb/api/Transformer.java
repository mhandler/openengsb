package org.openengsb.ekb.api;

public interface Transformer {
    Object transform(Object source, Class<?> targetType);
}
