//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.27 at 06:31:40 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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
 * <p>Provided property: refPoint
 * <p>Java class for RotateOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RotateOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}RotateOperationBase">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}angle"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}refPoint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "RotateOperation", propOrder = {
    "angle",
    "refPoint"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RotateOperation
    extends RotateOperationBase
    implements Equals, HashCode
{

    protected double angle;
    /**
     * Property angle
     * 
     */
    public final static String ANGLE_PROPERTY = "angle:cz.cuni.mff.peckam.java.origamist.model.jaxb.RotateOperation";
    protected cz.cuni.mff.peckam.java.origamist.model.Point2D refPoint;
    /**
     * Property refPoint
     * 
     */
    public final static String REF_POINT_PROPERTY = "refPoint:cz.cuni.mff.peckam.java.origamist.model.jaxb.RotateOperation";

    public RotateOperation() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.RotateOperation")) {
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
            support.firePropertyChange(RotateOperation.ANGLE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the refPoint property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.model.Point2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Point2D getRefPoint() {
        return refPoint;
    }

    /**
     * Sets the value of the refPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
    public void setRefPoint(cz.cuni.mff.peckam.java.origamist.model.Point2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Point2D old = this.refPoint;
        this.refPoint = ((cz.cuni.mff.peckam.java.origamist.model.Point2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(RotateOperation.REF_POINT_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof RotateOperation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final RotateOperation that = ((RotateOperation) object);
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
            cz.cuni.mff.peckam.java.origamist.model.Point2D lhsRefPoint;
            lhsRefPoint = this.getRefPoint();
            cz.cuni.mff.peckam.java.origamist.model.Point2D rhsRefPoint;
            rhsRefPoint = that.getRefPoint();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "refPoint", lhsRefPoint), LocatorUtils.property(thatLocator, "refPoint", rhsRefPoint), lhsRefPoint, rhsRefPoint)) {
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
            cz.cuni.mff.peckam.java.origamist.model.Point2D theRefPoint;
            theRefPoint = this.getRefPoint();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "refPoint", theRefPoint), currentHashCode, theRefPoint);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
