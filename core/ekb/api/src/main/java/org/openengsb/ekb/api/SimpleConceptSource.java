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
package org.openengsb.ekb.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;


public class SimpleConceptSource implements ConceptSource {

    private String id;

    private String urn;

    private String serviceName;

    private List<ConceptKey> providedConcepts = new ArrayList<ConceptKey>();

    @Override
    public boolean canProvideSubconcept(Concept<?> concept) {
        for (Concept<?> subConcept : concept.getSubConcepts()) {
            if (canProvide(subConcept) || canProvideSubconcept(subConcept)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canProvide(Concept<?> concept) {
        return providedConcepts.contains(concept.getKey());
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<ConceptKey> getProvidedConcepts() {
        return new ArrayList<ConceptKey>(providedConcepts);
    }

    public void setProvidedConcepts(List<ConceptKey> providedConcepts) {
        this.providedConcepts = new ArrayList<ConceptKey>(providedConcepts);
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getUrn() {
        return urn;
    }

    @Override
    public QName getService() {
        return new QName(urn, serviceName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConceptSource)) {
            return false;
        }
        return id.equals(((ConceptSource) obj).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "[ConceptSource(" + id + ") service:" + getService() + "]";
    }
}
