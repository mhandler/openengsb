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

import org.openengsb.ekb.annotations.Key;
import org.openengsb.ekb.annotations.MapsTo;
import org.openengsb.ekb.annotations.ReferenceId;
import org.openengsb.ekb.annotations.SuperConcept;
import org.openengsb.ekb.annotations.Transformation;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.SoftReference;
import org.openengsb.ekb.api.mapping.Transformer;
import org.openengsb.ekb.core.ConceptImpl;
import org.openengsb.ekb.core.softreferences.RegexSoftReference;
import org.openengsb.ekb.core.transformation.mappings.AbstractMapping;
import org.openengsb.ekb.core.transformation.mappings.AutomaticMapping;
import org.openengsb.ekb.core.transformation.mappings.TransformerFieldMapping;

public class ConceptParser {

    private ConceptCache cache;

    private ThreadPoolExecutor executor;

    private static final int EXECUTOR_TIME_OUT = 5000;

    public List<Concept<?>> parseConcepts(List<Class<?>> classes) throws AnnotationMissingException {
        List<Concept<?>> result = new ArrayList<Concept<?>>();

        executor = new ThreadPoolExecutor(1, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        cache = new ConceptCache();

        for (Class<?> clazz : classes) {
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

        checkSoftRefTargetsValid(result);

        return result;
    }

    private void checkSoftRefTargetsValid(List<Concept<?>> result) throws AnnotationMissingException {
        for (Concept<?> c : result) {
            for (SoftReference<?, ?> ref : c.getSoftReferences()) {
                checkSoftRefTargetValid(ref.getTargetConcept());
            }
        }
    }

    private void checkSoftRefTargetValid(Concept<?> targetConcept) throws AnnotationMissingException {
        for (Field f : targetConcept.getConceptClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Key.class)) {
                return;
            }
        }
        throw new AnnotationMissingException(
                "A target of a soft reference has to define a key by annotating a field with the org.openengsb.ekb.annotations.Key annotation.");
    }

    private <TYPE> Concept<TYPE> parseConcept(Class<TYPE> clazz) throws AnnotationMissingException {
        ConceptImpl<TYPE> concept = new ConceptImpl<TYPE>();
        concept.setConceptClass(clazz);
        setKey(clazz, concept);
        cache.storeConcept(concept);
        setSuperConcept(clazz, concept);
        setFieldMappings(clazz, concept);
        setSoftReferences(clazz, concept);

        return concept;
    }

    private <TYPE> void setKey(Class<TYPE> clazz, ConceptImpl<TYPE> concept) throws AnnotationMissingException {
        checkObligatoryAnnotationPresent(clazz, org.openengsb.ekb.annotations.Concept.class);
        String id = clazz.getAnnotation(org.openengsb.ekb.annotations.Concept.class).id();
        String version = clazz.getAnnotation(org.openengsb.ekb.annotations.Concept.class).version();
        concept.setKey(new ConceptKey(id, version));
    }

    private <TYPE> void setSuperConcept(Class<TYPE> clazz, ConceptImpl<TYPE> concept) {
        if (isAnnotationPresent(clazz, SuperConcept.class)) {
            SuperConcept superConceptAnn = clazz.getAnnotation(SuperConcept.class);
            ConceptKey superConceptKey = new ConceptKey(superConceptAnn.id(), superConceptAnn.version());
            ConceptImpl<?> superConcept = cache.getConcept(superConceptKey);
            if (superConcept != null) {
                concept.setSuperConcept(superConcept);
            } else {
                SuperConceptSettingTask<TYPE> task = new SuperConceptSettingTask<TYPE>(concept, superConceptKey);
                cache.addListener(task);
                executor.execute(task);
            }
        }
    }

    private <TYPE> void setFieldMappings(Class<TYPE> clazz, ConceptImpl<TYPE> concept) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (isAnnotationPresent(field, MapsTo.class)) {
                for (String targetField : field.getAnnotation(MapsTo.class).value()) {
                    AbstractMapping mapping = null;
                    if (isAnnotationPresent(field, Transformation.class)) {
                        Class<? extends Transformer> transformation = field.getAnnotation(Transformation.class).value();
                        TransformerFieldMapping transformer = new TransformerFieldMapping();
                        transformer.setTransformer(transformation.getName());
                        mapping = transformer;
                    } else {
                        mapping = new AutomaticMapping();
                    }
                    mapping.setSourceField(field.getName());
                    mapping.setTargetField(targetField);
                    concept.addFieldMapping(mapping);
                }
            }
        }
    }

    private <TYPE> void setSoftReferences(Class<TYPE> clazz, ConceptImpl<TYPE> concept)
            throws AnnotationMissingException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (isAnnotationPresent(field, ReferenceId.class)) {
                ReferenceId referenceId = field.getAnnotation(ReferenceId.class);
                String targetConceptId = referenceId.targetConceptId();
                String targetConceptVersion = referenceId.targetConceptVersion();
                ConceptKey targetConceptKey = new ConceptKey(targetConceptId, targetConceptVersion);
                String regexp = referenceId.regexp();
                Concept<?> targetConcept = cache.getConcept(targetConceptKey);
                if (targetConcept != null) {
                    concept.addSoftReference(createSoftRef(concept, targetConcept, field.getName(), regexp));
                } else {
                    SoftReferenceAddingTask<TYPE> task = new SoftReferenceAddingTask<TYPE>(concept, targetConceptKey,
                            field.getName(), regexp);
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
        ref.initId();
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

        protected ConceptKey keyToWaitFor;

        private Semaphore semaphore = new Semaphore(0);

        public CacheWaiterTask(ConceptImpl<T> concept, ConceptKey keyToWaitFor) {
            this.concept = concept;
            this.keyToWaitFor = keyToWaitFor;
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
            if (concept.getKey().equals(keyToWaitFor)) {
                semaphore.release();
                cache.removeListener(this);
            }
        }

    }

    private class SuperConceptSettingTask<T> extends CacheWaiterTask<T> {

        public SuperConceptSettingTask(ConceptImpl<T> concept, ConceptKey keyToWaitFor) {
            super(concept, keyToWaitFor);
        }

        @Override
        public void doAction() {
            concept.setSuperConcept(cache.getConcept(keyToWaitFor));
        }

    }

    private class SoftReferenceAddingTask<T> extends CacheWaiterTask<T> {

        private String fieldName;

        private String regexp;

        public SoftReferenceAddingTask(ConceptImpl<T> concept, ConceptKey keyToWaitFor, String fieldName, String regexp) {
            super(concept, keyToWaitFor);
            this.fieldName = fieldName;
            this.regexp = regexp;
        }

        @Override
        public void doAction() {
            Concept<?> targetConcept = cache.getConcept(keyToWaitFor);
            concept.addSoftReference(createSoftRef(concept, targetConcept, fieldName, regexp));
        }

    }

}
