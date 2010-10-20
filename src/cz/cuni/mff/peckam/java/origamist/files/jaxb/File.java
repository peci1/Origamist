//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.20 at 04:49:28 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.files.jaxb;

import java.net.URI;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Author;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Image;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Java class for File complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="File">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="author" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}Author" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}gYear" minOccurs="0"/>
 *         &lt;element name="shortdesc" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="license" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}License" minOccurs="0"/>
 *         &lt;element name="original" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="thumbnail" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}Image" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "File", propOrder = {
    "author",
    "name",
    "year",
    "shortdesc",
    "license",
    "original",
    "thumbnail"
})
public class File {

    protected Author author;
    protected List<LangString> name = new cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList<LangString>();
    @XmlSchemaType(name = "gYear")
    protected XMLGregorianCalendar year;
    protected List<LangString> shortdesc = new cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList<LangString>();
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.License.class)
    protected cz.cuni.mff.peckam.java.origamist.common.License license;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "anyURI")
    protected URI original;
    protected Image thumbnail;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "anyURI")
    protected URI src;

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link Author }
     *     
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link Author }
     *     
     */
    public void setAuthor(Author value) {
        this.author = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the name property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangString }
     * 
     * 
     */
    public List<LangString> getName() {
        if (name == null) {
            name = new cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList<LangString>();
        }
        return this.name;
    }

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setYear(XMLGregorianCalendar value) {
        this.year = value;
    }

    /**
     * Gets the value of the shortdesc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shortdesc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShortdesc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangString }
     * 
     * 
     */
    public List<LangString> getShortdesc() {
        if (shortdesc == null) {
            shortdesc = new cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList<LangString>();
        }
        return this.shortdesc;
    }

    /**
     * Gets the value of the license property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.License }
     *     
     */
    public cz.cuni.mff.peckam.java.origamist.common.jaxb.License getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.License }
     *     
     */
    public void setLicense(cz.cuni.mff.peckam.java.origamist.common.jaxb.License value) {
        this.license = ((cz.cuni.mff.peckam.java.origamist.common.License) value);
    }

    /**
     * Gets the value of the original property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public URI getOriginal() {
        return original;
    }

    /**
     * Sets the value of the original property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginal(URI value) {
        this.original = value;
    }

    /**
     * Gets the value of the thumbnail property.
     * 
     * @return
     *     possible object is
     *     {@link Image }
     *     
     */
    public Image getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the value of the thumbnail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Image }
     *     
     */
    public void setThumbnail(Image value) {
        this.thumbnail = value;
    }

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public URI getSrc() {
        return src;
    }

    /**
     * Sets the value of the src property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrc(URI value) {
        this.src = value;
    }

}
