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
package org.openengsb.eplanTool;

public class EPlanSensor {

    private String id;

    private String unit;

    private String range;

    private String connectionId;

    private EPlanSensor() {
    }

    public EPlanSensor(String id, String unit, String range, String connectionId) {
        this();
        this.id = id;
        this.unit = unit;
        this.range = range;
        this.connectionId = connectionId;
    }

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
