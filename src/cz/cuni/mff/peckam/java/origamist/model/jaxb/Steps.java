//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.27 at 03:59:33 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.common.GeneratedClassBase;
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
 * <p>Java class for Steps complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Steps">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="step" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Step" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "Steps", propOrder = {
    "step"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Steps
    extends GeneratedClassBase
    implements Equals, HashCode
{

    /**
     * Property step
     * 
     */
    public final static String STEP_PROPERTY = "step:cz.cuni.mff.peckam.java.origamist.model.jaxb.Steps";
    protected List<cz.cuni.mff.peckam.java.origamist.model.Step> step = new ObservableList<cz.cuni.mff.peckam.java.origamist.model.Step>();

    public Steps() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.Steps")) {
            init();
        }
    }

    /**
     * Gets the value of the step property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the step property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.model.Step }
     * 
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Step.class)
    public List<cz.cuni.mff.peckam.java.origamist.model.Step> getStep() {
        if (step == null) {
            step = new ObservableList<cz.cuni.mff.peckam.java.origamist.model.Step>();
        }
        return this.step;
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getStep()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {STEP_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Steps)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Steps that = ((Steps) object);
        {
            List<cz.cuni.mff.peckam.java.origamist.model.Step> lhsStep;
            lhsStep = this.getStep();
            List<cz.cuni.mff.peckam.java.origamist.model.Step> rhsStep;
            rhsStep = that.getStep();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "step", lhsStep), LocatorUtils.property(thatLocator, "step", rhsStep), lhsStep, rhsStep)) {
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
            List<cz.cuni.mff.peckam.java.origamist.model.Step> theStep;
            theStep = this.getStep();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "step", theStep), currentHashCode, theStep);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
