//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.20 at 04:49:28 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.common.jaxb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cz.cuni.mff.peckam.java.origamist.common.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cz.cuni.mff.peckam.java.origamist.common.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage createBinaryImage() {
        return new cz.cuni.mff.peckam.java.origamist.common.BinaryImage();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.License }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.common.jaxb.License createLicense() {
        return new cz.cuni.mff.peckam.java.origamist.common.License();
    }

    /**
     * Create an instance of {@link LangString }
     * 
     */
    public LangString createLangString() {
        return new LangString();
    }

    /**
     * Create an instance of {@link Author }
     * 
     */
    public Author createAuthor() {
        return new Author();
    }

    /**
     * Create an instance of {@link Image }
     * 
     */
    public Image createImage() {
        return new Image();
    }

}
