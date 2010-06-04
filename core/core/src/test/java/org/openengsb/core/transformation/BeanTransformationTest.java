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
package org.openengsb.core.transformation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.core.xmlmapping.XMLMappable;

public class BeanTransformationTest {

    private ToXmlTypesTransformer toXmlTranformer;

    private FromXmlTypesTransformer fromXmlTransformer;

    @Before
    public void setUp() {
        this.toXmlTranformer = new ToXmlTypesTransformer();
        this.fromXmlTransformer = new FromXmlTypesTransformer();
    }

    @Test
    public void testTransformWithInheritance() {
        SubBean subBean = new SubBean("foo", "bar", "buz");
        XMLMappable mappable = toXmlTranformer.toMappable(subBean);
        SubBean result = (SubBean) fromXmlTransformer.toObject(mappable);

        Assert.assertEquals("foo", result.getX());
        Assert.assertEquals("bar", result.getY());
        Assert.assertEquals("buz", result.getZ());
    }

    public static class SomeBean {
        private String x;

        private String y;

        public SomeBean() {
        }

        public SomeBean(String x, String y) {
            this.x = x;
            this.y = y;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }

    }

    public static class SubBean extends SomeBean {
        private String z;

        public SubBean() {
        }

        public SubBean(String x, String y, String z) {
            super(x, y);
            this.z = z;
        }

        public String getZ() {
            return z;
        }
    }
}
