//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.27 at 03:59:33 dop. CEST 
//


package org.w3._2001.xmlschema;

import java.util.Locale;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter3
    extends XmlAdapter<String, Locale>
{


    public Locale unmarshal(String value) {
        return (cz.cuni.mff.peckam.java.origamist.utils.LocaleConverter.parse(value));
    }

    public String marshal(Locale value) {
        return (cz.cuni.mff.peckam.java.origamist.utils.LocaleConverter.print(value));
    }

}
