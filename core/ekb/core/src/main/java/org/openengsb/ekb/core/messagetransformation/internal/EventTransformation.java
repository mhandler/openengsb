package org.openengsb.ekb.core.messagetransformation.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.openengsb.core.model.ConceptIRI;
import org.openengsb.core.model.Event;
import org.openengsb.ekb.core.messagetransformation.Transformation;
import org.openengsb.ekb.core.messagetransformation.TransformationException;
import org.openengsb.ekb.core.messagetransformation.TransformationUtil;
import org.openengsb.ekb.core.messagetransformation.transformationstore.TransformationMap;
import org.semanticweb.owlapi.model.IRI;

public abstract class EventTransformation implements Transformation {

    private Map<String, String> elementKeyMapping = new HashMap<String, String>();

    @Override
    public Event transform(TransformationMap map, Object input) throws TransformationException {
        if (!(input instanceof Event)) {
            throw new IllegalArgumentException("Only Event objects allowed as input for transformation.");
        }
        Event inEvent = (Event) input;

        Class<?> targetClass = getTargetEventClass(inEvent);
        Event outEvent = (Event) TransformationUtil.getInstance(targetClass);

        outEvent.setName(transformName(inEvent));
        outEvent.setDomain(transformDomain(inEvent));
        outEvent.setToolConnector(transformToolConnector(inEvent));

        for (String key : inEvent.getKeys()) {
            String newKey = elementKeyMapping.get(key);
            if (newKey == null) {
                newKey = key;
            }
            outEvent.setValue(newKey, transformElement(map, key, inEvent));
        }

        return outEvent;
    }

    private Object transformElement(TransformationMap map, String key, Event inEvent) throws TransformationException {
        Object value = inEvent.getValue(key);
        IRI iri = getConceptIRI(inEvent, key, value);
        Transformation transformation = map.getTransformation(iri);
        if (transformation == null) {
            return value;
        }
        return transformation.transform(map, value);
    }

    private IRI getConceptIRI(Event inEvent, String key, Object value) {
        try {
            return TransformationUtil.extractConceptIRI(value);
        } catch (IllegalStateException e) {
            return getConceptIRIFromGetter(inEvent, key);
        }
    }

    private IRI getConceptIRIFromGetter(Event inEvent, String key) {
        Class<? extends Event> clazz = inEvent.getClass();
        try {
            Method getter = clazz.getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length()));
            ConceptIRI annotation = getter.getAnnotation(ConceptIRI.class);
            return IRI.create(annotation.value());
        } catch (Exception e) {
            throw new IllegalStateException("Could not retrieve conceptIRI for event element by getter: " + key);
        }
    }

    protected abstract String transformName(Event inEvent);

    protected abstract String transformDomain(Event inEvent);

    protected abstract String transformToolConnector(Event inEvent);

    protected abstract Class<? extends Event> getTargetEventClass(Event inEvent);

    public void setElementKeyMapping(Map<String, String> elementKeyMapping) {
        this.elementKeyMapping = elementKeyMapping;
    }

}
