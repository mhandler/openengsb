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

import org.openengsb.ekb.annotations.Concept;
import org.openengsb.ekb.annotations.Key;
import org.openengsb.ekb.annotations.MapsTo;
import org.openengsb.ekb.annotations.SuperConcept;
import org.openengsb.ekb.annotations.Transformation;

@Concept(id = "eeSensor", version = "1.0.0")
@SuperConcept(id = "sensor", version = "1.0.0")
public class EeSensor {

    @Key
    @MapsTo("id")
    private String id;

    @MapsTo("unit")
    private String unit;

    @MapsTo("type")
    @Transformation(RangeToTypeTransformer.class)
    private String range;

    @MapsTo("connectionId")
    private String connectionId;

    public String getId() {
        return id;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getRange() {
        return range;
    }

    public String getUnit() {
        return unit;
    }
}
