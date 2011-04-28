//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.28 at 02:33:15 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 * <p>Provided property: background
 * <p>Provided property: foreground
 * <p>Java class for ModelColors complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModelColors">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Colors">
 *       &lt;sequence>
 *         &lt;element name="background" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Color"/>
 *         &lt;element name="foreground" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Color"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModelColors", propOrder = {
    "background",
    "foreground"
})
public class ModelColors
    extends Colors
    implements HasBoundProperties, Equals, HashCode
{

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Color background;
    /**
     * Property background
     * 
     */
    public final static String BACKGROUND_PROPERTY = "background";
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Color foreground;
    /**
     * Property foreground
     * 
     */
    public final static String FOREGROUND_PROPERTY = "foreground";
    protected transient CustomPropertyChangeSupport support = (new CustomPropertyChangeSupport(this));

    /**
     * Gets the value of the background property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Sets the value of the background property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackground(Color value) {
        Color old = this.background;
        this.background = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(ModelColors.BACKGROUND_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the foreground property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     * Sets the value of the foreground property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeground(Color value) {
        Color old = this.foreground;
        this.foreground = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(ModelColors.FOREGROUND_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ModelColors)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final ModelColors that = ((ModelColors) object);
        {
            Color lhsBackground;
            lhsBackground = this.getBackground();
            Color rhsBackground;
            rhsBackground = that.getBackground();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "background", lhsBackground), LocatorUtils.property(thatLocator, "background", rhsBackground), lhsBackground, rhsBackground)) {
                return false;
            }
        }
        {
            Color lhsForeground;
            lhsForeground = this.getForeground();
            Color rhsForeground;
            rhsForeground = that.getForeground();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "foreground", lhsForeground), LocatorUtils.property(thatLocator, "foreground", rhsForeground), lhsForeground, rhsForeground)) {
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
            Color theBackground;
            theBackground = this.getBackground();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "background", theBackground), currentHashCode, theBackground);
        }
        {
            Color theForeground;
            theForeground = this.getForeground();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "foreground", theForeground), currentHashCode, theForeground);
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

    public void addPropertyChangeListener(String param0, PropertyChangeListener param1) {
        this.support.addPropertyChangeListener(param0, param1);
    }

    public void addPropertyChangeListener(PropertyChangeListener param0) {
        this.support.addPropertyChangeListener(param0);
    }

    public void removePropertyChangeListener(String param0, PropertyChangeListener param1) {
        this.support.removePropertyChangeListener(param0, param1);
    }

    public void removePropertyChangeListener(PropertyChangeListener param0) {
        this.support.removePropertyChangeListener(param0);
    }

}
