//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.20 at 05:08:25 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.files.jaxb;

import java.net.URI;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import cz.cuni.mff.peckam.java.origamist.common.GeneratedClassBase;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Image;
import cz.cuni.mff.peckam.java.origamist.utils.URIAdapter;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: author
 * <p>Provided property: year
 * <p>Provided property: license
 * <p>Provided property: original
 * <p>Provided property: thumbnail
 * <p>Provided property: src
 * <p>Java class for File complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="File">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="author" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}Author" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}gYear" minOccurs="0"/>
 *         &lt;element name="shortdesc" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="license" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}License" minOccurs="0"/>
 *         &lt;element name="original" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="thumbnail" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}Image" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "File", propOrder = {
    "author",
    "name",
    "year",
    "shortdesc",
    "license",
    "original",
    "thumbnail"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class File
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.common.Author author;
    /**
     * Property author
     * 
     */
    public final static String AUTHOR_PROPERTY = "author:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    protected XMLGregorianCalendar year;
    /**
     * Property year
     * 
     */
    public final static String YEAR_PROPERTY = "year:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    protected cz.cuni.mff.peckam.java.origamist.common.License license;
    /**
     * Property license
     * 
     */
    public final static String LICENSE_PROPERTY = "license:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    protected URI original;
    /**
     * Property original
     * 
     */
    public final static String ORIGINAL_PROPERTY = "original:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    protected Image thumbnail;
    /**
     * Property thumbnail
     * 
     */
    public final static String THUMBNAIL_PROPERTY = "thumbnail:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    protected URI src;
    /**
     * Property src
     * 
     */
    public final static String SRC_PROPERTY = "src:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    /**
     * Property name
     * 
     */
    public final static String NAME_PROPERTY = "name:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    /**
     * Property shortdesc
     * 
     */
    public final static String SHORTDESC_PROPERTY = "shortdesc:cz.cuni.mff.peckam.java.origamist.files.jaxb.File";
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> name = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> shortdesc = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();

    public File() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.files.File")) {
            init();
        }
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.Author }
     *     
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.Author.class)
    public cz.cuni.mff.peckam.java.origamist.common.Author getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.Author }
     *     
     */
    public void setAuthor(cz.cuni.mff.peckam.java.origamist.common.Author value) {
        cz.cuni.mff.peckam.java.origamist.common.Author old = this.author;
        this.author = ((cz.cuni.mff.peckam.java.origamist.common.Author) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(File.AUTHOR_PROPERTY, old, value);
        }
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
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getName() {
        if (name == null) {
            name = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
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
        XMLGregorianCalendar old = this.year;
        this.year = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(File.YEAR_PROPERTY, old, value);
        }
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
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getShortdesc() {
        if (shortdesc == null) {
            shortdesc = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
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
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.common.License.class)
    public cz.cuni.mff.peckam.java.origamist.common.License getLicense() {
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
    public void setLicense(cz.cuni.mff.peckam.java.origamist.common.License value) {
        cz.cuni.mff.peckam.java.origamist.common.License old = this.license;
        this.license = ((cz.cuni.mff.peckam.java.origamist.common.License) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(File.LICENSE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the original property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(URIAdapter.class)
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
        URI old = this.original;
        this.original = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(File.ORIGINAL_PROPERTY, old, value);
        }
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
        Image old = this.thumbnail;
        this.thumbnail = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(File.THUMBNAIL_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(URIAdapter.class)
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
        URI old = this.src;
        this.src = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(File.SRC_PROPERTY, old, value);
        }
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getName(), getShortdesc()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {NAME_PROPERTY, SHORTDESC_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof File)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final File that = ((File) object);
        {
            cz.cuni.mff.peckam.java.origamist.common.Author lhsAuthor;
            lhsAuthor = this.getAuthor();
            cz.cuni.mff.peckam.java.origamist.common.Author rhsAuthor;
            rhsAuthor = that.getAuthor();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "author", lhsAuthor), LocatorUtils.property(thatLocator, "author", rhsAuthor), lhsAuthor, rhsAuthor)) {
                return false;
            }
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> lhsName;
            lhsName = this.getName();
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> rhsName;
            rhsName = that.getName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName), LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsYear;
            lhsYear = this.getYear();
            XMLGregorianCalendar rhsYear;
            rhsYear = that.getYear();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "year", lhsYear), LocatorUtils.property(thatLocator, "year", rhsYear), lhsYear, rhsYear)) {
                return false;
            }
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> lhsShortdesc;
            lhsShortdesc = this.getShortdesc();
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> rhsShortdesc;
            rhsShortdesc = that.getShortdesc();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "shortdesc", lhsShortdesc), LocatorUtils.property(thatLocator, "shortdesc", rhsShortdesc), lhsShortdesc, rhsShortdesc)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.common.License lhsLicense;
            lhsLicense = this.getLicense();
            cz.cuni.mff.peckam.java.origamist.common.License rhsLicense;
            rhsLicense = that.getLicense();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "license", lhsLicense), LocatorUtils.property(thatLocator, "license", rhsLicense), lhsLicense, rhsLicense)) {
                return false;
            }
        }
        {
            URI lhsOriginal;
            lhsOriginal = this.getOriginal();
            URI rhsOriginal;
            rhsOriginal = that.getOriginal();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "original", lhsOriginal), LocatorUtils.property(thatLocator, "original", rhsOriginal), lhsOriginal, rhsOriginal)) {
                return false;
            }
        }
        {
            Image lhsThumbnail;
            lhsThumbnail = this.getThumbnail();
            Image rhsThumbnail;
            rhsThumbnail = that.getThumbnail();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "thumbnail", lhsThumbnail), LocatorUtils.property(thatLocator, "thumbnail", rhsThumbnail), lhsThumbnail, rhsThumbnail)) {
                return false;
            }
        }
        {
            URI lhsSrc;
            lhsSrc = this.getSrc();
            URI rhsSrc;
            rhsSrc = that.getSrc();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "src", lhsSrc), LocatorUtils.property(thatLocator, "src", rhsSrc), lhsSrc, rhsSrc)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            cz.cuni.mff.peckam.java.origamist.common.Author theAuthor;
            theAuthor = this.getAuthor();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "author", theAuthor), currentHashCode, theAuthor);
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> theName;
            theName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
        {
            XMLGregorianCalendar theYear;
            theYear = this.getYear();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "year", theYear), currentHashCode, theYear);
        }
        {
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> theShortdesc;
            theShortdesc = this.getShortdesc();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shortdesc", theShortdesc), currentHashCode, theShortdesc);
        }
        {
            cz.cuni.mff.peckam.java.origamist.common.License theLicense;
            theLicense = this.getLicense();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "license", theLicense), currentHashCode, theLicense);
        }
        {
            URI theOriginal;
            theOriginal = this.getOriginal();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "original", theOriginal), currentHashCode, theOriginal);
        }
        {
            Image theThumbnail;
            theThumbnail = this.getThumbnail();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "thumbnail", theThumbnail), currentHashCode, theThumbnail);
        }
        {
            URI theSrc;
            theSrc = this.getSrc();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "src", theSrc), currentHashCode, theSrc);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
