//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.29 at 09:23:44 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TurnOverOperations.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TurnOverOperations">
 *   &lt;restriction base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Operations">
 *     &lt;enumeration value="TURN_OVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TurnOverOperations")
@XmlEnum(Operations.class)
public enum TurnOverOperations {

    TURN_OVER;

    public String value() {
        return name();
    }

    public static TurnOverOperations fromValue(String v) {
        return valueOf(v);
    }

}
