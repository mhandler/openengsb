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
package org.openengsb.drools.model;

import org.openengsb.ekb.annotations.Concept;
import org.openengsb.ekb.annotations.Key;
import org.openengsb.ekb.annotations.ReferenceId;

@Concept(id = "issue", version = "1.0.0")
public class Issue {

    @Key
    private String id;

    @ReferenceId(targetConceptId = "developer", targetConceptVersion = "1.0.0", regexp = ".+")
    private String assignee;

    @ReferenceId(targetConceptId = "requirement", targetConceptVersion = "1.0.0", regexp = "#requirement\\((.+)\\)")
    private String description;

    private Issue() {
    }

    public Issue(String id, String assignee, String description) {
        this();
        this.id = id;
        this.assignee = assignee;
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
