package org.openengsb.ekb.usecases;

import org.openengsb.ekb.api.Transformer;

public class TypeToTypeTransformer implements Transformer {

    @Override
    public Object transform(Object source, Class<?> targetType) {
        String type = source.toString();
        if (type.equals("boolean")) {
            return Sensor.Type.BINARY;
        } else if (type.equals("int")) {
            return Sensor.Type.INTEGER;
        } else {
            return Sensor.Type.OTHER;
        }
    }

}
