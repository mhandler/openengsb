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

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.openengsb.ekb.api.Concept;

public class SimpleConceptSource extends AbstractConceptSource {

    private List<Concept<?>> providedConcepts;

    @SuppressWarnings("unused")
    private SimpleConceptSource() {
        // this constructor is used by infrastructure components instantiating
        // this class by reflection
        providedConcepts = new ArrayList<Concept<?>>();
    }

    public SimpleConceptSource(List<Concept<?>> providedConcepts, String id, QName service) {
        this.providedConcepts = new ArrayList<Concept<?>>(providedConcepts);
        super.setId(id);
        super.setServiceName(service.getLocalPart());
        super.setUrn(service.getNamespaceURI());
    }

    @Override
    public boolean canProvide(Concept<?> concept) {
        return providedConcepts.contains(concept);
    }

}
