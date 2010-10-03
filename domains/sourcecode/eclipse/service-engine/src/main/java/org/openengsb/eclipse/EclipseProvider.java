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

import org.openengsb.drools.model.SweSensor;
import org.openengsb.ekb.api.conceptSource.SingleConceptProvider;

public class EclipseProvider extends SingleConceptProvider<SweSensor> {

    private Map<String, SweSensor> data = new HashMap<String, SweSensor>();

    public EclipseProvider() {
        fillWithTestData();
    }

    private void fillWithTestData() {
        data.put("sensor1", new SweSensor("sensor1", "mbar", "int"));
        data.put("sensor2", new SweSensor("sensor2", "bool", "boolean"));
    }

    @Override
    public List<SweSensor> getAll() {
        return new ArrayList<SweSensor>(data.values());
    }

    @Override
    public SweSensor getByKey(Object key) {
        return data.get(key);
    }

}
