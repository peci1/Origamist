//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.23 at 01:08:05 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.model.Operation;
import cz.cuni.mff.peckam.java.origamist.model.TurnOverOperation;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;


/**
 * <p>Java class for TurnOverOperationBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TurnOverOperationBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Operation">
 *       &lt;attribute name="type" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}TurnOverOperations" fixed="TURN_OVER" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "TurnOverOperationBase")
@XmlSeeAlso({
    TurnOverOperation.class
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class TurnOverOperationBase
    extends Operation
    implements Equals, HashCode
{


    public TurnOverOperationBase() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.jaxb.TurnOverOperationBase")) {
            init();
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TurnOverOperationBase)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = super.hashCode(locator, strategy);
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
