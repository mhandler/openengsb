package org.openengsb.ekb.codegen;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
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

    private static StringWriter sw = new StringWriter();

    private static PrintWriter out = new PrintWriter(sw);

    public static void main(String[] args) {
        File ontologyFile = new File("src/main/resources/ekbConcepts.owl");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        try {
            ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);

            OWLDataFactory factory = manager.getOWLDataFactory();
            IRI mainConceptIri = IRI.create("http://www.openengsb.org/ekb/ekbConcepts.owl#MainConcept");
            OWLClass mainConcept = factory.getOWLClass(mainConceptIri);
            Set<OWLClassExpression> mainConcepts = mainConcept.getSubClasses(ontology);
            generateMainConcepts(mainConcepts);

            IRI serviceIri = IRI.create("http://www.openengsb.org/ekb/ekbConcepts.owl#Service");
            OWLClass service = factory.getOWLClass(serviceIri);
            Set<OWLClassExpression> services = service.getSubClasses(ontology);

            generateServices(services);

            System.out.println(sw.toString());

        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateServices(Set<OWLClassExpression> services) {
        for (OWLClassExpression service : services) {
            generateService(service.asOWLClass());
        }
    }

    private static void generateService(OWLClass serviceClass) {
        out.println("package org.openengsb.codegen.test;");

        out.println("public interface " + serviceClass.getIRI().getFragment() + " {");
        generateServiceMethods(serviceClass);
        out.println("}");
        out.println();
        // subclasses
        generateServices(serviceClass.getSubClasses(ontology));
    }

    private static void generateServiceMethods(OWLClass serviceClass) {
        Set<OWLClassExpression> superClasses = serviceClass.getSuperClasses(ontology);
        for (OWLClassExpression superClass : superClasses) {
            if (!superClass.isClassExpressionLiteral()) {

            }
        }
    }

    private static void generateMainConcepts(Set<OWLClassExpression> subClasses) {
        for (OWLClassExpression subClass : subClasses) {
            generateMainConcept(subClass.asOWLClass());
        }
    }

    private static void generateMainConcept(OWLClass owlClass) {
        String extendsClause = "";

        out.println("package org.openengsb.codegen.test;");

        OWLClass parent = getMainConceptParent(owlClass.getSuperClasses(ontology));
        if (parent != null && !isActualMainConcept(parent)) {
            String superclassName = parent.getIRI().getFragment();
            extendsClause = " extends " + superclassName;
        }

        out.println("public class " + owlClass.getIRI().getFragment() + extendsClause + " {");
        generateFields(owlClass);
        out.println("}");
        out.println();
        // subclasses
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
                out.println("    private " + fieldType + " " + fieldName + ";");
                out.println();
                out.println("    public " + fieldType + " get" + firstCharToUpper(fieldName) + "() {");
                out.println("        return " + fieldName + ";");
                out.println("    }");
                out.println();
                out.println("    public void set" + firstCharToUpper(fieldName) + "(" + fieldType + " " + fieldName
                        + ") {");
                out.println("        this." + fieldName + " = " + fieldName + ";");
                out.println("    }");
                out.println();

            }
        }
    }

    private static String firstCharToUpper(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
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
