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
package org.openengsb.eclipse;

import java.util.Arrays;

import org.openengsb.contextcommon.ContextHelper;
import org.openengsb.core.endpoints.DataSourceEndpoint;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.conceptSource.ConceptSource;
import org.openengsb.ekb.api.conceptSource.SimpleConceptSource;

/**
 * @org.apache.xbean.XBean element="conceptSourceEndpoint"
 *                         description="eclipse code introspection concept source endpoint"
 */
public class EclipseDataSourceEndpoint extends DataSourceEndpoint<EclipseProvider> {

    private SimpleConceptSource source;

    @Override
    protected EclipseProvider getImplementation(ContextHelper contextHelper, MessageProperties msgProperties) {
        return new EclipseProvider();
    }

    @Override
    public ConceptSource getConceptSource() {
        if (source == null) {
            init();
        }
        return source;
    }

    private void init() {
        source = new SimpleConceptSource();
        source.setId("eclipse");
        source.setServiceName(getService().getLocalPart());
        source.setUrn(getService().getNamespaceURI());
        source.setProvidedConcepts(Arrays.asList(new ConceptKey[] { new ConceptKey("swSensor", "1.0.0") }));
    }

}
