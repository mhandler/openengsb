package org.openengsb.ekb.core.transformation.mappings;

import java.util.Arrays;
import java.util.List;

public class AutomaticMapping extends AbstractMapping {

    private List<Class<?>> primitives = Arrays.asList(new Class<?>[] { Boolean.class, Integer.class, Long.class,
            Short.class, Byte.class, Character.class, Float.class, Double.class });

    @Override
    @SuppressWarnings("unchecked")
    public <T> T transform(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();

        if (targetType.isAssignableFrom(sourceType)) {
            return (T) source;
        } else if (targetType.equals(String.class)) {
            return (T) String.valueOf(source);
        } else if (isPrimitiveObjectClass(targetType)) {
            if (sourceType.equals(String.class)) {
                return (T) parseToPrimitive(source.toString(), targetType);
            }
        }

        return null;
    }

    private boolean isPrimitiveObjectClass(Class<?> type) {
        return primitives.contains(type);
    }

    private Object parseToPrimitive(String input, Class<?> targetType) {
        int primitiveIndex = primitives.indexOf(targetType);
        switch (primitiveIndex) {
        case 0:
            return Boolean.valueOf(input);
        case 1:
            return Integer.valueOf(input);
        case 2:
            return Long.valueOf(input);
        case 3:
            return Short.valueOf(input);
        case 4:
            return Byte.valueOf(input);
        case 5:
            return input.charAt(0);
        case 6:
            return Float.valueOf(input);
        case 7:
            return Double.valueOf(input);
        }
        throw new IllegalStateException("Parse to Primitive called with no primitive type.");
    }

}
