//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.26 at 01:06:48 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Java class for Step complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Step">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="colspan" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="rowspan" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="zoom" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Percent" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="image" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}BinaryImage"/>
 *           &lt;element name="operation" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Operation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Step", propOrder = {
    "description",
    "colspan",
    "rowspan",
    "zoom",
    "image",
    "operation"
})
public class Step {

    @XmlElement(required = true)
    protected List<LangString> description = new ChangeNotifyingList<LangString>();
    @XmlElement(type = String.class, defaultValue = "1")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer colspan;
    @XmlElement(type = String.class, defaultValue = "1")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer rowspan;
    @XmlElement(defaultValue = "100")
    protected Double zoom;
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.BinaryImage.class)
    protected cz.cuni.mff.peckam.java.origamist.common.BinaryImage image;
    @XmlAttribute(name = "id", required = true)
    protected int id;
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.model.Operation.class)
    protected List<cz.cuni.mff.peckam.java.origamist.model.Operation> operation;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangString }
     * 
     * 
     */
    public List<LangString> getDescription() {
        if (description == null) {
            description = new ChangeNotifyingList<LangString>();
        }
        return this.description;
    }

    /**
     * Gets the value of the colspan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getColspan() {
        return colspan;
    }

    /**
     * Sets the value of the colspan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColspan(Integer value) {
        this.colspan = value;
    }

    /**
     * Gets the value of the rowspan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getRowspan() {
        return rowspan;
    }

    /**
     * Sets the value of the rowspan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRowspan(Integer value) {
        this.rowspan = value;
    }

    /**
     * Gets the value of the zoom property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getZoom() {
        return zoom;
    }

    /**
     * Sets the value of the zoom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setZoom(Double value) {
        this.zoom = value;
    }

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

    /**
     * Gets the value of the operation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperation().add(newItem);
     * </pre>
     * 
     * 
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.model.Operation }
     * 
     */
    public List<cz.cuni.mff.peckam.java.origamist.model.Operation> getOperation() {
        if (operation == null) {
            operation = new ArrayList<cz.cuni.mff.peckam.java.origamist.model.Operation>();
        }
        return this.operation;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

}
