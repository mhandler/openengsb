package org.openengsb.ekb.api;

public class NoSuchConceptException extends Exception {

    public NoSuchConceptException(String message) {
        super(message);
    }

    public NoSuchConceptException() {
        super();
    }

    public NoSuchConceptException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchConceptException(Throwable cause) {
        super(cause);
    }

}
