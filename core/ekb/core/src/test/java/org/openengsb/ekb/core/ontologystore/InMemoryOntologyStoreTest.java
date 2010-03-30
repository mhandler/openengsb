package org.openengsb.ekb.core.ontologystore;

import java.io.File;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openengsb.ekb.core.ontologystore.OntologyDescriptor;
import org.openengsb.ekb.core.ontologystore.OntologyStore;
import org.openengsb.ekb.core.ontologystore.OntologyStoreException;
import org.openengsb.ekb.core.ontologystore.internal.InMemoryOntologyStore;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class InMemoryOntologyStoreTest {

    private OntologyStore store;

    @Before
    public void setUp() {
        store = new InMemoryOntologyStore();
    }

    @Test
    public void testAddOntology() throws Exception {
        OWLOntology ont1 = loadFromFile(new File("target/test-classes/test.owl"));
        store.addOntology(ont1);

        Set<OntologyDescriptor> versions = store.getOntologyVersions(ont1.getOntologyID().getOntologyIRI());
        Assert.assertEquals(1, versions.size());
        Assert.assertEquals(ont1.getOntologyID().getOntologyIRI(), versions.iterator().next().getOntologyIRI());
    }

    @Test(expected = OntologyStoreException.class)
    public void testDoubleAddOntology() throws Exception {
        OWLOntology ont1 = loadFromFile(new File("target/test-classes/test.owl"));
        store.addOntology(ont1);
        store.addOntology(ont1);
    }

    @Test
    public void testDeleteOntology() throws Exception {
        OWLOntology ont1 = loadFromFile(new File("target/test-classes/test.owl"));
        store.addOntology(ont1);

        OntologyDescriptor descriptor = store.getOntologyVersions(ont1.getOntologyID().getOntologyIRI()).iterator()
                .next();
        store.deleteOntology(descriptor);
        Set<OntologyDescriptor> versions = store.getOntologyVersions(ont1.getOntologyID().getOntologyIRI());
        Assert.assertEquals(0, versions.size());
    }

    @Test(expected = OntologyStoreException.class)
    public void testDeleteNotStoredOntology() throws Exception {
        OWLOntology ont1 = loadFromFile(new File("target/test-classes/test.owl"));
        store.addOntology(ont1);

        OntologyDescriptor descriptor = store.getOntologyVersions(ont1.getOntologyID().getOntologyIRI()).iterator()
                .next();
        store.deleteOntology(descriptor);
        store.deleteOntology(descriptor);
    }

    @Test
    public void testGetOntologyVersionsAndGetOntology() throws Exception {
        OWLOntology ont1 = loadFromFile(new File("target/test-classes/test.owl"));
        OWLOntology ont2 = loadFromFile(new File("target/test-classes/test2.owl"));
        store.addOntology(ont1);
        store.addOntology(ont2);

        Set<OntologyDescriptor> versions = store.getOntologyVersions(ont1.getOntologyID().getOntologyIRI());
        Assert.assertEquals(2, versions.size());

        OntologyDescriptor desc1 = store.getOntology(ont1.getOntologyID().getOntologyIRI(), ont1.getOntologyID()
                .getVersionIRI());
        OntologyDescriptor desc2 = store.getOntology(ont2.getOntologyID().getOntologyIRI(), ont2.getOntologyID()
                .getVersionIRI());

        Assert.assertTrue(versions.contains(desc1));
        Assert.assertTrue(versions.contains(desc2));
    }

    private OWLOntology loadFromFile(File file) throws Exception {
        // load OWLOntology from file
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager.loadOntologyFromOntologyDocument(file);
    }
}
