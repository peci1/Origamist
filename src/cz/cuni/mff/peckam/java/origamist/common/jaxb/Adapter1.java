//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.20 at 06:43:51 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.common.jaxb;

import java.security.Permission;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Permission>
{


    public Permission unmarshal(String value) {
        return (cz.cuni.mff.peckam.java.origamist.utils.PermissionConverter.parse(value));
    }

    public String marshal(Permission value) {
        return (cz.cuni.mff.peckam.java.origamist.utils.PermissionConverter.print(value));
    }

}
