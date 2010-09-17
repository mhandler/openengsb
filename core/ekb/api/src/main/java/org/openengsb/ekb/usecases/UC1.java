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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, List<Sensor>> data = new HashMap<String, List<Sensor>>();

        for (ConceptSource source : sensorSources) {
            List<Sensor> sensors = ekb.getData(source, sensorConcept);
            data.put(source.getId(), sensors);
        }

        List<Map<String, Sensor>> alignedSensors = alignSensors(data);
        checkThreeSources(alignedSensors);
        checkConsistency(alignedSensors);
    }

    private static List<Map<String, Sensor>> alignSensors(Map<String, List<Sensor>> data) {
        // find the same sensor from different sources
        return null;
    }

    private static void checkThreeSources(List<Map<String, Sensor>> alignedSensors) {
        // check if each software variable representation of a sensor has three
        // physical sources
    }

    private static void checkConsistency(List<Map<String, Sensor>> alignedSensors) {
        // check if the unit of measurement is consistent for each sensor

    }

}
