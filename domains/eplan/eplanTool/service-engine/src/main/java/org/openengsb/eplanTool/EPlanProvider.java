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
package org.openengsb.eplanTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.ekb.api.conceptSource.SingleConceptProvider;

public class EPlanProvider extends SingleConceptProvider<EPlanSensor> {

    private Map<String, EPlanSensor> data = new HashMap<String, EPlanSensor>();

    public EPlanProvider() {
        fillWithTestData();
    }

    private void fillWithTestData() {
        data.put("s1", new EPlanSensor("s1", "mbar", "[*,*]", "sensor1"));
        data.put("s2", new EPlanSensor("s2", "mbar", "[*,*]", "sensor1"));
        data.put("s3", new EPlanSensor("s2", "mbar", "[*,*]", "sensor1"));
        data.put("s4", new EPlanSensor("s2", "bool", "[0,1]", "sensor2"));
        data.put("s5", new EPlanSensor("s2", "bool", "[0,1]", "sensor2"));
        data.put("s6", new EPlanSensor("s2", "bool", "[0,1]", "sensor2"));
    }

    @Override
    public List<EPlanSensor> getAll() {
        return new ArrayList<EPlanSensor>(data.values());
    }

    @Override
    public EPlanSensor getByKey(Object key) {
        return data.get(key);
    }

}
