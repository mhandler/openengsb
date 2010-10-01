package org.openengsb.ekb.usecases;

import org.openengsb.ekb.api.Transformer;

public class RangeToTypeTransformer implements Transformer {

    @Override
    public Object transform(Object source, Class<?> targetType) {
        String range = source.toString();
        if (range.equals("[0,1]")) {
            return Sensor.Type.BINARY;
        } else if (range.equals("[*,*]")) {
            return Sensor.Type.INTEGER;
        }
        return Sensor.Type.OTHER;
    }

}
