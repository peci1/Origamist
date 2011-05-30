//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.30 at 01:37:07 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 * <p>Java class for DiagramColors complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DiagramColors">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Colors">
 *       &lt;sequence>
 *         &lt;element name="background" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Color"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "DiagramColors", propOrder = {
    "background"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DiagramColors
    extends Colors
    implements Equals, HashCode
{

    protected Color background;
    /**
     * Property background
     * 
     */
    public final static String BACKGROUND_PROPERTY = "background:cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramColors";

    public DiagramColors() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramColors")) {
            init();
        }
    }

    /**
     * Gets the value of the background property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
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
            support.firePropertyChange(DiagramColors.BACKGROUND_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof DiagramColors)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final DiagramColors that = ((DiagramColors) object);
        {
            Color lhsBackground;
            lhsBackground = this.getBackground();
            Color rhsBackground;
            rhsBackground = that.getBackground();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "background", lhsBackground), LocatorUtils.property(thatLocator, "background", rhsBackground), lhsBackground, rhsBackground)) {
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
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
