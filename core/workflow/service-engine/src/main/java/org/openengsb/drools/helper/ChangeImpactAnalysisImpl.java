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
package org.openengsb.drools.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openengsb.contextcommon.ContextHelper;
import org.openengsb.drools.NotificationDomain;
import org.openengsb.drools.RequirementChangeImpactAnalysis;
import org.openengsb.drools.model.Developer;
import org.openengsb.drools.model.Issue;
import org.openengsb.drools.model.Issue.Status;
import org.openengsb.drools.model.Notification;
import org.openengsb.drools.model.Requirement;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.NoSuchSoftRefernceException;

public class ChangeImpactAnalysisImpl implements RequirementChangeImpactAnalysis {

    private static Log log = LogFactory.getLog(ChangeImpactAnalysisImpl.class);

    private EKB ekb;

    private NotificationDomain notification;

    private ContextHelper ctx;

    public void check(String reqId) {
        try {
            ConceptKey reqConceptKey = new ConceptKey("requirement", "1.0.0");
            ConceptKey issueConceptKey = new ConceptKey("issue", "1.0.0");

            log.info("Get requirement sources from EKB.");
            List<String> requirementSources = ekb.getSourceIds(reqConceptKey);
            if (requirementSources.isEmpty()) {
                reportProblem("No concept sources for requirement concept of version 1.0.0");
                return;
            }

            log.info("Get requirement with id '" + reqId + "' from requirement sources.");
            Requirement req = null;
            for (String source : requirementSources) {
                req = ekb.getDataElement(source, reqConceptKey, Requirement.class, reqId);
            }
            if (req == null) {
                reportProblem("No requirement with id '" + reqId + "' could be found.");
                return;
            }

            log.info("Get issue sources from EKB.");
            List<String> issuesSources = ekb.getSourceIds(issueConceptKey);
            if (issuesSources.isEmpty()) {
                reportProblem("No concept sources for issue concept of version 1.0.0");
                return;
            }

            log.info("Get all issues from EKB.");
            List<Issue> issues = new ArrayList<Issue>();
            for (String source : issuesSources) {
                issues.addAll(ekb.getData(source, issueConceptKey, Issue.class));
            }
            log.info("Issues found: " + issues);

            log.info("Use soft references to get affected issues.");
            List<Issue> affectedIssues = new ArrayList<Issue>();
            for (Issue issue : issues) {
                List<String> refs = ekb.getSoftReferenceIds(issueConceptKey, reqConceptKey);
                List<Requirement> reqs = ekb.follow(issueConceptKey, Issue.class, refs.get(0), issue, reqConceptKey,
                        Requirement.class);
                if (reqs.contains(req)) {
                    affectedIssues.add(issue);
                }
            }

            log.info("The following issues are affected: " + affectedIssues);
            for (Issue issue : affectedIssues) {
                handleIssue(issueConceptKey, issue, req);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void reportProblem(String message) {
        log.info("Reporting problem: " + message);
        String recipient = ctx.getValue("workflows/uc2/notification/recipient");
        Notification n = new Notification();
        n.setRecipient(recipient);
        n.setMessage(message);
        n.setSubject("Change Impace Analysis could not be performed because of unsuspected problem");
        notification.notify(n);
    }

    private void handleIssue(ConceptKey issueConceptKey, Issue issue, Requirement req) throws NoSuchConceptException,
            NoSuchSoftRefernceException {
        upateIssue(issue);
        ConceptKey devConceptKey = new ConceptKey("developer", "1.0.0");
        log.info("Get affected developers for issue: " + issue.getId());
        List<String> refs = ekb.getSoftReferenceIds(issueConceptKey, devConceptKey);
        List<Developer> developers = ekb.follow(issueConceptKey, Issue.class, refs.get(0), issue, devConceptKey,
                Developer.class);
        log.info("Affected developers for issue '" + issue.getId() + "' are: " + developers);
        for (Developer dev : developers) {
            notify(dev, req, issue);
        }
    }

    private void upateIssue(Issue issue) {
        if (issue.getStatus() == Status.CLOSED) {
            reportIssueStateChange("Issue " + issue.getId() + " has to be reviewed and possibly reopened.");
        }
        if (issue.getStatus() == Status.OPEN) {
            reportIssueStateChange("Issue " + issue.getId() + " has to be reviewed.");
        }
        if (issue.getStatus() == Status.IN_PROGRESS) {
            reportIssueStateChange("Issue " + issue.getId() + " has to be reviewed before work can continue.");
        }
    }

    private void notify(Developer dev, Requirement req, Issue issue) {
        log.info("Notifying developer " + dev);
        String recipient = dev.getEmail();
        Notification n = new Notification();
        n.setRecipient(recipient);
        n.setMessage("Due to a change of requirement " + req.getId() + " an issue assigned to you has changed: "
                + issue.getId());
        n.setSubject("Requirement Change");
        notification.notify(n);
    }

    private void reportIssueStateChange(String message) {
        log.info("Reporting issue state update: " + message);
        String recipient = ctx.getValue("workflows/uc2/notification/recipient");
        Notification n = new Notification();
        n.setRecipient(recipient);
        n.setMessage(message);
        n.setSubject("Issue update due to requirement change");
        notification.notify(n);
    }

    public void setEkb(EKB ekb) {
        this.ekb = ekb;
    }

    public void setNotification(NotificationDomain notification) {
        this.notification = notification;
    }

    public void setCtx(ContextHelper ctx) {
        this.ctx = ctx;
    }

}
