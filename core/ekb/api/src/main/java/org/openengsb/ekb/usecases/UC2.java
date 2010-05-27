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
