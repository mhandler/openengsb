package org.openengsb.ekb.api;

import java.util.List;

public class DomainQueryTranslator<T> implements DomainQueryInterface {

    private Class<T> interfaceClass;

    private T interfaceImplementation;

    @Override
    @SuppressWarnings("unchecked")
    public <U> List<U> getAll(Class<U> type) {
        try {
            return (List<U>) interfaceClass.getMethod("getAll" + type.getName()).invoke(interfaceImplementation);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U getByKey(Class<U> type, String key) {
        try {
            return (U) interfaceClass.getMethod("getByKey" + type.getName()).invoke(interfaceImplementation);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setInterfaceImplementation(T interfaceImplementation) {
        this.interfaceImplementation = interfaceImplementation;
    }

}
