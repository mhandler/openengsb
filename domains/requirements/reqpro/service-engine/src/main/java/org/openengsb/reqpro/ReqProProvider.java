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
package org.openengsb.reqpro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.drools.model.Requirement;
import org.openengsb.ekb.api.conceptSource.SingleConceptProvider;

public class ReqProProvider extends SingleConceptProvider<Requirement> {

    private Map<String, Requirement> data = new HashMap<String, Requirement>();

    public ReqProProvider() {
        fillWithTestData();
    }

    private void fillWithTestData() {
        data.put("req1", new Requirement("req1"));
        data.put("req2", new Requirement("req2"));
    }

    @Override
    public List<Requirement> getAll() {
        return new ArrayList<Requirement>(data.values());
    }

    @Override
    public Requirement getByKey(Object key) {
        return data.get(key);
    }

}
