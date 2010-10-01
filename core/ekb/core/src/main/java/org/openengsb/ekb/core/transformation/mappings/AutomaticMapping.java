package org.openengsb.ekb.core.transformation.mappings;

public class AutomaticMapping extends AbstractMapping {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T transform(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();

        if (targetType.equals(sourceType)) {
            return (T) source;
        }
        return null;
    }

}
