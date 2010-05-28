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

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.SoftReference;

public class UC2 {

    private static EKB ekb = null;

    public static void main(String[] args) {
        Concept<Requirement> reqConcept = ekb.getConcept("requirement", Requirement.class);
        Concept<Issue> issueConcept = ekb.getConcept("issue", Issue.class);

        Requirement req = null;

        List<SoftReference<Requirement, Issue>> refs = reqConcept.getSoftReferences(issueConcept);
        List<Issue> issues = refs.get(0).follow(ekb, req);

        for (Issue issue : issues) {
            handleIssue(issueConcept, issue);
        }

    }

    private static void handleIssue(Concept<Issue> issueConcept, Issue issue) {
        Concept<Developer> devConcept = ekb.getConcept("Developer", Developer.class);
        List<SoftReference<Issue, Developer>> refs = issueConcept.getSoftReferences(devConcept);
        List<Developer> developers = refs.get(0).follow(ekb, issue);
        for (Developer dev : developers) {
            notify(dev);
        }
    }

    private static void notify(Developer dev) {
        // notify developer that requirement has changed

    }
}
