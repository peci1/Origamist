//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.24 at 12:36:40 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ModelColors complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModelColors">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="background" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Color"/>
 *         &lt;element name="foreground" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Color"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModelColors", propOrder = {
    "background",
    "foreground"
})
public class ModelColors {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Color background;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Color foreground;

    /**
     * Gets the value of the background property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Sets the value of the background property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackground(Color value) {
        this.background = value;
    }

    /**
     * Gets the value of the foreground property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     * Sets the value of the foreground property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeground(Color value) {
        this.foreground = value;
    }

}
