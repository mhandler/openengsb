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
package org.openengsb.report.plaintext;

import java.io.File;

import org.openengsb.contextcommon.ContextHelper;
import org.openengsb.core.endpoints.LinkingEndpoint;
import org.openengsb.core.messaging.MessageProperties;
import org.openengsb.drools.ReportDomain;

/**
 * @org.apache.xbean.XBean element="plaintextReportEndpoint"
 *                         description="Plaintext Report Endpoint"
 */
public class PlaintextReportEndpoint extends LinkingEndpoint<ReportDomain> {

    @Override
    protected ReportDomain getImplementation(ContextHelper contextHelper, MessageProperties msgProperties) {
        String dirName = contextHelper.getValue("report/plaintext-report/config/reportDirectory");
        return new PlainTextReportDomainImpl(contextHelper, this, msgProperties, new File(dirName));
    }

}
