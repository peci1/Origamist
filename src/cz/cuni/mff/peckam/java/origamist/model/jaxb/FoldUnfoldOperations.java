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
 * <p>Java class for FoldUnfoldOperations.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FoldUnfoldOperations">
 *   &lt;restriction base="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Operations">
 *     &lt;enumeration value="MOUNTAIN_VALLEY_FOLD_UNFOLD"/>
 *     &lt;enumeration value="VALLEY_MOUNTAIN_FOLD_UNFOLD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FoldUnfoldOperations")
@XmlEnum(Operations.class)
public enum FoldUnfoldOperations {

    MOUNTAIN_VALLEY_FOLD_UNFOLD,
    VALLEY_MOUNTAIN_FOLD_UNFOLD;

    public String value() {
        return name();
    }

    public static FoldUnfoldOperations fromValue(String v) {
        return valueOf(v);
    }

}