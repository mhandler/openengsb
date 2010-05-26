package org.openengsb.ekb.api;

public class Concept<T> {

    private String id;

    private String domain;

    private Class<T> conceptClass;

    public String getId() {
        return id;
    }

    public Class<T> getConceptClass() {
        return conceptClass;
    }

    public String getDomain() {
        return domain;
    }

}
