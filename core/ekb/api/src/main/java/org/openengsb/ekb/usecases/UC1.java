package org.openengsb.ekb.usecases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.ekb.api.Concept;
import org.openengsb.ekb.api.ConceptSource;
import org.openengsb.ekb.api.EKB;

public class UC1 {

    private static EKB ekb = null;

    public static void main(String[] args) {

        Concept<Signal> signalConcept = ekb.getConcept("#Signal", Signal.class);
        List<ConceptSource> signalSources = ekb.getSources(signalConcept);

        Map<String, List<Signal>> data = new HashMap<String, List<Signal>>();

        for (ConceptSource source : signalSources) {
            List<Signal> signals = ekb.getData(source, signalConcept);
            data.put(source.getId(), signals);
        }

        // TODO: find out whatever we want to find out about the data
    }
}
