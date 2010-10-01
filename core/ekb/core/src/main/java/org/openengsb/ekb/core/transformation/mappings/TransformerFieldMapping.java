package org.openengsb.ekb.core.transformation.mappings;

import java.lang.reflect.Constructor;

import org.openengsb.ekb.api.Transformer;

public class TransformerFieldMapping extends AbstractMapping {

    private String transformer;

    @Override
    public <T> T transform(Object source, Class<T> targetType) {
        Transformer instance = createTransformer();
        return instance.transform(source, targetType);
    }

    private Transformer createTransformer() {
        try {
            Class<?> transformerClass = Class.forName(transformer);
            return (Transformer) getInstance(transformerClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public String getTransformer() {
        return transformer;
    }

    public Object getInstance(Class<?> clazz) throws Exception {
        Constructor<?> noArgConstructor = clazz.getDeclaredConstructor();
        boolean accessible = noArgConstructor.isAccessible();
        noArgConstructor.setAccessible(true);
        Object o = noArgConstructor.newInstance();
        noArgConstructor.setAccessible(accessible);
        return o;
    }

}
