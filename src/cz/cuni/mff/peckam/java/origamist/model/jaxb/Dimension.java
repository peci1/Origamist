//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.05 at 02:50:24 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for Dimension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Dimension">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Dimension", propOrder = {
    "width",
    "height"
})
@XmlSeeAlso({
    UnitDimension.class
})
public class Dimension
    implements Equals, HashCode
{

    protected double width;
    protected double height;

    /**
     * Gets the value of the width property.
     * 
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     */
    public void setWidth(double value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     * 
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     */
    public void setHeight(double value) {
        this.height = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Dimension)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Dimension that = ((Dimension) object);
        {
            double lhsWidth;
            lhsWidth = this.getWidth();
            double rhsWidth;
            rhsWidth = that.getWidth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "width", lhsWidth), LocatorUtils.property(thatLocator, "width", rhsWidth), lhsWidth, rhsWidth)) {
                return false;
            }
        }
        {
            double lhsHeight;
            lhsHeight = this.getHeight();
            double rhsHeight;
            rhsHeight = that.getHeight();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "height", lhsHeight), LocatorUtils.property(thatLocator, "height", rhsHeight), lhsHeight, rhsHeight)) {
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
            double theWidth;
            theWidth = this.getWidth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "width", theWidth), currentHashCode, theWidth);
        }
        {
            double theHeight;
            theHeight = this.getHeight();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "height", theHeight), currentHashCode, theHeight);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
