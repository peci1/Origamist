//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.23 at 05:56:08 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.common.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Image complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Image">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="image" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}BinaryImage"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Image", propOrder = {
    "image"
})
public class Image {

    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.BinaryImage.class)
    protected cz.cuni.mff.peckam.java.origamist.common.BinaryImage image;

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage }
     *     
     */
    public cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage }
     *     
     */
    public void setImage(cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage value) {
        this.image = ((cz.cuni.mff.peckam.java.origamist.common.BinaryImage) value);
    }

}
