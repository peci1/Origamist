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
 * <p>Provided property: line
 * <p>Provided property: refLine
 * <p>Provided property: oppositeLine
 * <p>Provided property: secondLine
 * <p>Provided property: secondOppositeLine
 * <p>Java class for CrimpFoldOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrimpFoldOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}CrimpFoldOperationBase">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}line"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}refLine"/>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}layerList"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="oppositeLine" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Line2D"/>
 *           &lt;element name="oppositeLayer" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}secondLine"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="secondOppositeLine" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Line2D"/>
 *           &lt;element name="secondOppositeLayer" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "CrimpFoldOperation", propOrder = {
    "line",
    "refLine",
    "layer",
    "oppositeLine",
    "oppositeLayer",
    "secondLine",
    "secondOppositeLine",
    "secondOppositeLayer"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CrimpFoldOperation
    extends CrimpFoldOperationBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.model.Line2D line;
    /**
     * Property line
     * 
     */
    public final static String LINE_PROPERTY = "line:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D refLine;
    /**
     * Property refLine
     * 
     */
    public final static String REF_LINE_PROPERTY = "refLine:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    protected List<Integer> layer = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Integer>();
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D oppositeLine;
    /**
     * Property oppositeLine
     * 
     */
    public final static String OPPOSITE_LINE_PROPERTY = "oppositeLine:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    protected List<Integer> oppositeLayer = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Integer>();
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D secondLine;
    /**
     * Property secondLine
     * 
     */
    public final static String SECOND_LINE_PROPERTY = "secondLine:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    protected cz.cuni.mff.peckam.java.origamist.model.Line2D secondOppositeLine;
    /**
     * Property secondOppositeLine
     * 
     */
    public final static String SECOND_OPPOSITE_LINE_PROPERTY = "secondOppositeLine:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    protected List<Integer> secondOppositeLayer = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Integer>();
    /**
     * Property layer
     * 
     */
    public final static String LAYER_PROPERTY = "layer:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    /**
     * Property oppositeLayer
     * 
     */
    public final static String OPPOSITE_LAYER_PROPERTY = "oppositeLayer:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";
    /**
     * Property secondOppositeLayer
     * 
     */
    public final static String SECOND_OPPOSITE_LAYER_PROPERTY = "secondOppositeLayer:cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation";

    public CrimpFoldOperation() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.CrimpFoldOperation")) {
            init();
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
            support.firePropertyChange(CrimpFoldOperation.LINE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the refLine property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Line2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Line2D getRefLine() {
        return refLine;
    }

    /**
     * Sets the value of the refLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    public void setRefLine(cz.cuni.mff.peckam.java.origamist.model.Line2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Line2D old = this.refLine;
        this.refLine = ((cz.cuni.mff.peckam.java.origamist.model.Line2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(CrimpFoldOperation.REF_LINE_PROPERTY, old, value);
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
            layer = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Integer>();
        }
        return this.layer;
    }

    /**
     * Gets the value of the oppositeLine property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.model.Line2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Line2D getOppositeLine() {
        return oppositeLine;
    }

    /**
     * Sets the value of the oppositeLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    public void setOppositeLine(cz.cuni.mff.peckam.java.origamist.model.Line2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Line2D old = this.oppositeLine;
        this.oppositeLine = ((cz.cuni.mff.peckam.java.origamist.model.Line2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(CrimpFoldOperation.OPPOSITE_LINE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the oppositeLayer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oppositeLayer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOppositeLayer().add(newItem);
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
    public List<Integer> getOppositeLayer() {
        if (oppositeLayer == null) {
            oppositeLayer = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Integer>();
        }
        return this.oppositeLayer;
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
            support.firePropertyChange(CrimpFoldOperation.SECOND_LINE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the secondOppositeLine property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.model.Line2D.class)
    public cz.cuni.mff.peckam.java.origamist.model.Line2D getSecondOppositeLine() {
        return secondOppositeLine;
    }

    /**
     * Sets the value of the secondOppositeLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Line2D }
     *     
     */
    public void setSecondOppositeLine(cz.cuni.mff.peckam.java.origamist.model.Line2D value) {
        cz.cuni.mff.peckam.java.origamist.model.Line2D old = this.secondOppositeLine;
        this.secondOppositeLine = ((cz.cuni.mff.peckam.java.origamist.model.Line2D) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(CrimpFoldOperation.SECOND_OPPOSITE_LINE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the secondOppositeLayer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the secondOppositeLayer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecondOppositeLayer().add(newItem);
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
    public List<Integer> getSecondOppositeLayer() {
        if (secondOppositeLayer == null) {
            secondOppositeLayer = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<Integer>();
        }
        return this.secondOppositeLayer;
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getLayer(), getOppositeLayer(), getSecondOppositeLayer()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {LAYER_PROPERTY, OPPOSITE_LAYER_PROPERTY, SECOND_OPPOSITE_LAYER_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof CrimpFoldOperation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final CrimpFoldOperation that = ((CrimpFoldOperation) object);
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
            cz.cuni.mff.peckam.java.origamist.model.Line2D lhsRefLine;
            lhsRefLine = this.getRefLine();
            cz.cuni.mff.peckam.java.origamist.model.Line2D rhsRefLine;
            rhsRefLine = that.getRefLine();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "refLine", lhsRefLine), LocatorUtils.property(thatLocator, "refLine", rhsRefLine), lhsRefLine, rhsRefLine)) {
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
            cz.cuni.mff.peckam.java.origamist.model.Line2D lhsOppositeLine;
            lhsOppositeLine = this.getOppositeLine();
            cz.cuni.mff.peckam.java.origamist.model.Line2D rhsOppositeLine;
            rhsOppositeLine = that.getOppositeLine();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "oppositeLine", lhsOppositeLine), LocatorUtils.property(thatLocator, "oppositeLine", rhsOppositeLine), lhsOppositeLine, rhsOppositeLine)) {
                return false;
            }
        }
        {
            List<Integer> lhsOppositeLayer;
            lhsOppositeLayer = this.getOppositeLayer();
            List<Integer> rhsOppositeLayer;
            rhsOppositeLayer = that.getOppositeLayer();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "oppositeLayer", lhsOppositeLayer), LocatorUtils.property(thatLocator, "oppositeLayer", rhsOppositeLayer), lhsOppositeLayer, rhsOppositeLayer)) {
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
            cz.cuni.mff.peckam.java.origamist.model.Line2D lhsSecondOppositeLine;
            lhsSecondOppositeLine = this.getSecondOppositeLine();
            cz.cuni.mff.peckam.java.origamist.model.Line2D rhsSecondOppositeLine;
            rhsSecondOppositeLine = that.getSecondOppositeLine();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "secondOppositeLine", lhsSecondOppositeLine), LocatorUtils.property(thatLocator, "secondOppositeLine", rhsSecondOppositeLine), lhsSecondOppositeLine, rhsSecondOppositeLine)) {
                return false;
            }
        }
        {
            List<Integer> lhsSecondOppositeLayer;
            lhsSecondOppositeLayer = this.getSecondOppositeLayer();
            List<Integer> rhsSecondOppositeLayer;
            rhsSecondOppositeLayer = that.getSecondOppositeLayer();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "secondOppositeLayer", lhsSecondOppositeLayer), LocatorUtils.property(thatLocator, "secondOppositeLayer", rhsSecondOppositeLayer), lhsSecondOppositeLayer, rhsSecondOppositeLayer)) {
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
            cz.cuni.mff.peckam.java.origamist.model.Line2D theRefLine;
            theRefLine = this.getRefLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "refLine", theRefLine), currentHashCode, theRefLine);
        }
        {
            List<Integer> theLayer;
            theLayer = this.getLayer();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "layer", theLayer), currentHashCode, theLayer);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D theOppositeLine;
            theOppositeLine = this.getOppositeLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "oppositeLine", theOppositeLine), currentHashCode, theOppositeLine);
        }
        {
            List<Integer> theOppositeLayer;
            theOppositeLayer = this.getOppositeLayer();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "oppositeLayer", theOppositeLayer), currentHashCode, theOppositeLayer);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D theSecondLine;
            theSecondLine = this.getSecondLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "secondLine", theSecondLine), currentHashCode, theSecondLine);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Line2D theSecondOppositeLine;
            theSecondOppositeLine = this.getSecondOppositeLine();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "secondOppositeLine", theSecondOppositeLine), currentHashCode, theSecondOppositeLine);
        }
        {
            List<Integer> theSecondOppositeLayer;
            theSecondOppositeLayer = this.getSecondOppositeLayer();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "secondOppositeLayer", theSecondOppositeLayer), currentHashCode, theSecondOppositeLayer);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
