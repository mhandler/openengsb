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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.openengsb.ekb.annotations.Concept;

public class JarReader {

    private URI jar;

    private URLClassLoader classloader;

    public JarReader(URI jar) {
        this.jar = jar;
        try {
            this.classloader = new URLClassLoader(new URL[] { jar.toURL() });
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Class<?>> readInConcepts() throws IOException {
        List<Class<?>> concepts = new ArrayList<Class<?>>();
        JarFile jarFile = new JarFile(new File(jar));
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                Class<?> clazz = loadClass(jarEntry.getName());
                if (isConcept(clazz)) {
                    concepts.add(clazz);
                }
            }
        }
        return concepts;
    }

    private boolean isConcept(Class<?> clazz) {
        return clazz.getAnnotation(Concept.class) != null;
    }

    private Class<?> loadClass(String entry) throws IOException {
        try {
            String className = entry.replaceAll("/", ".");
            className = className.substring(0, className.lastIndexOf('.'));
            return classloader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
