//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.22 at 04:43:03 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString;
import cz.cuni.mff.peckam.java.origamist.model.Paper;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;


/**
 * <p>Java class for ModelPaper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModelPaper">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Paper">
 *       &lt;sequence>
 *         &lt;element name="colors" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}ModelColors"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="note" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModelPaper", propOrder = {
    "colors",
    "weight",
    "note"
})
public class ModelPaper
    extends Paper
{

    @XmlElement(required = true)
    protected ModelColors colors;
    protected double weight;
    @XmlElement(required = true)
    protected List<LangString> note = new ChangeNotifyingList<LangString>();

    /**
     * Gets the value of the colors property.
     * 
     * @return
     *     possible object is
     *     {@link ModelColors }
     *     
     */
    public ModelColors getColors() {
        return colors;
    }

    /**
     * Sets the value of the colors property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelColors }
     *     
     */
    public void setColors(ModelColors value) {
        this.colors = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     */
    public void setWeight(double value) {
        this.weight = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangString }
     * 
     * 
     */
    public List<LangString> getNote() {
        if (note == null) {
            note = new ChangeNotifyingList<LangString>();
        }
        return this.note;
    }

}