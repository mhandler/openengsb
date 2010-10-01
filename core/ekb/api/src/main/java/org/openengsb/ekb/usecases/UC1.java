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

import java.util.ArrayList;
import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;
import org.openengsb.ekb.api.NoSuchConceptException;

public class UC1 {

    private static EKB ekb = null;

    public static void main(String[] args) throws NoSuchConceptException {

        Concept<Sensor> sensorConcept = ekb.getConcept(new ConceptKey("#Sensor", "1.0.0"), Sensor.class);
        List<ConceptSource> sensorSources = ekb.getSources(sensorConcept);

        // test whether sw tool and electrical engineering tool is present

        ConceptSource sweSource = getSource("sweTool", sensorSources);
        ConceptSource eeSource = getSource("eeTool", sensorSources);

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
    }

    private static void testTypeAndUnit(Sensor sweSensor, Sensor eeSensor) {
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

    private static void reportProblem(String string) {
        // TODO Auto-generated method stub
    }

    private static List<Sensor> getConnectedSensors(Sensor sensor, List<Sensor> sensors) {
        List<Sensor> result = new ArrayList<Sensor>();
        for (Sensor other : sensors) {
            if (other.getConnectionId().equals(sensor.getConnectionId())) {
                result.add(other);
            }
        }
        return result;
    }

    private static ConceptSource getSource(String sourceId, List<ConceptSource> sources) {
        for (ConceptSource source : sources) {
            if (source.getId().equals(sourceId)) {
                return source;
            }
        }
        throw new IllegalStateException("No concept source with ID '" + sourceId + "' active.");
    }

}
