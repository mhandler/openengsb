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
package org.openengsb.ekb.core.softreferences;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.NoSuchConceptSourceException;
import org.openengsb.ekb.api.SoftReference;

public class RegexSoftReference<SOURCETYPE, TARGETTYPE> implements SoftReference<SOURCETYPE, TARGETTYPE> {

    private Concept<SOURCETYPE> sourceConcept;

    private Concept<TARGETTYPE> targetConcept;

    private String referenceField;

    private String regex;

    private String id;

    @Override
    public List<TARGETTYPE> follow(EKB ekb, SOURCETYPE sourceObject) {
        try {
            Class<SOURCETYPE> sourceClass = sourceConcept.getConceptClass();
            Object sourceFieldValue = getReferenceFieldValue(sourceObject, sourceClass);
            String sourceFieldText = String.valueOf(sourceFieldValue);
            return findAndFollow(ekb, sourceFieldText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getReferenceFieldValue(SOURCETYPE sourceObject, Class<SOURCETYPE> sourceClass)
            throws NoSuchFieldException, IllegalAccessException {
        Field sourceField = sourceClass.getDeclaredField(referenceField);
        boolean accessible = sourceField.isAccessible();
        sourceField.setAccessible(true);
        Object sourceFieldValue = sourceField.get(sourceObject);
        sourceField.setAccessible(accessible);
        return sourceFieldValue;
    }

    private List<TARGETTYPE> findAndFollow(EKB ekb, String sourceFieldText) throws NoSuchConceptException,
            NoSuchConceptSourceException {
        Matcher matcher = Pattern.compile(regex).matcher(sourceFieldText);

        List<TARGETTYPE> result = new ArrayList<TARGETTYPE>();
        while (matcher.find()) {
            String key = matcher.group(1);
            if (key != null) {
                TARGETTYPE element = getTargetElementByKey(ekb, key);
                if (element != null) {
                    result.add(element);
                }
            }

        }
        return result;
    }

    private TARGETTYPE getTargetElementByKey(EKB ekb, String key) throws NoSuchConceptException,
            NoSuchConceptSourceException {
        List<String> sources = ekb.getSourceIds(targetConcept.getKey());
        for (String source : sources) {
            TARGETTYPE result = ekb
                    .getDataElement(source, targetConcept.getKey(), targetConcept.getConceptClass(), key);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public Concept<TARGETTYPE> getTargetConcept() {
        return targetConcept;
    }

    public void setReferenceField(String referenceField) {
        this.referenceField = referenceField;
    }

    public String getReferenceField() {
        return referenceField;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setTargetConcept(Concept<TARGETTYPE> targetConcept) {
        this.targetConcept = targetConcept;
    }

    public void setSourceConcept(Concept<SOURCETYPE> sourceConcept) {
        this.sourceConcept = sourceConcept;
    }

    @Override
    public String getId() {
        return id;
    }

    public void initId() {
        this.id = sourceConcept.getKey().toString() + " - " + targetConcept.getKey().toString();
    }
}
