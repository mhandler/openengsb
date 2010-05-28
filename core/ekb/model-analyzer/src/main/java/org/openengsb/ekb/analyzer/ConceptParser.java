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
package org.openengsb.ekb.analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.openengsb.ekb.annotations.MapsTo;
import org.openengsb.ekb.annotations.ReferenceId;
import org.openengsb.ekb.annotations.SuperConcept;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.SoftReference;
import org.openengsb.ekb.core.ConceptImpl;
import org.openengsb.ekb.core.FieldMappingImpl;
import org.openengsb.ekb.core.softreferences.RegexSoftReference;

public class ConceptParser {

    private ConceptCache cache = new ConceptCache();

    public <TYPE> Concept<TYPE> parseConcept(Class<TYPE> clazz) throws AnnotationMissingException {
        ConceptImpl<TYPE> concept = new ConceptImpl<TYPE>();
        concept.setConceptClass(clazz);

        setId(clazz, concept);
        cache.storeConcept(concept);
        setSuperConcept(clazz, concept);
        setFieldMappings(clazz, concept);
        setSoftReferences(clazz, concept);

        return null;
    }

    private <TYPE> void setId(Class<TYPE> clazz, ConceptImpl<TYPE> concept) throws AnnotationMissingException {
        checkObligatoryAnnotationPresent(clazz, org.openengsb.ekb.annotations.Concept.class);
        concept.setId(clazz.getAnnotation(org.openengsb.ekb.annotations.Concept.class).value());
    }

    private <TYPE> void setSuperConcept(Class<TYPE> clazz, ConceptImpl<TYPE> concept) {
        if (isAnnotationPresent(clazz, SuperConcept.class)) {
            String superConceptId = clazz.getAnnotation(SuperConcept.class).value();
            ConceptImpl<?> superConcept = cache.getConcept(superConceptId);
            // TODO Handle case that concept is not already in cache...
            concept.setSuperConcept(superConcept);
        }
    }

    private <TYPE> void setFieldMappings(Class<TYPE> clazz, ConceptImpl<TYPE> concept) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (isAnnotationPresent(field, MapsTo.class)) {
                String targetField = field.getAnnotation(MapsTo.class).value();
                FieldMappingImpl mapping = new FieldMappingImpl();
                mapping.setSourceField(field.getName());
                mapping.setTargetField(targetField);
                concept.addFieldMapping(mapping);
            }
        }
    }

    private <TYPE> void setSoftReferences(Class<TYPE> clazz, ConceptImpl<TYPE> concept) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (isAnnotationPresent(field, ReferenceId.class)) {
                ReferenceId referenceId = field.getAnnotation(ReferenceId.class);
                String targetConceptId = referenceId.targetConcept();
                Concept<?> targetConcept = cache.getConcept(targetConceptId);
                // TODO handle case that concept is not already cached
                String regexp = referenceId.regexp();
                concept.addSoftReference(createSoftRef(concept, targetConcept, field.getName(), regexp));
            }
        }
    }

    private <T, U> SoftReference<T, U> createSoftRef(Concept<T> source, Concept<U> target, String refField,
            String regexp) {
        RegexSoftReference<T, U> ref = new RegexSoftReference<T, U>();
        ref.setReferenceField(refField);
        ref.setRegex(regexp);
        ref.setSourceConcept(source);
        ref.setTargetConcept(target);
        return ref;
    }

    private void checkObligatoryAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotationClass)
            throws AnnotationMissingException {
        if (!isAnnotationPresent(clazz, annotationClass)) {
            throw new AnnotationMissingException(clazz, annotationClass);
        }
    }

    private boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Annotation annotation = clazz.getAnnotation(annotationClass);
        return annotation != null;
    }

    private boolean isAnnotationPresent(Field field, Class<? extends Annotation> annotationClass) {
        Annotation annotation = field.getAnnotation(annotationClass);
        return annotation != null;
    }

}
