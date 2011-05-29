//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.29 at 09:23:44 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.DoubleDimension;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: unit
 * <p>Provided property: referenceLength
 * <p>Provided property: referenceUnit
 * <p>Java class for UnitDimension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnitDimension">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Dimension">
 *       &lt;sequence>
 *         &lt;element name="unit" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Unit"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="referenceLength" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *           &lt;element name="referenceUnit" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Unit"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "UnitDimension", propOrder = {
    "unit",
    "referenceLength",
    "referenceUnit"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UnitDimension
    extends DoubleDimension
    implements Equals, HashCode
{

    protected Unit unit;
    /**
     * Property unit
     * 
     */
    public final static String UNIT_PROPERTY = "unit:cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension";
    protected Double referenceLength;
    /**
     * Property referenceLength
     * 
     */
    public final static String REFERENCE_LENGTH_PROPERTY = "referenceLength:cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension";
    protected Unit referenceUnit;
    /**
     * Property referenceUnit
     * 
     */
    public final static String REFERENCE_UNIT_PROPERTY = "referenceUnit:cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension";

    public UnitDimension() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.UnitDimension")) {
            init();
        }
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link Unit }
     *     
     */
    @XmlElement(required = true)
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Unit }
     *     
     */
    public void setUnit(Unit value) {
        Unit old = this.unit;
        this.unit = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(UnitDimension.UNIT_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the referenceLength property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getReferenceLength() {
        return referenceLength;
    }

    /**
     * Sets the value of the referenceLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setReferenceLength(Double value) {
        Double old = this.referenceLength;
        this.referenceLength = value;
        if (old!= value) {
            support.firePropertyChange(UnitDimension.REFERENCE_LENGTH_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the referenceUnit property.
     * 
     * @return
     *     possible object is
     *     {@link Unit }
     *     
     */
    public Unit getReferenceUnit() {
        return referenceUnit;
    }

    /**
     * Sets the value of the referenceUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Unit }
     *     
     */
    public void setReferenceUnit(Unit value) {
        Unit old = this.referenceUnit;
        this.referenceUnit = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(UnitDimension.REFERENCE_UNIT_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof UnitDimension)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final UnitDimension that = ((UnitDimension) object);
        {
            Unit lhsUnit;
            lhsUnit = this.getUnit();
            Unit rhsUnit;
            rhsUnit = that.getUnit();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "unit", lhsUnit), LocatorUtils.property(thatLocator, "unit", rhsUnit), lhsUnit, rhsUnit)) {
                return false;
            }
        }
        {
            Double lhsReferenceLength;
            lhsReferenceLength = this.getReferenceLength();
            Double rhsReferenceLength;
            rhsReferenceLength = that.getReferenceLength();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "referenceLength", lhsReferenceLength), LocatorUtils.property(thatLocator, "referenceLength", rhsReferenceLength), lhsReferenceLength, rhsReferenceLength)) {
                return false;
            }
        }
        {
            Unit lhsReferenceUnit;
            lhsReferenceUnit = this.getReferenceUnit();
            Unit rhsReferenceUnit;
            rhsReferenceUnit = that.getReferenceUnit();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "referenceUnit", lhsReferenceUnit), LocatorUtils.property(thatLocator, "referenceUnit", rhsReferenceUnit), lhsReferenceUnit, rhsReferenceUnit)) {
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
            Unit theUnit;
            theUnit = this.getUnit();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "unit", theUnit), currentHashCode, theUnit);
        }
        {
            Double theReferenceLength;
            theReferenceLength = this.getReferenceLength();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "referenceLength", theReferenceLength), currentHashCode, theReferenceLength);
        }
        {
            Unit theReferenceUnit;
            theReferenceUnit = this.getReferenceUnit();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "referenceUnit", theReferenceUnit), currentHashCode, theReferenceUnit);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
