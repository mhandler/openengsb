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
package org.openengsb.ekb.api.conceptSource;

import java.util.List;


public abstract class SingleConceptProvider<T> implements DomainQueryInterface {

    public abstract List<T> getAll();

    public abstract T getByKey(Object key);

    @Override
    @SuppressWarnings("unchecked")
    public <TYPE> List<TYPE> getAll(Class<TYPE> type) {
        checkType(type);
        return (List<TYPE>) getAll();
    }

    private <TYPE> void checkType(Class<TYPE> type) {
        if (!this.getClass().getGenericInterfaces()[0].equals(type)) {
            throw new IllegalArgumentException("Type does not match generic type.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TYPE> TYPE getByKey(Class<TYPE> type, Object key) {
        checkType(type);
        return (TYPE) getByKey(key);
    }

}
