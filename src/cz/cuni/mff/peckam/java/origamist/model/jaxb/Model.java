//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.13 at 02:43:54 dop. CEST 
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
 * <p>Provided property: paper
 * <p>Provided property: steps
 * <p>Java class for Model complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Model">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paper" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}ModelPaper"/>
 *         &lt;element name="steps" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Steps"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "Model", propOrder = {
    "paper",
    "steps"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Model
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.model.ModelPaper paper;
    /**
     * Property paper
     * 
     */
    public final static String PAPER_PROPERTY = "paper:cz.cuni.mff.peckam.java.origamist.model.jaxb.Model";
    protected cz.cuni.mff.peckam.java.origamist.model.Steps steps;
    /**
     * Property steps
     * 
     */
    public final static String STEPS_PROPERTY = "steps:cz.cuni.mff.peckam.java.origamist.model.jaxb.Model";

    public Model() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.Model")) {
            init();
        }
    }

    /**
     * Gets the value of the paper property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.ModelPaper.class)
    public cz.cuni.mff.peckam.java.origamist.model.ModelPaper getPaper() {
        return paper;
    }

    /**
     * Sets the value of the paper property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper }
     *     
     */
    public void setPaper(cz.cuni.mff.peckam.java.origamist.model.ModelPaper value) {
        cz.cuni.mff.peckam.java.origamist.model.ModelPaper old = this.paper;
        this.paper = ((cz.cuni.mff.peckam.java.origamist.model.ModelPaper) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Model.PAPER_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the steps property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Steps }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Steps.class)
    public cz.cuni.mff.peckam.java.origamist.model.Steps getSteps() {
        return steps;
    }

    /**
     * Sets the value of the steps property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Steps }
     *     
     */
    public void setSteps(cz.cuni.mff.peckam.java.origamist.model.Steps value) {
        cz.cuni.mff.peckam.java.origamist.model.Steps old = this.steps;
        this.steps = ((cz.cuni.mff.peckam.java.origamist.model.Steps) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Model.STEPS_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Model)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Model that = ((Model) object);
        {
            cz.cuni.mff.peckam.java.origamist.model.ModelPaper lhsPaper;
            lhsPaper = this.getPaper();
            cz.cuni.mff.peckam.java.origamist.model.ModelPaper rhsPaper;
            rhsPaper = that.getPaper();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "paper", lhsPaper), LocatorUtils.property(thatLocator, "paper", rhsPaper), lhsPaper, rhsPaper)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Steps lhsSteps;
            lhsSteps = this.getSteps();
            cz.cuni.mff.peckam.java.origamist.model.Steps rhsSteps;
            rhsSteps = that.getSteps();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "steps", lhsSteps), LocatorUtils.property(thatLocator, "steps", rhsSteps), lhsSteps, rhsSteps)) {
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
            cz.cuni.mff.peckam.java.origamist.model.ModelPaper thePaper;
            thePaper = this.getPaper();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "paper", thePaper), currentHashCode, thePaper);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Steps theSteps;
            theSteps = this.getSteps();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "steps", theSteps), currentHashCode, theSteps);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
