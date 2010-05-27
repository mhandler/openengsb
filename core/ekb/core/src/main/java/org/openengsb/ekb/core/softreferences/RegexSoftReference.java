package org.openengsb.ekb.core.softreferences;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.ReferenceableConcept;
import org.openengsb.ekb.api.SoftReference;

public class RegexSoftReference<U, T> implements SoftReference<U, T> {

    private Concept<U> sourceConcept;

    private ReferenceableConcept<T> targetConcept;

    private String referenceField;

    @Override
    public List<T> follow(EKB ekb, U sourceObject) {
        try {
            Class<U> sourceClass = sourceConcept.getConceptClass();
            Object sourceFieldValue = getReferenceFieldValue(sourceObject, sourceClass);
            String sourceFieldText = String.valueOf(sourceFieldValue);
            return findAndFollow(ekb, sourceFieldText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getReferenceFieldValue(U sourceObject, Class<U> sourceClass) throws NoSuchFieldException,
            IllegalAccessException {
        Field sourceField = sourceClass.getDeclaredField(referenceField);
        boolean accessible = sourceField.isAccessible();
        sourceField.setAccessible(true);
        Object sourceFieldValue = sourceField.get(sourceObject);
        sourceField.setAccessible(accessible);
        return sourceFieldValue;
    }

    private List<T> findAndFollow(EKB ekb, String sourceFieldText) {
        Matcher matcher = Pattern.compile(targetConcept.getReferenceRegex()).matcher(sourceFieldText);

        List<T> result = new ArrayList<T>();
        while (matcher.find()) {
            String matchingPart = matcher.group();

            String key = targetConcept.extractKey(matchingPart);
            if (key != null) {
                T element = getTargetElementByKey(ekb, key);
                if (element != null) {
                    result.add(element);
                }
            }

        }
        return result;
    }

    private T getTargetElementByKey(EKB ekb, String key) {
        List<ConceptSource> sources = ekb.getSources(targetConcept);
        for (ConceptSource source : sources) {
            T result = ekb.getDataElement(source, targetConcept, key);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public Concept<U> getSourceConcept() {
        return sourceConcept;
    }

    @Override
    public Concept<T> getTargetConcept() {
        return targetConcept;
    }

    public void setReferenceField(String referenceField) {
        this.referenceField = referenceField;
    }

    public String getReferenceField() {
        return referenceField;
    }

}
