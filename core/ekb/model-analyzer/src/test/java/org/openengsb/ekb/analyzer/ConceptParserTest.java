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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.annotations.Concept;
import org.openengsb.ekb.annotations.Key;
import org.openengsb.ekb.annotations.MapsTo;
import org.openengsb.ekb.annotations.ReferenceId;
import org.openengsb.ekb.annotations.SuperConcept;
import org.openengsb.ekb.annotations.Transformation;
import org.openengsb.ekb.api.FieldMapping;
import org.openengsb.ekb.api.Transformer;
import org.openengsb.ekb.core.softreferences.RegexSoftReference;
import org.openengsb.ekb.core.transformation.mappings.AutomaticMapping;
import org.openengsb.ekb.core.transformation.mappings.TransformerFieldMapping;

public class ConceptParserTest {

    private ConceptParser parser;

    @Before
    public void setUp() {
        this.parser = new ConceptParser();
    }

    @Test
    public void testParseNoConcept() throws AnnotationMissingException {
        List<org.openengsb.ekb.api.Concept<?>> concepts = parser.parseConcepts(new ArrayList<Class<?>>());
        Assert.assertTrue(concepts.isEmpty());
    }

    @Test(expected = AnnotationMissingException.class)
    public void testParseFailure() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(TestObject.class);
        parser.parseConcepts(classes);
    }

    @Test
    public void testParseSimpleConcept() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(SimpleConcept.class);
        List<org.openengsb.ekb.api.Concept<?>> concepts = parser.parseConcepts(classes);
        Assert.assertEquals(1, concepts.size());
        Assert.assertEquals("SimpleConcept", concepts.get(0).getKey().getId());
        Assert.assertEquals(SimpleConcept.class, concepts.get(0).getConceptClass());
    }

    @Test
    public void testParseSuperConcept() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(ConceptWithSuperConcept.class);
        classes.add(SimpleConcept.class);
        List<org.openengsb.ekb.api.Concept<?>> concepts = parser.parseConcepts(classes);

        org.openengsb.ekb.api.Concept<?> concept = getConcept(concepts, "ConceptWithSuperConcept");

        Assert.assertEquals(2, concepts.size());
        Assert.assertEquals("ConceptWithSuperConcept", concept.getKey().getId());
        Assert.assertEquals(ConceptWithSuperConcept.class, concept.getConceptClass());

        Assert.assertEquals("SimpleConcept", concept.getSuperConcept().getKey().getId());
        Assert.assertEquals(SimpleConcept.class, concept.getSuperConcept().getConceptClass());
    }

    @Test(expected = AnnotationMissingException.class)
    public void testParseIllegalSoftReferences() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(SimpleConcept.class);
        classes.add(ConceptWithSuperConcept.class);
        classes.add(ConceptWithIllegalSoftReferences.class);
        parser.parseConcepts(classes);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testParseSoftReferences() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(ConceptWithSoftReferences.class);
        classes.add(SimpleConcept.class);
        classes.add(ConceptWithSoftReferences2.class);
        List<org.openengsb.ekb.api.Concept<?>> concepts = parser.parseConcepts(classes);

        org.openengsb.ekb.api.Concept<?> concept = getConcept(concepts, "ConceptWithSoftReferences");
        org.openengsb.ekb.api.Concept<?> simpleConcept = getConcept(concepts, "SimpleConcept");
        org.openengsb.ekb.api.Concept<?> otherConcept = getConcept(concepts, "ConceptWithSoftReferences2");

        Assert.assertEquals(3, concepts.size());
        Assert.assertEquals("ConceptWithSoftReferences", concept.getKey().getId());
        Assert.assertEquals(ConceptWithSoftReferences.class, concept.getConceptClass());

        Assert.assertEquals(2, concept.getSoftReferences().size());
        Assert.assertEquals(1, concept.getSoftReferences(simpleConcept).size());
        Assert.assertEquals(1, concept.getSoftReferences(otherConcept).size());

        Assert.assertEquals(simpleConcept, concept.getSoftReferences(simpleConcept).get(0).getTargetConcept());
        Assert.assertEquals(otherConcept, concept.getSoftReferences(otherConcept).get(0).getTargetConcept());

        Assert.assertEquals(RegexSoftReference.class, concept.getSoftReferences(simpleConcept).get(0).getClass());
        Assert.assertEquals(RegexSoftReference.class, concept.getSoftReferences(otherConcept).get(0).getClass());

        Assert.assertEquals("#ref\\(([\\d]+)\\)",
                ((RegexSoftReference) concept.getSoftReferences(simpleConcept).get(0)).getRegex());
        Assert.assertEquals("#someRef\\(([a-z,A-Z]+)\\)", ((RegexSoftReference) concept.getSoftReferences(otherConcept)
                .get(0)).getRegex());

        Assert.assertEquals("refField1",
                ((RegexSoftReference) concept.getSoftReferences(simpleConcept).get(0)).getReferenceField());
        Assert.assertEquals("refField2",
                ((RegexSoftReference) concept.getSoftReferences(otherConcept).get(0)).getReferenceField());
    }

    @Test
    public void testParseBiDirectionalReferences() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(ConceptWithSoftReferences.class);
        classes.add(SimpleConcept.class);
        classes.add(ConceptWithSoftReferences2.class);
        List<org.openengsb.ekb.api.Concept<?>> concepts = parser.parseConcepts(classes);

        org.openengsb.ekb.api.Concept<?> concept = getConcept(concepts, "ConceptWithSoftReferences");
        org.openengsb.ekb.api.Concept<?> otherConcept = getConcept(concepts, "ConceptWithSoftReferences2");
        org.openengsb.ekb.api.Concept<?> simpleConcept = getConcept(concepts, "SimpleConcept");

        Assert.assertEquals(3, concepts.size());
        Assert.assertEquals("ConceptWithSoftReferences", concept.getKey().getId());
        Assert.assertEquals(ConceptWithSoftReferences.class, concept.getConceptClass());

        Assert.assertEquals(2, concept.getSoftReferences().size());
        Assert.assertEquals(1, otherConcept.getSoftReferences().size());
        Assert.assertEquals(1, concept.getSoftReferences(simpleConcept).size());
        Assert.assertEquals(1, concept.getSoftReferences(otherConcept).size());
        Assert.assertEquals(1, otherConcept.getSoftReferences(concept).size());
    }

    @Test
    public void testParseFieldMappings() throws AnnotationMissingException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(ConceptWithFieldMappings.class);
        classes.add(SimpleConcept.class);

        List<org.openengsb.ekb.api.Concept<?>> concepts = parser.parseConcepts(classes);

        org.openengsb.ekb.api.Concept<?> concept = getConcept(concepts, "ConceptWithFieldMappings");
        org.openengsb.ekb.api.Concept<?> simpleConcept = getConcept(concepts, "SimpleConcept");

        Assert.assertEquals(2, concepts.size());
        Assert.assertEquals("ConceptWithFieldMappings", concept.getKey().getId());
        Assert.assertEquals(ConceptWithFieldMappings.class, concept.getConceptClass());

        Assert.assertEquals(5, concept.getFieldMappings(simpleConcept).size());

        for (FieldMapping fm : concept.getFieldMappings(simpleConcept)) {
            Assert.assertEquals("mapsTo" + fm.getTargetFieldName(), fm.getSourceFieldName());
            if (!fm.getTargetFieldName().equals("test5")) {
                Assert.assertEquals(AutomaticMapping.class, fm.getClass());
            } else {
                Assert.assertEquals(TransformerFieldMapping.class, fm.getClass());
                TransformerFieldMapping tfm = (TransformerFieldMapping) fm;
                Assert.assertEquals(TestTransformer.class.getName(), tfm.getTransformer());
            }
        }
    }

    private org.openengsb.ekb.api.Concept<?> getConcept(List<org.openengsb.ekb.api.Concept<?>> concepts, String id) {
        for (org.openengsb.ekb.api.Concept<?> concept : concepts) {
            if (concept.getKey().getId().equals(id)) {
                return concept;
            }
        }
        return null;
    }

    @Concept(id = "SimpleConcept", version = "1.0.0")
    @SuppressWarnings("unused")
    private class SimpleConcept {
        @Key
        private String key;

        private Integer test2;
        private Boolean test3;
        private TestObject test4;

        private String test5;
    }

    @Concept(id = "ConceptWithSuperConcept", version = "1.0.0")
    @SuperConcept(id = "SimpleConcept", version = "1.0.0")
    private class ConceptWithSuperConcept {

    }

    @Concept(id = "ConceptWithIllegalSoftReferences", version = "1.0.0")
    @SuppressWarnings("unused")
    private class ConceptWithIllegalSoftReferences {
        @Key
        private String someKey;

        @ReferenceId(regexp = "#ref\\(([\\d]+)\\)", targetConceptId = "ConceptWithSuperConcept", targetConceptVersion = "1.0.0")
        private String refField1;

    }

    @Concept(id = "ConceptWithSoftReferences", version = "1.0.0")
    @SuppressWarnings("unused")
    private class ConceptWithSoftReferences {
        @Key
        private String someKey;

        @ReferenceId(regexp = "#ref\\(([\\d]+)\\)", targetConceptId = "SimpleConcept", targetConceptVersion = "1.0.0")
        private String refField1;

        @ReferenceId(regexp = "#someRef\\(([a-z,A-Z]+)\\)", targetConceptId = "ConceptWithSoftReferences2", targetConceptVersion = "1.0.0")
        private String refField2;

    }

    @Concept(id = "ConceptWithSoftReferences2", version = "1.0.0")
    @SuppressWarnings("unused")
    private class ConceptWithSoftReferences2 {
        @Key
        private String someKey;

        @ReferenceId(regexp = "#someRef\\(([a-z,A-Z]+)\\)", targetConceptId = "ConceptWithSoftReferences", targetConceptVersion = "1.0.0")
        private String refField;

    }

    @Concept(id = "ConceptWithFieldMappings", version = "1.0.0")
    @SuperConcept(id = "SimpleConcept", version = "1.0.0")
    @SuppressWarnings("unused")
    private class ConceptWithFieldMappings {

        @MapsTo("key")
        private String mapsTokey;

        @MapsTo("test2")
        private Integer mapsTotest2;

        @MapsTo("test3")
        private Boolean mapsTotest3;

        @MapsTo("test4")
        private TestObject mapsTotest4;

        @MapsTo("test5")
        @Transformation(TestTransformer.class)
        private TestObject mapsTotest5;

    }

    private class TestObject {
    }

    private class TestTransformer implements Transformer {

        @Override
        @SuppressWarnings("unchecked")
        public <TARGETTYPE> TARGETTYPE transform(Object source, Class<TARGETTYPE> targetType) {
            return (TARGETTYPE) source.toString();
        }
    }

}
