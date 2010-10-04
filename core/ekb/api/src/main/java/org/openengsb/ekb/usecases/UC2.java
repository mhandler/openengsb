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
package org.openengsb.ekb.usecases;

import java.util.List;

import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.NoSuchSoftRefernceException;

public class UC2 {

    private static EKB ekb = null;

    public static void main(String[] args) throws Exception {
        ConceptKey reqConceptKey = new ConceptKey("requirement", "1.0.0");
        ConceptKey issueConceptKey = new ConceptKey("issue", "1.0.0");

        Requirement req = null;

        List<String> refs = ekb.getSoftReferenceIds(reqConceptKey, issueConceptKey);
        List<Issue> issues = ekb.follow(reqConceptKey, Requirement.class, refs.get(0), req, issueConceptKey,
                Issue.class);

        for (Issue issue : issues) {
            handleIssue(issueConceptKey, issue);
        }

    }

    private static void handleIssue(ConceptKey issueConceptKey, Issue issue) throws NoSuchConceptException,
            NoSuchSoftRefernceException {
        upateIssue(issue);
        ConceptKey devConceptKey = new ConceptKey("Developer", "1.0.0");
        List<String> refs = ekb.getSoftReferenceIds(issueConceptKey, devConceptKey);
        List<Developer> developers = ekb.follow(issueConceptKey, Issue.class, refs.get(0), issue, devConceptKey,
                Developer.class);
        for (Developer dev : developers) {
            notify(dev);
        }
    }

    private static void upateIssue(Issue issue) {
        // reopen closed issues (and resolved issues)
        // add comment to open issues (have to be reviewed)
        // add comment to inProgress issues
    }

    private static void notify(Developer dev) {
        // notify developer that requirement has changed

    }
}
