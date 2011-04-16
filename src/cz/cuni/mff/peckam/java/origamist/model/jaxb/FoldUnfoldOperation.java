//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.15 at 12:06:19 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * <p>Java class for FoldUnfoldOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FoldUnfoldOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}FoldUnfoldOperationBase">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}line"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}refPoint" minOccurs="0"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}layerList"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FoldUnfoldOperation", propOrder = {
    "line",
    "refPoint",
    "layer"
})
public class FoldUnfoldOperation
    extends FoldUnfoldOperationBase
    implements Equals, HashCode
{

    @XmlElement(type = Integer.class)
    protected List<Integer> layer = new ObservableList<Integer>();
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Line2D.class)
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D line;
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.model.Point2D.class)
    protected cz.cuni.mff.peckam.java.origamist.model.Point2D refPoint;

    /**
     * Gets the value of the line property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
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
        this.line = value;
    }

    /**
     * Gets the value of the refPoint property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Point2D }
     *     
     */
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
        this.refPoint = value;
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
    public List<Integer> getLayer() {
        if (layer == null) {
            layer = new ObservableList<Integer>();
        }
        return this.layer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof FoldUnfoldOperation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final FoldUnfoldOperation that = ((FoldUnfoldOperation) object);
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
            cz.cuni.mff.peckam.java.origamist.model.Point2D lhsRefPoint;
            lhsRefPoint = this.getRefPoint();
            cz.cuni.mff.peckam.java.origamist.model.Point2D rhsRefPoint;
            rhsRefPoint = that.getRefPoint();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "refPoint", lhsRefPoint), LocatorUtils.property(thatLocator, "refPoint", rhsRefPoint), lhsRefPoint, rhsRefPoint)) {
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
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = super.hashCode(locator, strategy);
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D theLine;
            theLine = this.getLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "line", theLine), currentHashCode, theLine);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Point2D theRefPoint;
            theRefPoint = this.getRefPoint();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "refPoint", theRefPoint), currentHashCode, theRefPoint);
        }
        {
            List<Integer> theLayer;
            theLayer = this.getLayer();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "layer", theLayer), currentHashCode, theLayer);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
