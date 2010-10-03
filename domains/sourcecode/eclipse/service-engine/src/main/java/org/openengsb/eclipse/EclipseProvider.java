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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.ekb.api.conceptSource.SingleConceptProvider;

public class EclipseProvider extends SingleConceptProvider<SWSensor> {

    private Map<String, SWSensor> data = new HashMap<String, SWSensor>();

    public EclipseProvider() {
        fillWithTestData();
    }

    private void fillWithTestData() {
        data.put("sensor1", new SWSensor("sensor1", "mbar", "int"));
        data.put("sensor2", new SWSensor("sensor2", "bool", "boolean"));
    }

    @Override
    public List<SWSensor> getAll() {
        return new ArrayList<SWSensor>(data.values());
    }

    @Override
    public SWSensor getByKey(Object key) {
        return data.get(key);
    }

}
