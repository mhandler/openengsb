package org.openengsb.ekb.codegen;

import java.io.File;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class CodeGenerator {

    private static OWLOntology ontology;

    public static void main(String[] args) {
        File ontologyFile = new File("src/main/resources/ekbConcepts.owl");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);

            OWLDataFactory factory = manager.getOWLDataFactory();
            IRI iri = IRI.create("http://www.openengsb.org/ekb/ekbConcepts.owl#MainConcept");
            OWLClass mainConcept = factory.getOWLClass(iri);
            Set<OWLClassExpression> subClasses = mainConcept.getSubClasses(ontology);
            generateMainConcepts(subClasses);

        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateMainConcepts(Set<OWLClassExpression> subClasses) {
        for (OWLClassExpression subClass : subClasses) {
            generateMainConcept(subClass.asOWLClass());
        }
    }

    private static void generateMainConcept(OWLClass owlClass) {
        System.out.println("package org.openengsb.codegen.test");

        OWLClass parent = getMainConceptParent(owlClass.getSuperClasses(ontology));
        if (parent == null || isActualMainConcept(parent)) {
            System.out.println("public class " + owlClass.getIRI().getFragment() + " {");
        } else {
            String superclassName = parent.getIRI().getFragment();
            System.out.println("public class " + owlClass.getIRI().getFragment() + " extends " + superclassName + " {");
        }

        generateFields(owlClass);
        System.out.println("}");
        generateMainConcepts(owlClass.getSubClasses(ontology));
    }

    private static boolean isActualMainConcept(OWLClass parent) {
        return parent.getIRI().getFragment().equals("MainConcept");
    }

    private static OWLClass getMainConceptParent(Set<OWLClassExpression> superClasses) {
        for (OWLClassExpression superClassExpression : superClasses) {
            if (!superClassExpression.isClassExpressionLiteral()) {
                continue;
            }
            OWLClass superClass = superClassExpression.asOWLClass();
            if (isActualMainConcept(superClass)) {
                return superClass;
            }
            OWLClass recursion = getMainConceptParent(superClass.getSuperClasses(ontology));
            if (recursion != null) {
                return superClass;
            }
        }
        return null;
    }

    private static void generateFields(OWLClass owlClass) {
        Set<OWLClassExpression> superClasses = owlClass.getSuperClasses(ontology);
        for (OWLClassExpression superClass : superClasses) {
            if (!superClass.isClassExpressionLiteral()) {
                String fieldName = getFieldName(superClass);
                OWLClass type = superClass.getClassesInSignature().iterator().next();
                String fieldType = type.getIRI().getFragment();
                System.out.println("    private " + fieldType + " " + fieldName + ";");
            }
        }
    }

    private static String getFieldName(OWLClassExpression superClass) {
        Set<OWLObjectProperty> objectPropertiesInSignature = superClass.getObjectPropertiesInSignature();
        OWLObjectProperty property = objectPropertiesInSignature.iterator().next();
        String propertyName = property.getIRI().getFragment();
        String fieldName = transformToFieldName(propertyName);
        return fieldName;
    }

    private static String transformToFieldName(String propertyName) {
        String fieldName = propertyName.replaceFirst("has", "");
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
    }

}
