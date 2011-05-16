//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.16 at 11:30:35 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

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
 * <p>Provided property: paper
 * <p>Provided property: model
 * <p>Java class for Origami complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Origami">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="author" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}Author"/>
 *         &lt;element name="name" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}gYear"/>
 *         &lt;element name="shortdesc" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="description" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="license" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}License"/>
 *         &lt;element name="original" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="thumbnail">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}Image">
 *                 &lt;attribute name="generated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="paper" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}DiagramPaper"/>
 *         &lt;element name="model" type="{http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2}Model"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
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
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Origami
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.common.Author author;
    /**
     * Property author
     * 
     */
    public final static String AUTHOR_PROPERTY = "author:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected XMLGregorianCalendar year;
    /**
     * Property year
     * 
     */
    public final static String YEAR_PROPERTY = "year:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected cz.cuni.mff.peckam.java.origamist.common.License license;
    /**
     * Property license
     * 
     */
    public final static String LICENSE_PROPERTY = "license:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected URI original;
    /**
     * Property original
     * 
     */
    public final static String ORIGINAL_PROPERTY = "original:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected Origami.Thumbnail thumbnail;
    /**
     * Property thumbnail
     * 
     */
    public final static String THUMBNAIL_PROPERTY = "thumbnail:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected cz.cuni.mff.peckam.java.origamist.model.DiagramPaper paper;
    /**
     * Property paper
     * 
     */
    public final static String PAPER_PROPERTY = "paper:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected cz.cuni.mff.peckam.java.origamist.model.Model model;
    /**
     * Property model
     * 
     */
    public final static String MODEL_PROPERTY = "model:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    /**
     * Property name
     * 
     */
    public final static String NAME_PROPERTY = "name:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    /**
     * Property shortdesc
     * 
     */
    public final static String SHORTDESC_PROPERTY = "shortdesc:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    /**
     * Property description
     * 
     */
    public final static String DESCRIPTION_PROPERTY = "description:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami";
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> name = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> shortdesc = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> description = new cz.cuni.mff.peckam.java.origamist.utils.ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();

    public Origami() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.Origami")) {
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
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.Author.class)
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
            support.firePropertyChange(Origami.AUTHOR_PROPERTY, old, value);
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
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
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
    @XmlElement(required = true)
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
            support.firePropertyChange(Origami.YEAR_PROPERTY, old, value);
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
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
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
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
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
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.License.class)
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
            support.firePropertyChange(Origami.LICENSE_PROPERTY, old, value);
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
            support.firePropertyChange(Origami.ORIGINAL_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the thumbnail property.
     * 
     * @return
     *     possible object is
     *     {@link Origami.Thumbnail }
     *     
     */
    @XmlElement(required = true)
    public Origami.Thumbnail getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the value of the thumbnail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Origami.Thumbnail }
     *     
     */
    public void setThumbnail(Origami.Thumbnail value) {
        Origami.Thumbnail old = this.thumbnail;
        this.thumbnail = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Origami.THUMBNAIL_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the paper property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.DiagramPaper.class)
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
        cz.cuni.mff.peckam.java.origamist.model.DiagramPaper old = this.paper;
        this.paper = ((cz.cuni.mff.peckam.java.origamist.model.DiagramPaper) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Origami.PAPER_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Model }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.model.Model.class)
    public cz.cuni.mff.peckam.java.origamist.model.Model getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Model }
     *     
     */
    public void setModel(cz.cuni.mff.peckam.java.origamist.model.Model value) {
        cz.cuni.mff.peckam.java.origamist.model.Model old = this.model;
        this.model = ((cz.cuni.mff.peckam.java.origamist.model.Model) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Origami.MODEL_PROPERTY, old, value);
        }
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getName(), getShortdesc(), getDescription()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {NAME_PROPERTY, SHORTDESC_PROPERTY, DESCRIPTION_PROPERTY };
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
            Origami.Thumbnail lhsThumbnail;
            lhsThumbnail = this.getThumbnail();
            Origami.Thumbnail rhsThumbnail;
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
            cz.cuni.mff.peckam.java.origamist.model.Model lhsModel;
            lhsModel = this.getModel();
            cz.cuni.mff.peckam.java.origamist.model.Model rhsModel;
            rhsModel = that.getModel();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "model", lhsModel), LocatorUtils.property(thatLocator, "model", rhsModel), lhsModel, rhsModel)) {
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
            Origami.Thumbnail theThumbnail;
            theThumbnail = this.getThumbnail();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "thumbnail", theThumbnail), currentHashCode, theThumbnail);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.DiagramPaper thePaper;
            thePaper = this.getPaper();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "paper", thePaper), currentHashCode, thePaper);
        }
        {
            cz.cuni.mff.peckam.java.origamist.model.Model theModel;
            theModel = this.getModel();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "model", theModel), currentHashCode, theModel);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }


    /**
     * <p>Provided property: generated
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}Image">
     *       &lt;attribute name="generated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlType(name = "")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    public static class Thumbnail
        extends Image
        implements Equals, HashCode
    {

        protected Boolean generated;
        /**
         * Property generated
         * 
         */
        public final static String GENERATED_PROPERTY = "generated:cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami.Thumbnail";

        public Thumbnail() {
            if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami.Thumbnail")) {
                init();
            }
        }

        /**
         * Gets the value of the generated property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isGenerated() {
            if (generated == null) {
                return true;
            } else {
                return generated;
            }
        }

        /**
         * Sets the value of the generated property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        @XmlAttribute
        public void setGenerated(Boolean value) {
            Boolean old = this.generated;
            this.generated = value;
            if (old!= value) {
                support.firePropertyChange(Origami.Thumbnail.GENERATED_PROPERTY, old, value);
            }
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof Origami.Thumbnail)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            if (!super.equals(thisLocator, thatLocator, object, strategy)) {
                return false;
            }
            final Origami.Thumbnail that = ((Origami.Thumbnail) object);
            {
                boolean lhsGenerated;
                lhsGenerated = this.isGenerated();
                boolean rhsGenerated;
                rhsGenerated = that.isGenerated();
                if (!strategy.equals(LocatorUtils.property(thisLocator, "generated", lhsGenerated), LocatorUtils.property(thatLocator, "generated", rhsGenerated), lhsGenerated, rhsGenerated)) {
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
            int currentHashCode = super.hashCode(locator, strategy);
            {
                boolean theGenerated;
                theGenerated = this.isGenerated();
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "generated", theGenerated), currentHashCode, theGenerated);
            }
            return currentHashCode;
        }

        public int hashCode() {
            final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
            return this.hashCode(null, strategy);
        }

    }

}
