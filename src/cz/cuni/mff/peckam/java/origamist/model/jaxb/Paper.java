//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.26 at 01:06:48 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.DiagramPaper;
import cz.cuni.mff.peckam.java.origamist.model.ModelPaper;


/**
 * <p>Java class for Paper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Paper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="size" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}UnitDimension"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Paper", propOrder = {
    "size"
})
@XmlSeeAlso({
    ModelPaper.class,
    DiagramPaper.class
})
public abstract class Paper {

    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.UnitDimension.class)
    protected cz.cuni.mff.peckam.java.origamist.model.UnitDimension size;

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension }
     *     
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension }
     *     
     */
    public void setSize(cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension value) {
        this.size = ((cz.cuni.mff.peckam.java.origamist.model.UnitDimension) value);
    }

}
