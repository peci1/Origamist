//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.27 at 03:46:59 odp. CEST 
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
 * <p>Provided property: line
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
 *         &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}layerList"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "FoldUnfoldOperation", propOrder = {
    "line",
    "layer"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class FoldUnfoldOperation
    extends FoldUnfoldOperationBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.model.Line2D line;
    /**
     * Property line
     * 
     */
    public final static String LINE_PROPERTY = "line:cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldUnfoldOperation";
    protected List<Integer> layer = new ObservableList<Integer>();
    /**
     * Property layer
     * 
     */
    public final static String LAYER_PROPERTY = "layer:cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldUnfoldOperation";

    public FoldUnfoldOperation() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.FoldUnfoldOperation")) {
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
            support.firePropertyChange(FoldUnfoldOperation.LINE_PROPERTY, old, value);
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
