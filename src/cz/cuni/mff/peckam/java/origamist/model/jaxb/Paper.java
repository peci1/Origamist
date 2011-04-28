//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.29 at 12:54:14 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.beans.PropertyChangeListener;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.DiagramPaper;
import cz.cuni.mff.peckam.java.origamist.model.ModelPaper;
import cz.cuni.mff.peckam.java.origamist.utils.CustomPropertyChangeSupport;
import cz.cuni.mff.peckam.java.origamist.utils.HasBoundProperties;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: size
 * <p>Java class for Paper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Paper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="size" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}UnitDimension"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "Paper", propOrder = {
    "size"
})
@XmlSeeAlso({
    ModelPaper.class,
    DiagramPaper.class
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class Paper implements HasBoundProperties, Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.model.UnitDimension size;
    /**
     * Property size
     * 
     */
    public final static String SIZE_PROPERTY = "size";
    protected transient CustomPropertyChangeSupport support = (new CustomPropertyChangeSupport(this));

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.UnitDimension.class)
    public cz.cuni.mff.peckam.java.origamist.model.UnitDimension getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension }
     *     
     */
    public void setSize(cz.cuni.mff.peckam.java.origamist.model.UnitDimension value) {
        cz.cuni.mff.peckam.java.origamist.model.UnitDimension old = this.size;
        this.size = ((cz.cuni.mff.peckam.java.origamist.model.UnitDimension) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Paper.SIZE_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Paper)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Paper that = ((Paper) object);
        {
            cz.cuni.mff.peckam.java.origamist.model.UnitDimension lhsSize;
            lhsSize = this.getSize();
            cz.cuni.mff.peckam.java.origamist.model.UnitDimension rhsSize;
            rhsSize = that.getSize();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "size", lhsSize), LocatorUtils.property(thatLocator, "size", rhsSize), lhsSize, rhsSize)) {
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
            cz.cuni.mff.peckam.java.origamist.model.UnitDimension theSize;
            theSize = this.getSize();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "size", theSize), currentHashCode, theSize);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public List<String> removeAllListeners(PropertyChangeListener param0) {
        return this.support.removeAllListeners(param0);
    }

    public void addPropertyChangeListener(PropertyChangeListener param0) {
        this.support.addPropertyChangeListener(param0);
    }

    public void addPropertyChangeListener(String param0, PropertyChangeListener param1) {
        this.support.addPropertyChangeListener(param0, param1);
    }

    public void removePropertyChangeListener(String param0, PropertyChangeListener param1) {
        this.support.removePropertyChangeListener(param0, param1);
    }

    public void removePropertyChangeListener(PropertyChangeListener param0) {
        this.support.removePropertyChangeListener(param0);
    }

}
