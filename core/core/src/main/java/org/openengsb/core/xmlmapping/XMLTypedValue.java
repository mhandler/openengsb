
package org.openengsb.core.xmlmapping;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="XMLTypedValue">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="type"/>
 *     &lt;xs:element type="xs:string" name="conceptIRI"/>
 *     &lt;xs:element type="XMLMappable" name="value"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class XMLTypedValue
{
    private String type;
    private String conceptIRI;
    private XMLMappable value;

    /** 
     * Get the 'type' element value.
     * 
     * @return value
     */
    public String getType() {
        return type;
    }

    /** 
     * Set the 'type' element value.
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /** 
     * Get the 'conceptIRI' element value.
     * 
     * @return value
     */
    public String getConceptIRI() {
        return conceptIRI;
    }

    /** 
     * Set the 'conceptIRI' element value.
     * 
     * @param conceptIRI
     */
    public void setConceptIRI(String conceptIRI) {
        this.conceptIRI = conceptIRI;
    }

    /** 
     * Get the 'value' element value.
     * 
     * @return value
     */
    public XMLMappable getValue() {
        return value;
    }

    /** 
     * Set the 'value' element value.
     * 
     * @param value
     */
    public void setValue(XMLMappable value) {
        this.value = value;
    }
}
