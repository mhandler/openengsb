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
import java.util.List;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.core.knowledgemanagement.KnowledgeManager;

public class ModelAnalyzer {

    private KnowledgeManager knowledgeManager;

    public void analyzeAndStore(File modelFile) {
        try {
            JarReader jarReader = new JarReader(modelFile.toURI());
            List<Class<?>> conceptClasses = jarReader.readInConcepts();
            ConceptParser parser = new ConceptParser();
            List<Concept<?>> concepts = parser.parseConcepts(conceptClasses);
            store(concepts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void store(List<Concept<?>> concepts) {
        knowledgeManager.setConcepts(concepts);
    }

    public void setKnowledgeManager(KnowledgeManager knowledgeManager) {
        this.knowledgeManager = knowledgeManager;
    }

}
