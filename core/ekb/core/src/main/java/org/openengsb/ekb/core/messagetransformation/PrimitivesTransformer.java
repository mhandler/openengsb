package org.openengsb.ekb.core.messagetransformation;

public class PrimitivesTransformer {

    public static Object transform(Class<?> source, Class<?> target, Object value) {
        if (value == null) {
            return null;
        }
        if (target.equals(String.class)) {
            return transformToString(value);
        } else if (target.equals(Integer.class)) {
            return transformToInteger(value);
        }
        // TODO: support more than Integer->String and String->Integer
        return value;
    }

    private static Integer transformToInteger(Object value) {
        return Integer.valueOf(transformToString(value));
    }

    private static String transformToString(Object value) {
        return String.valueOf(value);
    }

}
