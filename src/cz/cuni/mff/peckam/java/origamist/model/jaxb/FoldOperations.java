//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.15 at 12:06:19 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FoldOperations.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FoldOperations">
 *   &lt;restriction base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Operations">
 *     &lt;enumeration value="VALLEY_FOLD"/>
 *     &lt;enumeration value="MOUNTAIN_FOLD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FoldOperations")
@XmlEnum(Operations.class)
public enum FoldOperations {

    VALLEY_FOLD,
    MOUNTAIN_FOLD;

    public String value() {
        return name();
    }

    public static FoldOperations fromValue(String v) {
        return valueOf(v);
    }

}
