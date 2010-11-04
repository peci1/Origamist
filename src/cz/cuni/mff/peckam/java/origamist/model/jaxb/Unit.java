//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.04 at 01:05:07 dop. CET 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Unit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Unit">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="mm"/>
 *     &lt;enumeration value="cm"/>
 *     &lt;enumeration value="m"/>
 *     &lt;enumeration value="km"/>
 *     &lt;enumeration value="inch"/>
 *     &lt;enumeration value="foot"/>
 *     &lt;enumeration value="mile"/>
 *     &lt;enumeration value="rel"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Unit")
@XmlEnum
public enum Unit {

    @XmlEnumValue("mm")
    MM("mm"),
    @XmlEnumValue("cm")
    CM("cm"),
    @XmlEnumValue("m")
    M("m"),
    @XmlEnumValue("km")
    KM("km"),
    @XmlEnumValue("inch")
    INCH("inch"),
    @XmlEnumValue("foot")
    FOOT("foot"),
    @XmlEnumValue("mile")
    MILE("mile"),
    @XmlEnumValue("rel")
    REL("rel");
    private final String value;

    Unit(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Unit fromValue(String v) {
        for (Unit c: Unit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
