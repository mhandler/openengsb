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
package org.openengsb.ekb.core.transformation.mappings;

import java.lang.reflect.Constructor;

import org.openengsb.ekb.api.Transformer;

public class TransformerFieldMapping extends AbstractMapping {

    private String transformer;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T transform(Object source, Class<T> targetType) {
        Transformer instance = createTransformer();
        return (T) instance.transform(source, targetType);
    }

    private Transformer createTransformer() {
        try {
            Class<?> transformerClass = Class.forName(transformer);
            return (Transformer) getInstance(transformerClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public String getTransformer() {
        return transformer;
    }

    public Object getInstance(Class<?> clazz) throws Exception {
        Constructor<?> noArgConstructor = clazz.getDeclaredConstructor();
        boolean accessible = noArgConstructor.isAccessible();
        noArgConstructor.setAccessible(true);
        Object o = noArgConstructor.newInstance();
        noArgConstructor.setAccessible(accessible);
        return o;
    }

}
