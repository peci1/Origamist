//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.31 at 09:46:56 odp. CET 
//


package cz.cuni.mff.peckam.java.origamist.files.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cz.cuni.mff.peckam.java.origamist.files.jaxb package. 
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

    private final static QName _Listing_QNAME = new QName("http://www.mff.cuni.cz/~peckam/java/origamist/files/", "listing");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cz.cuni.mff.peckam.java.origamist.files.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Files }
     * 
     */
    public Files createFiles() {
        return new Files();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing createListing() {
        return new cz.cuni.mff.peckam.java.origamist.files.Listing();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.File }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.files.jaxb.File createFile() {
        return new cz.cuni.mff.peckam.java.origamist.files.File();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Category }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.files.jaxb.Category createCategory() {
        return new cz.cuni.mff.peckam.java.origamist.files.Category();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Categories }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.files.jaxb.Categories createCategories() {
        return new cz.cuni.mff.peckam.java.origamist.files.Categories();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.mff.cuni.cz/~peckam/java/origamist/files/", name = "listing")
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public JAXBElement<cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing> createListing(cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing value) {
        return new JAXBElement<cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing>(_Listing_QNAME, ((Class) cz.cuni.mff.peckam.java.origamist.files.Listing.class), null, ((cz.cuni.mff.peckam.java.origamist.files.Listing) value));
    }

}
