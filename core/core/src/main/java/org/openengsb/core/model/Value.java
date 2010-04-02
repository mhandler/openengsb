package org.openengsb.core.model;


public class Value {

    private final Object value;

    private final Class<?> type;

    private final String conceptIRI;

    public Value(Object value, Class<?> type, String conceptIRI) {
        this.value = value;
        this.type = type;
        this.conceptIRI = conceptIRI;
    }

    public Class<?> getType() {
        return type;
    }

    public String getConceptIRI() {
        return conceptIRI;
    }

    public Object getValue() {
        return value;
    }

}
