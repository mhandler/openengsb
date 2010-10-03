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

import org.openengsb.contextcommon.ContextHelper;
import org.openengsb.drools.NotificationDomain;
import org.openengsb.drools.SensorValidityCheck;
import org.openengsb.drools.model.Notification;
import org.openengsb.drools.model.Sensor;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.conceptSource.ConceptSource;

public class SensorValidityCheckImpl implements SensorValidityCheck {

    private EKB ekb;

    private NotificationDomain notification;

    private ContextHelper ctx;

    public void check() {
        try {
            Concept<Sensor> sensorConcept;
            sensorConcept = ekb.getConcept(new ConceptKey("sensor", "1.0.0"), Sensor.class);
            List<ConceptSource> sensorSources = ekb.getSources(sensorConcept);

            // test whether sw tool and electrical engineering tool is present

            ConceptSource sweSource = getSource("eclipse", sensorSources);
            ConceptSource eeSource = getSource("eplan", sensorSources);

            // test three sources
            List<Sensor> sweSensors = ekb.getData(sweSource, sensorConcept);
            List<Sensor> eeSensors = ekb.getData(eeSource, sensorConcept);

            for (Sensor sweSensor : sweSensors) {
                List<Sensor> connected = getConnectedSensors(sweSensor, eeSensors);
                if (connected.size() != 3) {
                    reportProblem("Software Sensor '" + sweSensor.getId() + "' is connected to " + connected.size()
                            + " sensors in the electrical engineering tool, but should be 3.");
                }
            }

            // test no double connect
            for (Sensor eeSensor : eeSensors) {
                List<Sensor> connected = getConnectedSensors(eeSensor, sweSensors);
                if (connected.size() != 1) {
                    reportProblem("Physical Sensor '" + eeSensor.getId() + "' is connected to " + connected.size()
                            + " sensors in the software engineering tool, but should be 1.");
                }
            }

            // test type and unit valid
            for (Sensor sweSensor : sweSensors) {
                List<Sensor> connected = getConnectedSensors(sweSensor, eeSensors);
                for (Sensor eeSensor : connected) {
                    testTypeAndUnit(sweSensor, eeSensor);
                }
            }
        } catch (NoSuchConceptException e) {
            throw new RuntimeException(e);
        }
    }

    private void testTypeAndUnit(Sensor sweSensor, Sensor eeSensor) {
        if (!sweSensor.getType().equals(eeSensor.getType())) {
            reportProblem("Software sensor '" + sweSensor.getId() + "' and electrical engineering sensor '" + eeSensor
                    + "' are connected but have a different sensor type. '" + sweSensor.getType() + "' vs '"
                    + eeSensor.getType() + "'");
        }
        if (!sweSensor.getUnit().equals(eeSensor.getUnit())) {
            reportProblem("Software sensor '" + sweSensor.getId() + "' and electrical engineering sensor '" + eeSensor
                    + "' are connected but have a different sensor unit. '" + sweSensor.getUnit() + "' vs '"
                    + eeSensor.getUnit() + "'");
        }
    }

    private void reportProblem(String message) {
        Notification n = new Notification();
        n.setSubject("sensor validity problem");
        n.setMessage(message);
        n.setRecipient(ctx.getValue("workflows/uc1/notification/recipient"));
        notification.notify(n);
    }

    private List<Sensor> getConnectedSensors(Sensor sensor, List<Sensor> sensors) {
        List<Sensor> result = new ArrayList<Sensor>();
        for (Sensor other : sensors) {
            if (other.getConnectionId().equals(sensor.getConnectionId())) {
                result.add(other);
            }
        }
        return result;
    }

    private ConceptSource getSource(String sourceId, List<ConceptSource> sources) {
        for (ConceptSource source : sources) {
            if (source.getId().equals(sourceId)) {
                return source;
            }
        }
        throw new IllegalStateException("No concept source with ID '" + sourceId + "' active.");
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
