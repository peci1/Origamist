//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.29 at 09:23:44 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import cz.cuni.mff.peckam.java.origamist.model.Paper;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Provided property: color
 * <p>Provided property: cols
 * <p>Provided property: rows
 * <p>Java class for DiagramPaper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DiagramPaper">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Paper">
 *       &lt;sequence>
 *         &lt;element name="color" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}DiagramColors"/>
 *         &lt;element name="cols" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="rows" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "DiagramPaper", propOrder = {
    "color",
    "cols",
    "rows"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DiagramPaper
    extends Paper
    implements Equals, HashCode
{

    protected DiagramColors color;
    /**
     * Property color
     * 
     */
    public final static String COLOR_PROPERTY = "color:cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper";
    protected Integer cols;
    /**
     * Property cols
     * 
     */
    public final static String COLS_PROPERTY = "cols:cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper";
    protected Integer rows;
    /**
     * Property rows
     * 
     */
    public final static String ROWS_PROPERTY = "rows:cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper";

    public DiagramPaper() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.DiagramPaper")) {
            init();
        }
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link DiagramColors }
     *     
     */
    @XmlElement(required = true)
    public DiagramColors getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagramColors }
     *     
     */
    public void setColor(DiagramColors value) {
        DiagramColors old = this.color;
        this.color = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(DiagramPaper.COLOR_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the cols property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    public Integer getCols() {
        return cols;
    }

    /**
     * Sets the value of the cols property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCols(Integer value) {
        Integer old = this.cols;
        this.cols = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(DiagramPaper.COLS_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the rows property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    public Integer getRows() {
        return rows;
    }

    /**
     * Sets the value of the rows property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRows(Integer value) {
        Integer old = this.rows;
        this.rows = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(DiagramPaper.ROWS_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof DiagramPaper)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final DiagramPaper that = ((DiagramPaper) object);
        {
            DiagramColors lhsColor;
            lhsColor = this.getColor();
            DiagramColors rhsColor;
            rhsColor = that.getColor();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "color", lhsColor), LocatorUtils.property(thatLocator, "color", rhsColor), lhsColor, rhsColor)) {
                return false;
            }
        }
        {
            Integer lhsCols;
            lhsCols = this.getCols();
            Integer rhsCols;
            rhsCols = that.getCols();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cols", lhsCols), LocatorUtils.property(thatLocator, "cols", rhsCols), lhsCols, rhsCols)) {
                return false;
            }
        }
        {
            Integer lhsRows;
            lhsRows = this.getRows();
            Integer rhsRows;
            rhsRows = that.getRows();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "rows", lhsRows), LocatorUtils.property(thatLocator, "rows", rhsRows), lhsRows, rhsRows)) {
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
            DiagramColors theColor;
            theColor = this.getColor();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "color", theColor), currentHashCode, theColor);
        }
        {
            Integer theCols;
            theCols = this.getCols();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cols", theCols), currentHashCode, theCols);
        }
        {
            Integer theRows;
            theRows = this.getRows();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "rows", theRows), currentHashCode, theRows);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
