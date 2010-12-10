//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.10 at 08:01:48 odp. CET 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

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
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Java class for Origami complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Origami">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="author" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}Author"/>
 *         &lt;element name="name" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}gYear"/>
 *         &lt;element name="shortdesc" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="description" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="license" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}License"/>
 *         &lt;element name="original" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="thumbnail" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}Image"/>
 *         &lt;element name="paper" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}DiagramPaper"/>
 *         &lt;element name="model" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/}Model"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Origami", propOrder = {
    "author",
    "name",
    "year",
    "shortdesc",
    "description",
    "license",
    "original",
    "thumbnail",
    "paper",
    "model"
})
public class Origami
    implements Equals, HashCode
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "gYear")
    protected XMLGregorianCalendar year;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(URIAdapter.class)
    @XmlSchemaType(name = "anyURI")
    protected URI original;
    @XmlElement(required = true)
    protected Image thumbnail;
    @XmlElement(required = true)
    protected Model model;
    @XmlAttribute(name = "version", required = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "positiveInteger")
    protected Integer version;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.Author.class)
    protected cz.cuni.mff.peckam.java.origamist.common.Author author;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> name;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> shortdesc;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> description;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.License.class)
    protected cz.cuni.mff.peckam.java.origamist.common.License license;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.DiagramPaper.class)
    protected cz.cuni.mff.peckam.java.origamist.model.DiagramPaper paper;

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.Author }
     *     
     */
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
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
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
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getShortdesc() {
        if (shortdesc == null) {
            shortdesc = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
        }
        return this.shortdesc;
    }

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>Objects of the following type(s) are allowed in the list: {@link cz.cuni.mff.peckam.java.origamist.common.LangString }
     * 
     */
    public List<cz.cuni.mff.peckam.java.origamist.common.LangString> getDescription() {
        if (description == null) {
            description = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
        }
        return this.description;
    }

    /**
     * Gets the value of the license property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.License }
     *     
     */
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
        this.license = value;
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
     * Gets the value of the paper property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper }
     *     
     */
    public cz.cuni.mff.peckam.java.origamist.model.DiagramPaper getPaper() {
        return paper;
    }

    /**
     * Sets the value of the paper property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper }
     *     
     */
    public void setPaper(cz.cuni.mff.peckam.java.origamist.model.DiagramPaper value) {
        this.paper = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link Model }
     *     
     */
    public Model getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model }
     *     
     */
    public void setModel(Model value) {
        this.model = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(Integer value) {
        this.version = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Origami)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Origami that = ((Origami) object);
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
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> lhsDescription;
            lhsDescription = this.getDescription();
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
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
            cz.cuni.mff.peckam.java.origamist.model.DiagramPaper lhsPaper;
            lhsPaper = this.getPaper();
            cz.cuni.mff.peckam.java.origamist.model.DiagramPaper rhsPaper;
            rhsPaper = that.getPaper();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "paper", lhsPaper), LocatorUtils.property(thatLocator, "paper", rhsPaper), lhsPaper, rhsPaper)) {
                return false;
            }
        }
        {
            Model lhsModel;
            lhsModel = this.getModel();
            Model rhsModel;
            rhsModel = that.getModel();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "model", lhsModel), LocatorUtils.property(thatLocator, "model", rhsModel), lhsModel, rhsModel)) {
                return false;
            }
        }
        {
            Integer lhsVersion;
            lhsVersion = this.getVersion();
            Integer rhsVersion;
            rhsVersion = that.getVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "version", lhsVersion), LocatorUtils.property(thatLocator, "version", rhsVersion), lhsVersion, rhsVersion)) {
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
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
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
            cz.cuni.mff.peckam.java.origamist.model.DiagramPaper thePaper;
            thePaper = this.getPaper();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "paper", thePaper), currentHashCode, thePaper);
        }
        {
            Model theModel;
            theModel = this.getModel();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "model", theModel), currentHashCode, theModel);
        }
        {
            Integer theVersion;
            theVersion = this.getVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "version", theVersion), currentHashCode, theVersion);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
