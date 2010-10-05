
package org.openengsb.core.xmlmapping;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="XMLEnum">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="className"/>
 *     &lt;xs:element type="xs:int" name="ordinal"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class XMLEnum
{
    private String className;
    private int ordinal;
    private String name;

    /** 
     * Get the 'className' element value.
     * 
     * @return value
     */
    public String getClassName() {
        return className;
    }

    /** 
     * Set the 'className' element value.
     * 
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /** 
     * Get the 'ordinal' element value.
     * 
     * @return value
     */
    public int getOrdinal() {
        return ordinal;
    }

    /** 
     * Set the 'ordinal' element value.
     * 
     * @param ordinal
     */
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    /** 
     * Get the 'name' element value.
     * 
     * @return value
     */
    public String getName() {
        return name;
    }

    /** 
     * Set the 'name' element value.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
