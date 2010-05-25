package org.openengsb.ekb.api;

public class Concept<T> {

    private String id;

    private Class<T> conceptClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<T> getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(Class<T> conceptClass) {
        this.conceptClass = conceptClass;
    }

}
