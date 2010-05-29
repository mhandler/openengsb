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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.openengsb.ekb.annotations.MapsTo;
import org.openengsb.ekb.annotations.ReferenceId;
import org.openengsb.ekb.annotations.SuperConcept;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.SoftReference;
import org.openengsb.ekb.core.ConceptImpl;
import org.openengsb.ekb.core.FieldMappingImpl;
import org.openengsb.ekb.core.softreferences.RegexSoftReference;

public class ConceptParser {

    private ConceptCache cache;

    private ThreadPoolExecutor executor;

    private static final int EXECUTOR_TIME_OUT = 5000;

    public <TYPE> List<Concept<TYPE>> parseConcepts(List<Class<TYPE>> classes) throws AnnotationMissingException {
        List<Concept<TYPE>> result = new ArrayList<Concept<TYPE>>();

        executor = new ThreadPoolExecutor(1, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        cache = new ConceptCache();

        for (Class<TYPE> clazz : classes) {
            result.add(parseConcept(clazz));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(EXECUTOR_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        if (!executor.isShutdown()) {
            throw new IllegalStateException("Some references to superclasses or soft references could not be resolved.");
        }

        return result;
    }

    private <TYPE> Concept<TYPE> parseConcept(Class<TYPE> clazz) throws AnnotationMissingException {
        ConceptImpl<TYPE> concept = new ConceptImpl<TYPE>();
        concept.setConceptClass(clazz);

        setId(clazz, concept);
        cache.storeConcept(concept);
        setSuperConcept(clazz, concept);
        setFieldMappings(clazz, concept);
        setSoftReferences(clazz, concept);

        return concept;
    }

    private <TYPE> void setId(Class<TYPE> clazz, ConceptImpl<TYPE> concept) throws AnnotationMissingException {
        checkObligatoryAnnotationPresent(clazz, org.openengsb.ekb.annotations.Concept.class);
        concept.setId(clazz.getAnnotation(org.openengsb.ekb.annotations.Concept.class).value());
    }

    private <TYPE> void setSuperConcept(Class<TYPE> clazz, ConceptImpl<TYPE> concept) {
        if (isAnnotationPresent(clazz, SuperConcept.class)) {
            String superConceptId = clazz.getAnnotation(SuperConcept.class).value();
            ConceptImpl<?> superConcept = cache.getConcept(superConceptId);
            if (superConcept != null) {
                concept.setSuperConcept(superConcept);
            } else {
                SetSuperConceptTask<TYPE> task = new SetSuperConceptTask<TYPE>(concept, superConceptId);
                cache.addListener(task);
                executor.execute(task);
            }
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
                String regexp = referenceId.regexp();
                Concept<?> targetConcept = cache.getConcept(targetConceptId);
                if (targetConcept != null) {
                    concept.addSoftReference(createSoftRef(concept, targetConcept, field.getName(), regexp));
                } else {
                    AddSoftReferenceTask<TYPE> task = new AddSoftReferenceTask<TYPE>(concept, targetConceptId, field
                            .getName(), regexp);
                    cache.addListener(task);
                    executor.execute(task);
                }
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

    public void setCache(ConceptCache cache) {
        this.cache = cache;
    }

    private abstract class CacheWaiterTask<T> implements Runnable, ConceptCacheListener {

        protected ConceptImpl<T> concept;

        protected String idToWaitFor;

        private Semaphore semaphore = new Semaphore(0);

        public CacheWaiterTask(ConceptImpl<T> concept, String idToWaitFor) {
            this.concept = concept;
            this.idToWaitFor = idToWaitFor;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                doAction();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        public abstract void doAction();

        @Override
        public void conceptStored(Concept<?> concept) {
            if (concept.getId().equals(idToWaitFor)) {
                semaphore.release();
                cache.removeListener(this);
            }
        }

    }

    private class SetSuperConceptTask<T> extends CacheWaiterTask<T> {

        public SetSuperConceptTask(ConceptImpl<T> concept, String idToWaitFor) {
            super(concept, idToWaitFor);
        }

        @Override
        public void doAction() {
            concept.setSuperConcept(cache.getConcept(idToWaitFor));
        }

    }

    private class AddSoftReferenceTask<T> extends CacheWaiterTask<T> {

        private String fieldName;

        private String regexp;

        public AddSoftReferenceTask(ConceptImpl<T> concept, String idToWaitFor, String fieldName, String regexp) {
            super(concept, idToWaitFor);
            this.fieldName = fieldName;
            this.regexp = regexp;
        }

        @Override
        public void doAction() {
            Concept<?> targetConcept = cache.getConcept(idToWaitFor);
            concept.addSoftReference(createSoftRef(concept, targetConcept, fieldName, regexp));
        }

    }

}
