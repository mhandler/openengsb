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

package org.openengsb.core.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.openengsb.core.InvocationFailedException;

@ConceptIRI("http://www.openengsb.org/ekb/ekbConcepts.owl#ServiceCall")
public class MethodCall {
    private final String methodName;
    private final Value[] arguments;

    public MethodCall(String methodName, Value[] arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public MethodCall(Method method, Object[] args) {
        this(method.getName(), extractArguments(method, args));
    }

    private static Value[] extractArguments(Method method, Object[] args) {
        Value[] arguments = new Value[args.length];
        for (int i = 0; i < args.length; i++) {
            arguments[i] = new Value(args[i], method.getParameterTypes()[i], getConceptIRI(method, i));
        }
        return arguments;
    }

    private static String getConceptIRI(Method method, int argIndex) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] argAnnotations = parameterAnnotations[argIndex];
        for (Annotation annotation : argAnnotations) {
            if (annotation instanceof ConceptIRI) {
                return ((ConceptIRI) annotation).value();
            }
        }
        // TODO throw exception
        return "http://openengsb.org/null-concept";
        // throw new
        // IllegalStateException("No conceptIRI found for parameter with index: "
        // + argIndex + " of method: "
        // + method);
    }

    public ReturnValue invoke(Object instance) throws InvocationFailedException {
        try {
            Class<?> clazz = instance.getClass();
            Method method = clazz.getMethod(methodName, getTypes());
            Object result = method.invoke(instance, getArgValues());
            String returnValueConcept = getReturnValueConceptIRI(method);
            Value val = new Value(result, method.getReturnType(), returnValueConcept);
            return new ReturnValue(val);
        } catch (SecurityException e) {
            throwException(e);
        } catch (NoSuchMethodException e) {
            throwException(e);
        } catch (IllegalArgumentException e) {
            throwException(e);
        } catch (IllegalAccessException e) {
            throwException(e);
        } catch (InvocationTargetException e) {
            throwException(e);
        }

        return null; // unreachable
    }

    private String getReturnValueConceptIRI(Method method) {
        ReturnValueConceptIRI annotation = method.getAnnotation(ReturnValueConceptIRI.class);
        if (annotation == null) {
            // TODO throw exception
            return "http://openengsb.org/null-concept";
            // throw new
            // IllegalStateException("No returnValueConceptIRI found for method: "
            // + method);
        }
        return annotation.value();
    }

    private void throwException(Throwable cause) throws InvocationFailedException {
        throw new InvocationFailedException(String.format("Invocation failed for method '%s' %s", methodName, Arrays
                .toString(getTypes())), cause);
    }

    private Class<?>[] getTypes() {
        Class<?>[] types = new Class<?>[arguments.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = arguments[i].getType();
        }
        return types;
    }

    private Object[] getArgValues() {
        Object[] values = new Object[arguments.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = arguments[i].getValue();
        }
        return values;
    }

    public String getMethodName() {
        return methodName;
    }

    public Value[] getArguments() {
        return arguments;
    }

}