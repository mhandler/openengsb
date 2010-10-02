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
package org.openengsb.core.endpoints;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.openengsb.core.MethodCallHelper;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.ekb.api.conceptSource.ConceptSource;
import org.openengsb.ekb.api.conceptSource.ConceptSourceManager;
import org.openengsb.ekb.api.conceptSource.DomainQueryInterface;

public abstract class DataSourceEndpoint<T extends DomainQueryInterface> extends LinkingEndpoint<T> {

    public abstract ConceptSource getConceptSource();

    @Override
    public void activate() throws Exception {
        super.activate();
        MethodCallHelper.sendMethodCall(this, getEKBEndpoint(), getActivateMethod(),
                new Object[] { getConceptSource() }, new MessageProperties("", UUID.randomUUID().toString()));
    }

    @Override
    public void deactivate() throws Exception {
        super.deactivate();
        MethodCallHelper.sendMethodCall(this, getEKBEndpoint(), getDeactivateMethod(),
                new Object[] { getConceptSource() }, new MessageProperties("", UUID.randomUUID().toString()));
    }

    private Method getActivateMethod() {
        try {
            return ConceptSourceManager.class.getMethod("activate", ConceptSource.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getDeactivateMethod() {
        try {
            return ConceptSourceManager.class.getMethod("deactivate", ConceptSource.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private QName getEKBEndpoint() {
        return new QName("urn:openengsb:ekb", "conceptSourceManagerService");
    }

}
