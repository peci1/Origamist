//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.28 at 03:36:51 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Operations.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Operations">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UNDEFINED"/>
 *     &lt;enumeration value="VALLEY_FOLD"/>
 *     &lt;enumeration value="MOUNTAIN_FOLD"/>
 *     &lt;enumeration value="MOUNTAIN_VALLEY_FOLD_UNFOLD"/>
 *     &lt;enumeration value="VALLEY_MOUNTAIN_FOLD_UNFOLD"/>
 *     &lt;enumeration value="THUNDERBOLT_FOLD"/>
 *     &lt;enumeration value="TURN_OVER"/>
 *     &lt;enumeration value="ROTATE"/>
 *     &lt;enumeration value="PULL"/>
 *     &lt;enumeration value="INSIDE_CRIMP_FOLD"/>
 *     &lt;enumeration value="OUTSIDE_CRIMP_FOLD"/>
 *     &lt;enumeration value="OPEN"/>
 *     &lt;enumeration value="INSIDE_REVERSE_FOLD"/>
 *     &lt;enumeration value="OUTSIDE_REVERSE_FOLD"/>
 *     &lt;enumeration value="MARKER"/>
 *     &lt;enumeration value="REPEAT_ACTION"/>
 *     &lt;enumeration value="RABBIT_FOLD"/>
 *     &lt;enumeration value="SQUASH_FOLD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Operations")
@XmlEnum
public enum Operations {

    UNDEFINED,
    VALLEY_FOLD,
    MOUNTAIN_FOLD,
    MOUNTAIN_VALLEY_FOLD_UNFOLD,
    VALLEY_MOUNTAIN_FOLD_UNFOLD,
    THUNDERBOLT_FOLD,
    TURN_OVER,
    ROTATE,
    PULL,
    INSIDE_CRIMP_FOLD,
    OUTSIDE_CRIMP_FOLD,
    OPEN,
    INSIDE_REVERSE_FOLD,
    OUTSIDE_REVERSE_FOLD,
    MARKER,
    REPEAT_ACTION,
    RABBIT_FOLD,
    SQUASH_FOLD;

    public String value() {
        return name();
    }

    public static Operations fromValue(String v) {
        return valueOf(v);
    }

}
