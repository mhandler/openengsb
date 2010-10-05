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
package org.openengsb.ekb.analyzer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class JarReaderTest {

    private JarReader reader;

    @Before
    public void setUp() throws URISyntaxException {
        URL modelFile = ClassLoader.getSystemResource("knowledge-model.jar");
        reader = new JarReader(modelFile.toURI());
    }

    @Test
    public void testReadInConcepts() throws IOException {
        List<Class<?>> concepts = reader.readInConcepts();
        Assert.assertEquals(3, concepts.size());
    }

}
