//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.30 at 01:37:07 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import cz.cuni.mff.peckam.java.origamist.common.GeneratedClassBase;
import cz.cuni.mff.peckam.java.origamist.model.Operation;
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
 * <p>Provided property: colspan
 * <p>Provided property: rowspan
 * <p>Provided property: zoom
 * <p>Provided property: image
 * <p>Provided property: id
 * <p>Java class for Step complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Step">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="colspan" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="rowspan" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="zoom" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Percent" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="image" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}BinaryImage"/>
 *           &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}operations"/>
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
@XmlType(name = "Step", propOrder = {
    "description",
    "colspan",
    "rowspan",
    "zoom",
    "image",
    "operations"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Step
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected Integer colspan;
    /**
     * Property colspan
     * 
     */
    public final static String COLSPAN_PROPERTY = "colspan:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    protected Integer rowspan;
    /**
     * Property rowspan
     * 
     */
    public final static String ROWSPAN_PROPERTY = "rowspan:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    protected Double zoom;
    /**
     * Property zoom
     * 
     */
    public final static String ZOOM_PROPERTY = "zoom:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    protected cz.cuni.mff.peckam.java.origamist.common.BinaryImage image;
    /**
     * Property image
     * 
     */
    public final static String IMAGE_PROPERTY = "image:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    protected List<Operation> operations = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Operation>();
    protected int id;
    /**
     * Property id
     * 
     */
    public final static String ID_PROPERTY = "id:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    /**
     * Property description
     * 
     */
    public final static String DESCRIPTION_PROPERTY = "description:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    /**
     * Property operations
     * 
     */
    public final static String OPERATIONS_PROPERTY = "operations:cz.cuni.mff.peckam.java.origamist.model.jaxb.Step";
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> description = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();

    public Step() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.Step")) {
            init();
        }
    }

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
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getDescription() {
        if (description == null) {
            description = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
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
    @XmlElement(type = String.class, defaultValue = "1")
    @XmlJavaTypeAdapter(Adapter1 .class)
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
        Integer old = this.colspan;
        this.colspan = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Step.COLSPAN_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the rowspan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(type = String.class, defaultValue = "1")
    @XmlJavaTypeAdapter(Adapter1 .class)
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
        Integer old = this.rowspan;
        this.rowspan = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Step.ROWSPAN_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the zoom property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    @XmlElement(defaultValue = "100")
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
        Double old = this.zoom;
        this.zoom = value;
        if (old!= value) {
            support.firePropertyChange(Step.ZOOM_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage }
     *     
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.BinaryImage.class)
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
        cz.cuni.mff.peckam.java.origamist.common.BinaryImage old = this.image;
        this.image = ((cz.cuni.mff.peckam.java.origamist.common.BinaryImage) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Step.IMAGE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the operations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.TurnOverOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ImageOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.RotateOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldUnfoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.RepeatOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.SymmetryOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ReverseFoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.MarkerOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation }
     * 
     * 
     */
    @XmlElements({
        @XmlElement(name = "turnOverOperation", type = cz.cuni.mff.peckam.java.origamist.model.TurnOverOperation.class),
        @XmlElement(name = "imageOperation", type = cz.cuni.mff.peckam.java.origamist.model.ImageOperation.class),
        @XmlElement(name = "thunderboltFoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.ThunderboltFoldOperation.class),
        @XmlElement(name = "rotateOperation", type = cz.cuni.mff.peckam.java.origamist.model.RotateOperation.class),
        @XmlElement(name = "foldUnfoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.FoldUnfoldOperation.class),
        @XmlElement(name = "foldOperation", type = cz.cuni.mff.peckam.java.origamist.model.FoldOperation.class),
        @XmlElement(name = "repeatOperation", type = cz.cuni.mff.peckam.java.origamist.model.RepeatOperation.class),
        @XmlElement(name = "symmetryOperation", type = cz.cuni.mff.peckam.java.origamist.model.SymmetryOperation.class),
        @XmlElement(name = "reverseFoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.ReverseFoldOperation.class),
        @XmlElement(name = "markerOperation", type = cz.cuni.mff.peckam.java.origamist.model.MarkerOperation.class),
        @XmlElement(name = "crimpFoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.CrimpFoldOperation.class)
    })
    public List<Operation> getOperations() {
        if (operations == null) {
            operations = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Operation>();
        }
        return this.operations;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.Integer }
     *     
     */
    @XmlAttribute(required = true)
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.Integer }
     *     
     */
    public void setId(int value) {
        int old = this.id;
        this.id = value;
        if (old!= value) {
            support.firePropertyChange(Step.ID_PROPERTY, old, value);
        }
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getDescription(), getOperations()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {DESCRIPTION_PROPERTY, OPERATIONS_PROPERTY };
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
            List<Operation> lhsOperations;
            lhsOperations = this.getOperations();
            List<Operation> rhsOperations;
            rhsOperations = that.getOperations();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "operations", lhsOperations), LocatorUtils.property(thatLocator, "operations", rhsOperations), lhsOperations, rhsOperations)) {
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
            List<Operation> theOperations;
            theOperations = this.getOperations();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "operations", theOperations), currentHashCode, theOperations);
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
