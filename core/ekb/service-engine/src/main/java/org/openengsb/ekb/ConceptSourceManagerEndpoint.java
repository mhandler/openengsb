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
package org.openengsb.ekb;

import org.openengsb.contextcommon.ContextHelper;
import org.openengsb.core.endpoints.LinkingEndpoint;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.ekb.api.ConceptSourceManager;

/**
 * @org.apache.xbean.XBean element="conceptSourceManagerEndpoint"
 *                         description="EKB ConceptSourceManager Endpoint"
 */
public class ConceptSourceManagerEndpoint extends LinkingEndpoint<ConceptSourceManager> {

    private ConceptSourceManager conceptSourceManager;

    @Override
    protected ConceptSourceManager getImplementation(ContextHelper contextHelper, MessageProperties msgProperties) {
        return conceptSourceManager;
    }

    public void setConceptSourceManager(ConceptSourceManager conceptSourceManager) {
        this.conceptSourceManager = conceptSourceManager;
    }
}
