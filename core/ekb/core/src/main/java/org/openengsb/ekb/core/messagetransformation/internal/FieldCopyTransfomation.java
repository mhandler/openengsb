package org.openengsb.ekb.core.messagetransformation.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.TransformationUtil;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

/**
 * Copy the fields of a given object into the fields of another object. Per
 * default the values are copied directly (without transformation) to the field
 * in the target class with the same name. A map defining name mappings for the
 * fields can be used to modify this behavior. Further more it is possible to
 * specify transformer for the fields. As key always the name of the field in
 * the source class is used.
 * 
 * @param <T> The target type
 */
public class FieldCopyTransfomation<T> implements Transformation {

    private Map<String, String> fieldNameMapping = new HashMap<String, String>();

    private Class<T> targetClass;

    private IRI targetConceptIRI;

    public void setTargetClassToken(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public void setFieldNameMapping(Map<String, String> fieldNameMapping) {
        this.fieldNameMapping = new HashMap<String, String>(fieldNameMapping);
    }

    @Override
    public T transform(TransformationMap map, Object input) throws TransformationException {
        T target = createInstance();
        copyFields(map, input, target);
        return target;
    }

    private T createInstance() throws TransformationException {
        try {
            Constructor<T> noArgConstructor = targetClass.getDeclaredConstructor();
            boolean accessible = noArgConstructor.isAccessible();
            noArgConstructor.setAccessible(true);
            T target = noArgConstructor.newInstance();
            noArgConstructor.setAccessible(accessible);
            return target;
        } catch (Exception e) {
            throw new TransformationException("Could not instantiate the target class.", e);
        }
    }

    private void copyFields(TransformationMap map, Object input, Object target) throws TransformationException {
        Class<? extends Object> inputClass = input.getClass();
        for (Field f : inputClass.getDeclaredFields()) {
            String name = f.getName();
            if (fieldNameMapping.containsKey(name)) {
                copyField(map, input, name, target, fieldNameMapping.get(name));
            } else if (getField(targetClass, name) != null) {
                copyField(map, input, name, target, name);
            } else {
                throw new TransformationException("No mapping for field with name: " + name + " from class: "
                        + inputClass + ", which is not present in the target class: " + targetClass);
            }
        }

    }

    private void copyField(TransformationMap map, Object source, String sourceFieldName, Object target,
            String targetFieldName) throws TransformationException {
        Field sourceField = getField(source.getClass(), sourceFieldName);
        IRI iri = TransformationUtil.extractConceptIRI(sourceField);
        Field targetField = getField(targetClass, targetFieldName);

        Object value = getValue(source, sourceField);
        value = transformValue(map, iri, value);
        setValue(target, targetField, value);
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private Object getValue(Object source, Field sourceField) throws TransformationException {
        try {
            boolean sourceAccessible = sourceField.isAccessible();
            sourceField.setAccessible(true);
            Object value = sourceField.get(source);
            sourceField.setAccessible(sourceAccessible);
            return value;
        } catch (IllegalAccessException e) {
            throw new TransformationException("Colud not get value from source field.", e);
        }
    }

    private Object transformValue(TransformationMap map, IRI iri, Object value) throws TransformationException {
        Transformation t = map.getTransformation(iri);
        if (t != null) {
            return t.transform(map, value);
        }
        return value;
    }

    private void setValue(Object target, Field targetField, Object value) throws TransformationException {
        try {
            boolean targetAccessible = targetField.isAccessible();
            targetField.setAccessible(true);
            targetField.set(target, value);
            targetField.setAccessible(targetAccessible);
        } catch (IllegalAccessException e) {
            throw new TransformationException("Colud not set value on target field.", e);
        }
    }

    @Override
    public IRI getTargetConceptIRI(TransformationMap map, Object input) {
        if (this.targetConceptIRI == null) {
            return TransformationUtil.extractConceptIRI(targetClass);
        }
        return this.targetConceptIRI;
    }

    public void setTargetConceptIRI(IRI targetConceptIRI) {
        this.targetConceptIRI = targetConceptIRI;
    }

    @Override
    public Class<?> getTargetType(TransformationMap map, Object input) {
        return targetClass;
    }
}
