/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ekb.core.messagetransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.FieldMapping;

public class SimpleTransformer implements Transformer {

    @Override
    public <TARGETTYPE> TARGETTYPE transform(Concept<?> source, Concept<TARGETTYPE> target, Object input)
            throws TransformationException {
        Class<TARGETTYPE> targetClass = target.getConceptClass();
        Class<?> sourceClass = source.getConceptClass();
        if (sourceClass.isPrimitive() || targetClass.isPrimitive()) {
            return handlePrimitive(source, target, input);
        }

        TARGETTYPE output = createInstance(targetClass);
        mapFields(source, target, input, output);
        return output;
    }

    @SuppressWarnings("unchecked")
    private <TARGETTYPE> TARGETTYPE handlePrimitive(Concept<?> source, Concept<TARGETTYPE> target, Object input) {
        // TODO support more than identity transformation for primitives (like
        // string to integer, etc...
        return (TARGETTYPE) input;
    }

    private <TARGETTYPE> TARGETTYPE createInstance(Class<TARGETTYPE> clazz) {
        try {
            Constructor<TARGETTYPE> constructor = clazz.getConstructor();
            boolean accessible = constructor.isAccessible();
            constructor.setAccessible(true);
            TARGETTYPE instance = constructor.newInstance();
            constructor.setAccessible(accessible);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <TARGETTYPE> void mapFields(Concept<?> source, Concept<TARGETTYPE> target, Object input, TARGETTYPE output) {
        List<FieldMapping> mappings = source.getFieldMappings(target);
        for (FieldMapping mapping : mappings) {
            String sourceFieldName = mapping.getSourceFieldName();
            Object value = getFieldValue(sourceFieldName, source.getConceptClass(), input);
            String targetFieldName = mapping.getTargetFieldName();
            // TODO transform value
            setFieldValue(targetFieldName, target.getConceptClass(), output, value);
        }
    }

    private Object getFieldValue(String fieldName, Class<?> clazz, Object obj) {
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);
            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);
            Object fieldValue = declaredField.get(obj);
            declaredField.setAccessible(accessible);
            return fieldValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldValue(String fieldName, Class<?> clazz, Object obj, Object value) {
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);
            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);
            declaredField.set(obj, value);
            declaredField.setAccessible(accessible);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
