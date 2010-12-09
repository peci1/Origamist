//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.09 at 02:54:06 odp. CET 
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
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.w3._2001.xmlschema.Adapter1;


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
public class Step
    implements Equals, HashCode
{

    @XmlElement(type = String.class, defaultValue = "1")
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer colspan;
    @XmlElement(type = String.class, defaultValue = "1")
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer rowspan;
    @XmlElement(defaultValue = "100")
    protected Double zoom;
    @XmlAttribute(name = "id", required = true)
    protected int id;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> description;
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.BinaryImage.class)
    protected cz.cuni.mff.peckam.java.origamist.common.BinaryImage image;
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
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getDescription() {
        if (description == null) {
            description = new ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
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
    public cz.cuni.mff.peckam.java.origamist.common.BinaryImage getImage() {
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
    public void setImage(cz.cuni.mff.peckam.java.origamist.common.BinaryImage value) {
        this.image = value;
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

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Step)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Step that = ((Step) object);
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> lhsDescription;
            lhsDescription = this.getDescription();
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
                return false;
            }
        }
        {
            Integer lhsColspan;
            lhsColspan = this.getColspan();
            Integer rhsColspan;
            rhsColspan = that.getColspan();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "colspan", lhsColspan), LocatorUtils.property(thatLocator, "colspan", rhsColspan), lhsColspan, rhsColspan)) {
                return false;
            }
        }
        {
            Integer lhsRowspan;
            lhsRowspan = this.getRowspan();
            Integer rhsRowspan;
            rhsRowspan = that.getRowspan();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "rowspan", lhsRowspan), LocatorUtils.property(thatLocator, "rowspan", rhsRowspan), lhsRowspan, rhsRowspan)) {
                return false;
            }
        }
        {
            Double lhsZoom;
            lhsZoom = this.getZoom();
            Double rhsZoom;
            rhsZoom = that.getZoom();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "zoom", lhsZoom), LocatorUtils.property(thatLocator, "zoom", rhsZoom), lhsZoom, rhsZoom)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.common.BinaryImage lhsImage;
            lhsImage = this.getImage();
            cz.cuni.mff.peckam.java.origamist.common.BinaryImage rhsImage;
            rhsImage = that.getImage();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "image", lhsImage), LocatorUtils.property(thatLocator, "image", rhsImage), lhsImage, rhsImage)) {
                return false;
            }
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.model.Operation> lhsOperation;
            lhsOperation = this.getOperation();
            List<cz.cuni.mff.peckam.java.origamist.model.Operation> rhsOperation;
            rhsOperation = that.getOperation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "operation", lhsOperation), LocatorUtils.property(thatLocator, "operation", rhsOperation), lhsOperation, rhsOperation)) {
                return false;
            }
        }
        {
            int lhsId;
            lhsId = this.getId();
            int rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            Integer theColspan;
            theColspan = this.getColspan();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "colspan", theColspan), currentHashCode, theColspan);
        }
        {
            Integer theRowspan;
            theRowspan = this.getRowspan();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "rowspan", theRowspan), currentHashCode, theRowspan);
        }
        {
            Double theZoom;
            theZoom = this.getZoom();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "zoom", theZoom), currentHashCode, theZoom);
        }
        {
            cz.cuni.mff.peckam.java.origamist.common.BinaryImage theImage;
            theImage = this.getImage();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "image", theImage), currentHashCode, theImage);
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.model.Operation> theOperation;
            theOperation = this.getOperation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "operation", theOperation), currentHashCode, theOperation);
        }
        {
            int theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
