package org.openengsb.drools;

/**

 Copyright 2009 OpenEngSB Division, Vienna University of Technology

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE\-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

import java.io.IOException;

import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.servicemix.jbi.jaxp.SourceTransformer;
import org.openengsb.drools.model.Event;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Helper-methods for parsing Xml-messages.
 */
public final class XmlHelper {

    /**
     * hidden default-constructor.
     */
    private XmlHelper() {
    }

    /**
     * Transformer used for transforming the Source into a parsable
     * xml-document-object.
     */
    private static SourceTransformer t = new SourceTransformer();

    /**
     * parse an Event-message.
     * 
     * @param msg message that should be parsed.
     * @return the parsed Event-Object.
     */
    public static Event parseEvent(NormalizedMessage msg) {
        Element source;
        try {
            source = t.toDOMElement(msg);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("Could not parse the message", e);
        } catch (TransformerException e) {
            throw new IllegalArgumentException("Could not parse the message", e);
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("Could not parse the message", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse the message", e);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Could not parse the message", e);
        }
        Node nameNode = source.getElementsByTagName("name").item(0);
        Event result = new Event(nameNode.getTextContent());
        Node contextNode = source.getElementsByTagName("contextid").item(0);
        if (contextNode != null) {
            result.setContextId(contextNode.getTextContent());
        }
        return result;
    }

}