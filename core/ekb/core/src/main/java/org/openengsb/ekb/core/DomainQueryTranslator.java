package org.openengsb.ekb.core;

import java.util.List;

import org.openengsb.ekb.api.DomainQueryInterface;

public class DomainQueryTranslator<T> implements DomainQueryInterface {

    private Class<T> interfaceClass;

    private T interfaceImplementation;

    @Override
    @SuppressWarnings("unchecked")
    public <U> List<U> getAll(Class<U> type) {
        return (List<U>) invokeMethod("getAll" + type.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U getByKey(Class<U> type, String key) {
        return (U) invokeMethod("getByKey" + type.getName(), String.class);
    }

    private Object invokeMethod(String name, Class<?>... argTypes) {
        try {
            return interfaceClass.getMethod(name, argTypes).invoke(interfaceImplementation);
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
