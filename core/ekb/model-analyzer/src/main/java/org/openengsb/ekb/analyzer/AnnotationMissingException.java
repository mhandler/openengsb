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
package org.openengsb.ekb.analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationMissingException extends Exception {

    public AnnotationMissingException(Class<?> annotatedClass, Class<? extends Annotation> annotation) {
        super("Annotation " + annotation + " is missing on class " + annotatedClass);
    }

    public AnnotationMissingException(Field annotatedField, Class<? extends Annotation> annotation) {
        super("Annotation " + annotation + " is missing on field " + annotatedField);
    }

    public AnnotationMissingException(String message) {
        super(message);
    }

    public AnnotationMissingException() {
        super();
    }

    public AnnotationMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationMissingException(Throwable cause) {
        super(cause);
    }

}
