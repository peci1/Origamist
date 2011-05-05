//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.05 at 11:44:00 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 * <p>Provided property: start
 * <p>Provided property: end
 * <p>Java class for Line2D complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Line2D">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="start" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Point2D"/>
 *         &lt;element name="end" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Point2D"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "Line2D", propOrder = {
    "start",
    "end"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Line2D
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.model.Point2D start;
    /**
     * Property start
     * 
     */
    public final static String START_PROPERTY = "start:cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D";
    protected cz.cuni.mff.peckam.java.origamist.model.Point2D end;
    /**
     * Property end
     * 
     */
    public final static String END_PROPERTY = "end:cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D";

    public Line2D() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.Line2D")) {
            init();
        }
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Point2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Point2D getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
    public void setStart(cz.cuni.mff.peckam.java.origamist.model.Point2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Point2D old = this.start;
        this.start = ((cz.cuni.mff.peckam.java.origamist.model.Point2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Line2D.START_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Point2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Point2D getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
    public void setEnd(cz.cuni.mff.peckam.java.origamist.model.Point2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Point2D old = this.end;
        this.end = ((cz.cuni.mff.peckam.java.origamist.model.Point2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Line2D.END_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Line2D)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Line2D that = ((Line2D) object);
        {
            cz.cuni.mff.peckam.java.origamist.model.Point2D lhsStart;
            lhsStart = this.getStart();
            cz.cuni.mff.peckam.java.origamist.model.Point2D rhsStart;
            rhsStart = that.getStart();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "start", lhsStart), LocatorUtils.property(thatLocator, "start", rhsStart), lhsStart, rhsStart)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Point2D lhsEnd;
            lhsEnd = this.getEnd();
            cz.cuni.mff.peckam.java.origamist.model.Point2D rhsEnd;
            rhsEnd = that.getEnd();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "end", lhsEnd), LocatorUtils.property(thatLocator, "end", rhsEnd), lhsEnd, rhsEnd)) {
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
            cz.cuni.mff.peckam.java.origamist.model.Point2D theStart;
            theStart = this.getStart();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "start", theStart), currentHashCode, theStart);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Point2D theEnd;
            theEnd = this.getEnd();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "end", theEnd), currentHashCode, theEnd);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
