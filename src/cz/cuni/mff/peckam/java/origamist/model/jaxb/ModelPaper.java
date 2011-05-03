//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.02 at 11:41:02 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.Paper;
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
 * <p>Provided property: colors
 * <p>Provided property: weight
 * <p>Java class for ModelPaper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModelPaper">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Paper">
 *       &lt;sequence>
 *         &lt;element name="colors" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}ModelColors"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="note" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "ModelPaper", propOrder = {
    "colors",
    "weight",
    "note"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ModelPaper
    extends Paper
    implements Equals, HashCode
{

    protected ModelColors colors;
    /**
     * Property colors
     * 
     */
    public final static String COLORS_PROPERTY = "colors:cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper";
    protected double weight;
    /**
     * Property weight
     * 
     */
    public final static String WEIGHT_PROPERTY = "weight:cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper";
    /**
     * Property note
     * 
     */
    public final static String NOTE_PROPERTY = "note:cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper";
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> note = new ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();

    public ModelPaper() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.ModelPaper")) {
            init();
        }
    }

    /**
     * Gets the value of the colors property.
     * 
     * @return
     *     possible object is
     *     {@link ModelColors }
     *     
     */
    @XmlElement(required = true)
    public ModelColors getColors() {
        return colors;
    }

    /**
     * Sets the value of the colors property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelColors }
     *     
     */
    public void setColors(ModelColors value) {
        ModelColors old = this.colors;
        this.colors = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(ModelPaper.COLORS_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWeight(double value) {
        double old = this.weight;
        this.weight = value;
        if (old!= value) {
            support.firePropertyChange(ModelPaper.WEIGHT_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getNote() {
        if (note == null) {
            note = new ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
        }
        return this.note;
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getNote()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses.
     */
    public String[] getListProperties() {
        return new String[] {NOTE_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ModelPaper)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final ModelPaper that = ((ModelPaper) object);
        {
            ModelColors lhsColors;
            lhsColors = this.getColors();
            ModelColors rhsColors;
            rhsColors = that.getColors();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "colors", lhsColors), LocatorUtils.property(thatLocator, "colors", rhsColors), lhsColors, rhsColors)) {
                return false;
            }
        }
        {
            double lhsWeight;
            lhsWeight = this.getWeight();
            double rhsWeight;
            rhsWeight = that.getWeight();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "weight", lhsWeight), LocatorUtils.property(thatLocator, "weight", rhsWeight), lhsWeight, rhsWeight)) {
                return false;
            }
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> lhsNote;
            lhsNote = this.getNote();
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> rhsNote;
            rhsNote = that.getNote();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "note", lhsNote), LocatorUtils.property(thatLocator, "note", rhsNote), lhsNote, rhsNote)) {
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
            ModelColors theColors;
            theColors = this.getColors();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "colors", theColors), currentHashCode, theColors);
        }
        {
            double theWeight;
            theWeight = this.getWeight();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "weight", theWeight), currentHashCode, theWeight);
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> theNote;
            theNote = this.getNote();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "note", theNote), currentHashCode, theNote);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
