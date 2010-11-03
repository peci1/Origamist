//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.03 at 11:41:45 dop. CET 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import cz.cuni.mff.peckam.java.origamist.model.Paper;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Java class for DiagramPaper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DiagramPaper">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Paper">
 *       &lt;sequence>
 *         &lt;element name="color" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}DiagramColors"/>
 *         &lt;element name="cols" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="rows" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiagramPaper", propOrder = {
    "color",
    "cols",
    "rows"
})
public class DiagramPaper
    extends Paper
{

    @XmlElement(required = true)
    protected DiagramColors color;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer cols;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer rows;

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link DiagramColors }
     *     
     */
    public DiagramColors getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagramColors }
     *     
     */
    public void setColor(DiagramColors value) {
        this.color = value;
    }

    /**
     * Gets the value of the cols property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getCols() {
        return cols;
    }

    /**
     * Sets the value of the cols property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCols(Integer value) {
        this.cols = value;
    }

    /**
     * Gets the value of the rows property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getRows() {
        return rows;
    }

    /**
     * Sets the value of the rows property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRows(Integer value) {
        this.rows = value;
    }

}
