//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.23 at 01:08:05 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.Operation;
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
 * <p>Provided property: displayed
 * <p>Java class for RepeatOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RepeatOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}RepeatOperationBase">
 *       &lt;group ref="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}operations"/>
 *       &lt;attribute name="displayed" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "RepeatOperation", propOrder = {
    "operations"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RepeatOperation
    extends RepeatOperationBase
    implements Equals, HashCode
{

    protected List<Operation> operations = new ObservableList<Operation>();
    protected boolean displayed;
    /**
     * Property displayed
     * 
     */
    public final static String DISPLAYED_PROPERTY = "displayed:cz.cuni.mff.peckam.java.origamist.model.jaxb.RepeatOperation";
    /**
     * Property operations
     * 
     */
    public final static String OPERATIONS_PROPERTY = "operations:cz.cuni.mff.peckam.java.origamist.model.jaxb.RepeatOperation";

    public RepeatOperation() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.RepeatOperation")) {
            init();
        }
    }

    /**
     * Gets the value of the operations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ReverseFoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldUnfoldOperation }
     * {@link RepeatOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.TurnOverOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.MarkerOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.CrimpFoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ThunderboltFoldOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.RotateOperation }
     * {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.FoldOperation }
     * 
     * 
     */
    @XmlElements({
        @XmlElement(name = "reverseFoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.ReverseFoldOperation.class),
        @XmlElement(name = "foldUnfoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.FoldUnfoldOperation.class),
        @XmlElement(name = "repeatOperation", type = cz.cuni.mff.peckam.java.origamist.model.RepeatOperation.class),
        @XmlElement(name = "turnOverOperation", type = cz.cuni.mff.peckam.java.origamist.model.TurnOverOperation.class),
        @XmlElement(name = "markerOperation", type = cz.cuni.mff.peckam.java.origamist.model.MarkerOperation.class),
        @XmlElement(name = "crimpFoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.CrimpFoldOperation.class),
        @XmlElement(name = "thunderboltFoldOperation", type = cz.cuni.mff.peckam.java.origamist.model.ThunderboltFoldOperation.class),
        @XmlElement(name = "rotateOperation", type = cz.cuni.mff.peckam.java.origamist.model.RotateOperation.class),
        @XmlElement(name = "foldOperation", type = cz.cuni.mff.peckam.java.origamist.model.FoldOperation.class)
    })
    public List<Operation> getOperations() {
        if (operations == null) {
            operations = new ObservableList<Operation>();
        }
        return this.operations;
    }

    /**
     * Gets the value of the displayed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDisplayed() {
        return displayed;
    }

    /**
     * Sets the value of the displayed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @XmlAttribute(required = true)
    public void setDisplayed(boolean value) {
        boolean old = this.displayed;
        this.displayed = value;
        if (old!= value) {
            support.firePropertyChange(RepeatOperation.DISPLAYED_PROPERTY, old, value);
        }
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getOperations()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {OPERATIONS_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof RepeatOperation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final RepeatOperation that = ((RepeatOperation) object);
        {
            List<Operation> lhsOperations;
            lhsOperations = this.getOperations();
            List<Operation> rhsOperations;
            rhsOperations = that.getOperations();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "operations", lhsOperations), LocatorUtils.property(thatLocator, "operations", rhsOperations), lhsOperations, rhsOperations)) {
                return false;
            }
        }
        {
            boolean lhsDisplayed;
            lhsDisplayed = this.isDisplayed();
            boolean rhsDisplayed;
            rhsDisplayed = that.isDisplayed();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "displayed", lhsDisplayed), LocatorUtils.property(thatLocator, "displayed", rhsDisplayed), lhsDisplayed, rhsDisplayed)) {
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
            List<Operation> theOperations;
            theOperations = this.getOperations();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "operations", theOperations), currentHashCode, theOperations);
        }
        {
            boolean theDisplayed;
            theDisplayed = this.isDisplayed();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "displayed", theDisplayed), currentHashCode, theDisplayed);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
