package org.openengsb.ekb.usecases;

import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.EKB;

public class UC2 {

    private static EKB ekb = null;

    public static void main(String[] args) {
        Concept<Requirement> reqConcept = ekb.getConcept("requirement", Requirement.class);
        Concept<Issue> issueConcept = ekb.getConcept("issue", Issue.class);

        Requirement req = null;

        List<Issue> issues = ekb.followSoftReference(reqConcept, req, issueConcept);

        for (Issue issue : issues) {
            handleIssue(issueConcept, issue);
        }

    }

    private static void handleIssue(Concept<Issue> issueConcept, Issue issue) {
        Concept<Developer> devConcept = ekb.getConcept("Developer", Developer.class);
        List<Developer> developers = ekb.followSoftReference(issueConcept, issue, devConcept);
        for (Developer dev : developers) {
            notify(dev);
        }
    }

    private static void notify(Developer dev) {
        // notify developer that requirement has changed

    }
}
