//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.22 at 04:43:03 odp. CEST 
//


package org.w3._2001.xmlschema;

import java.net.URI;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, URI>
{


    public URI unmarshal(String value) {
        return (cz.cuni.mff.peckam.java.origamist.utils.URIConverter.parse(value));
    }

    public String marshal(URI value) {
        return (cz.cuni.mff.peckam.java.origamist.utils.URIConverter.print(value));
    }

}