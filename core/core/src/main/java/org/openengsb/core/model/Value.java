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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((conceptIRI == null) ? 0 : conceptIRI.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Value other = (Value) obj;
        if (conceptIRI == null) {
            if (other.conceptIRI != null)
                return false;
        } else if (!conceptIRI.equals(other.conceptIRI))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
