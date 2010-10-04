package org.openengsb.ekb.analyzer;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptKey;
import org.openengsb.ekb.api.NoSuchConceptException;
import org.openengsb.ekb.api.conceptSource.ConceptSource;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeChangeListener;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeManager;

public class ModelAnalyzerTest {

    private ModelAnalyzer analyzer;

    private List<Concept<?>> concepts;

    @Before
    public void setUp() {
        analyzer = new ModelAnalyzer();
        analyzer.setKnowledgeManager(new MockKnowledgeManager());
    }

    @Test
    public void testAnalyzeAndStore() throws URISyntaxException {
        URL modelFile = ClassLoader.getSystemResource("knowledge-model.jar");
        analyzer.analyzeAndStore(new File(modelFile.toURI()));
        Assert.assertEquals(3, concepts.size());
    }

    private class MockKnowledgeManager implements KnowledgeManager {

        @Override
        public List<Concept<?>> getActiveConcepts() {
            return null;
        }

        @Override
        public Concept<?> getActiveConcept(ConceptKey key) throws NoSuchConceptException {
            return null;
        }

        @Override
        public List<Concept<?>> getActiveConcepts(ConceptSource source) {
            return null;
        }

        @Override
        public List<Concept<?>> getInactiveConcepts() {
            return null;
        }

        @Override
        public List<Concept<?>> getInactiveConcepts(ConceptSource source) {
            return null;
        }

        @Override
        public void setConcepts(List<Concept<?>> concepts) {
            ModelAnalyzerTest.this.concepts = concepts;
        }

        @Override
        public void deactivateConcepts(List<Concept<?>> concepts) {

        }

        @Override
        public void activateConcepts(List<Concept<?>> concepts) {

        }

        @Override
        public void addListener(KnowledgeChangeListener listener) {

        }

        @Override
        public void removeListener(KnowledgeChangeListener listener) {

        }

    }

}
