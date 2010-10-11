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
package org.openengsb.devtool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.drools.model.Developer;
import org.openengsb.ekb.api.conceptSource.SingleConceptProvider;

public class DevToolProvider extends SingleConceptProvider<Developer> {

    private Map<String, Developer> data = new HashMap<String, Developer>();

    public DevToolProvider() {
        fillWithTestData();
    }

    private void fillWithTestData() {
        data.put("dev1", new Developer("dev1", "email@example.com"));
        data.put("dev2", new Developer("dev2", "email@example.com"));
    }

    @Override
    public List<Developer> getAll() {
        return new ArrayList<Developer>(data.values());
    }

    @Override
    public Developer getByKey(Object key) {
        return data.get(key);
    }

}
