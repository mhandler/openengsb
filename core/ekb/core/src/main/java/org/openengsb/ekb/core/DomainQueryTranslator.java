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
