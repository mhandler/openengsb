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
package org.openengsb.trac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.drools.model.Issue;
import org.openengsb.drools.model.Issue.Status;
import org.openengsb.ekb.api.conceptSource.SingleConceptProvider;

public class TracProvider extends SingleConceptProvider<Issue> {

    private Map<String, Issue> data = new HashMap<String, Issue>();

    public TracProvider() {
        fillWithTestData();
    }

    private void fillWithTestData() {
        data.put("issue1", new Issue("issue1", "dev1",
                "This issue is related to #requirement(req1) and #requirement(req2).", Status.OPEN));
        data.put("issue2", new Issue("issue2", "dev2", "This issue is related to #requirement(req2).", Status.CLOSED));
        data.put("issue3", new Issue("issue2", "dev2", "This issue is related to no requirement.", Status.IN_PROGRESS));
    }

    @Override
    public List<Issue> getAll() {
        return new ArrayList<Issue>(data.values());
    }

    @Override
    public Issue getByKey(Object key) {
        return data.get(key);
    }

}
