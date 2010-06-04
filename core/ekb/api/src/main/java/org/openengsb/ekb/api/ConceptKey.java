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

public class ConceptKey {

    private String id;

    private String version;

    @SuppressWarnings("unused")
    private ConceptKey() {
        // for instantiation via reflection
    }

    public ConceptKey(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConceptKey)) {
            return false;
        }
        ConceptKey other = (ConceptKey) obj;
        return id.equals(other.getId()) && version.equals(other.getVersion());
    }

    @Override
    public int hashCode() {
        return 17 + id.hashCode() * 31 + version.hashCode();
    }

    @Override
    public String toString() {
        return "[ConceptKey - id: " + id + ", version: " + version + "]";
    }

}
