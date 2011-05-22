//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.21 at 05:28:36 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: angle
 * <p>Provided property: line
 * <p>Provided property: secondAngle
 * <p>Provided property: secondLine
 * <p>Provided property: invert
 * <p>Java class for ThunderboltFoldOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ThunderboltFoldOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}ThunderboltFoldOperationBase">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}angle"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}line"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}layerList"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}secondAngle" minOccurs="0"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}secondLine"/>
 *       &lt;/sequence>
 *       &lt;attribute name="invert" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "ThunderboltFoldOperation", propOrder = {
    "angle",
    "line",
    "layer",
    "secondAngle",
    "secondLine"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ThunderboltFoldOperation
    extends ThunderboltFoldOperationBase
    implements Equals, HashCode
{

    protected double angle;
    /**
     * Property angle
     * 
     */
    public final static String ANGLE_PROPERTY = "angle:cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation";
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D line;
    /**
     * Property line
     * 
     */
    public final static String LINE_PROPERTY = "line:cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation";
    protected List<Integer> layer = new ObservableList<Integer>();
    protected Double secondAngle;
    /**
     * Property secondAngle
     * 
     */
    public final static String SECOND_ANGLE_PROPERTY = "secondAngle:cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation";
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D secondLine;
    /**
     * Property secondLine
     * 
     */
    public final static String SECOND_LINE_PROPERTY = "secondLine:cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation";
    protected Boolean invert;
    /**
     * Property invert
     * 
     */
    public final static String INVERT_PROPERTY = "invert:cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation";
    /**
     * Property layer
     * 
     */
    public final static String LAYER_PROPERTY = "layer:cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation";

    public ThunderboltFoldOperation() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.ThunderboltFoldOperation")) {
            init();
        }
    }

    /**
     * Gets the value of the angle property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the value of the angle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAngle(double value) {
        double old = this.angle;
        this.angle = value;
        if (old!= value) {
            support.firePropertyChange(ThunderboltFoldOperation.ANGLE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the line property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Line2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Line2D getLine() {
        return line;
    }

    /**
     * Sets the value of the line property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    public void setLine(cz.cuni.mff.peckam.java.origamist.model.Line2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Line2D old = this.line;
        this.line = ((cz.cuni.mff.peckam.java.origamist.model.Line2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(ThunderboltFoldOperation.LINE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the layer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the layer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLayer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    @XmlElement(type = Integer.class)
    public List<Integer> getLayer() {
        if (layer == null) {
            layer = new ObservableList<Integer>();
        }
        return this.layer;
    }

    /**
     * Gets the value of the secondAngle property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSecondAngle() {
        return secondAngle;
    }

    /**
     * Sets the value of the secondAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSecondAngle(Double value) {
        Double old = this.secondAngle;
        this.secondAngle = value;
        if (old!= value) {
            support.firePropertyChange(ThunderboltFoldOperation.SECOND_ANGLE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the secondLine property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Line2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Line2D getSecondLine() {
        return secondLine;
    }

    /**
     * Sets the value of the secondLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    public void setSecondLine(cz.cuni.mff.peckam.java.origamist.model.Line2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Line2D old = this.secondLine;
        this.secondLine = ((cz.cuni.mff.peckam.java.origamist.model.Line2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(ThunderboltFoldOperation.SECOND_LINE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the invert property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInvert() {
        if (invert == null) {
            return false;
        } else {
            return invert;
        }
    }

    /**
     * Sets the value of the invert property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @XmlAttribute
    public void setInvert(Boolean value) {
        Boolean old = this.invert;
        this.invert = value;
        if (old!= value) {
            support.firePropertyChange(ThunderboltFoldOperation.INVERT_PROPERTY, old, value);
        }
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getLayer()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {LAYER_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ThunderboltFoldOperation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final ThunderboltFoldOperation that = ((ThunderboltFoldOperation) object);
        {
            double lhsAngle;
            lhsAngle = this.getAngle();
            double rhsAngle;
            rhsAngle = that.getAngle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "angle", lhsAngle), LocatorUtils.property(thatLocator, "angle", rhsAngle), lhsAngle, rhsAngle)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D lhsLine;
            lhsLine = this.getLine();
            cz.cuni.mff.peckam.java.origamist.model.Line2D rhsLine;
            rhsLine = that.getLine();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "line", lhsLine), LocatorUtils.property(thatLocator, "line", rhsLine), lhsLine, rhsLine)) {
                return false;
            }
        }
        {
            List<Integer> lhsLayer;
            lhsLayer = this.getLayer();
            List<Integer> rhsLayer;
            rhsLayer = that.getLayer();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "layer", lhsLayer), LocatorUtils.property(thatLocator, "layer", rhsLayer), lhsLayer, rhsLayer)) {
                return false;
            }
        }
        {
            Double lhsSecondAngle;
            lhsSecondAngle = this.getSecondAngle();
            Double rhsSecondAngle;
            rhsSecondAngle = that.getSecondAngle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "secondAngle", lhsSecondAngle), LocatorUtils.property(thatLocator, "secondAngle", rhsSecondAngle), lhsSecondAngle, rhsSecondAngle)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D lhsSecondLine;
            lhsSecondLine = this.getSecondLine();
            cz.cuni.mff.peckam.java.origamist.model.Line2D rhsSecondLine;
            rhsSecondLine = that.getSecondLine();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "secondLine", lhsSecondLine), LocatorUtils.property(thatLocator, "secondLine", rhsSecondLine), lhsSecondLine, rhsSecondLine)) {
                return false;
            }
        }
        {
            boolean lhsInvert;
            lhsInvert = this.isInvert();
            boolean rhsInvert;
            rhsInvert = that.isInvert();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "invert", lhsInvert), LocatorUtils.property(thatLocator, "invert", rhsInvert), lhsInvert, rhsInvert)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            double theAngle;
            theAngle = this.getAngle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "angle", theAngle), currentHashCode, theAngle);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D theLine;
            theLine = this.getLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "line", theLine), currentHashCode, theLine);
        }
        {
            List<Integer> theLayer;
            theLayer = this.getLayer();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "layer", theLayer), currentHashCode, theLayer);
        }
        {
            Double theSecondAngle;
            theSecondAngle = this.getSecondAngle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "secondAngle", theSecondAngle), currentHashCode, theSecondAngle);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D theSecondLine;
            theSecondLine = this.getSecondLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "secondLine", theSecondLine), currentHashCode, theSecondLine);
        }
        {
            boolean theInvert;
            theInvert = this.isInvert();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "invert", theInvert), currentHashCode, theInvert);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
