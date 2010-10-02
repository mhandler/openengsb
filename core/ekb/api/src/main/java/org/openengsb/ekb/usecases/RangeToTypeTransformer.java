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

import org.openengsb.ekb.api.mapping.Transformer;

public class RangeToTypeTransformer implements Transformer {

    @Override
    public Object transform(Object source, Class<?> targetType) {
        String range = source.toString();
        if (range.equals("[0,1]")) {
            return Sensor.Type.BINARY;
        } else if (range.equals("[*,*]")) {
            return Sensor.Type.INTEGER;
        }
        return Sensor.Type.OTHER;
    }

}
