//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.13 at 02:43:54 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.common.GeneratedClassBase;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: x
 * <p>Provided property: y
 * <p>Java class for Point2D complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Point2D">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "Point2D", propOrder = {
    "x",
    "y"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Point2D
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected double x;
    /**
     * Property x
     * 
     */
    public final static String X_PROPERTY = "x:cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D";
    protected double y;
    /**
     * Property y
     * 
     */
    public final static String Y_PROPERTY = "y:cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D";

    public Point2D() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.Point2D")) {
            init();
        }
    }

    /**
     * Gets the value of the x property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setX(double value) {
        double old = this.x;
        this.x = value;
        if (old!= value) {
            support.firePropertyChange(Point2D.X_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the y property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setY(double value) {
        double old = this.y;
        this.y = value;
        if (old!= value) {
            support.firePropertyChange(Point2D.Y_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Point2D)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Point2D that = ((Point2D) object);
        {
            double lhsX;
            lhsX = this.getX();
            double rhsX;
            rhsX = that.getX();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "x", lhsX), LocatorUtils.property(thatLocator, "x", rhsX), lhsX, rhsX)) {
                return false;
            }
        }
        {
            double lhsY;
            lhsY = this.getY();
            double rhsY;
            rhsY = that.getY();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "y", lhsY), LocatorUtils.property(thatLocator, "y", rhsY), lhsY, rhsY)) {
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
            double theX;
            theX = this.getX();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "x", theX), currentHashCode, theX);
        }
        {
            double theY;
            theY = this.getY();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "y", theY), currentHashCode, theY);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
