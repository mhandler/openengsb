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
