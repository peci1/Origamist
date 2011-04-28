//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.28 at 02:39:12 odp. CEST 
//


package cz.cuni.mff.peckam.java.origamist.files.jaxb;

import java.beans.PropertyChangeListener;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.utils.CustomPropertyChangeSupport;
import cz.cuni.mff.peckam.java.origamist.utils.HasBoundProperties;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: files
 * <p>Provided property: categories
 * <p>Java class for Listing complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Listing">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="files" type="{http://www.mff.cuni.cz/~peckam/java/origamist/files/v1}Files" minOccurs="0"/>
 *         &lt;element name="categories" type="{http://www.mff.cuni.cz/~peckam/java/origamist/files/v1}Categories" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Listing", propOrder = {
    "files",
    "categories"
})
public class Listing implements HasBoundProperties, Equals, HashCode
{

    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.files.Files.class)
    protected cz.cuni.mff.peckam.java.origamist.files.Files files;
    /**
     * Property files
     * 
     */
    public final static String FILES_PROPERTY = "files";
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.files.Categories.class)
    protected cz.cuni.mff.peckam.java.origamist.files.Categories categories;
    /**
     * Property categories
     * 
     */
    public final static String CATEGORIES_PROPERTY = "categories";
    protected transient CustomPropertyChangeSupport support = (new CustomPropertyChangeSupport(this));

    /**
     * Gets the value of the files property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Files }
     *     
     */
    public cz.cuni.mff.peckam.java.origamist.files.Files getFiles() {
        return files;
    }

    /**
     * Sets the value of the files property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Files }
     *     
     */
    public void setFiles(cz.cuni.mff.peckam.java.origamist.files.Files value) {
        cz.cuni.mff.peckam.java.origamist.files.Files old = this.files;
        this.files = ((cz.cuni.mff.peckam.java.origamist.files.Files) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Listing.FILES_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the categories property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Categories }
     *     
     */
    public cz.cuni.mff.peckam.java.origamist.files.Categories getCategories() {
        return categories;
    }

    /**
     * Sets the value of the categories property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.files.jaxb.Categories }
     *     
     */
    public void setCategories(cz.cuni.mff.peckam.java.origamist.files.Categories value) {
        cz.cuni.mff.peckam.java.origamist.files.Categories old = this.categories;
        this.categories = ((cz.cuni.mff.peckam.java.origamist.files.Categories) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Listing.CATEGORIES_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Listing)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Listing that = ((Listing) object);
        {
            cz.cuni.mff.peckam.java.origamist.files.Files lhsFiles;
            lhsFiles = this.getFiles();
            cz.cuni.mff.peckam.java.origamist.files.Files rhsFiles;
            rhsFiles = that.getFiles();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "files", lhsFiles), LocatorUtils.property(thatLocator, "files", rhsFiles), lhsFiles, rhsFiles)) {
                return false;
            }
        }
        {
            cz.cuni.mff.peckam.java.origamist.files.Categories lhsCategories;
            lhsCategories = this.getCategories();
            cz.cuni.mff.peckam.java.origamist.files.Categories rhsCategories;
            rhsCategories = that.getCategories();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "categories", lhsCategories), LocatorUtils.property(thatLocator, "categories", rhsCategories), lhsCategories, rhsCategories)) {
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
            cz.cuni.mff.peckam.java.origamist.files.Files theFiles;
            theFiles = this.getFiles();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "files", theFiles), currentHashCode, theFiles);
        }
        {
            cz.cuni.mff.peckam.java.origamist.files.Categories theCategories;
            theCategories = this.getCategories();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "categories", theCategories), currentHashCode, theCategories);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public List<String> removeAllListeners(PropertyChangeListener param0) {
        return this.support.removeAllListeners(param0);
    }

    public void addPropertyChangeListener(PropertyChangeListener param0) {
        this.support.addPropertyChangeListener(param0);
    }

    public void addPropertyChangeListener(String param0, PropertyChangeListener param1) {
        this.support.addPropertyChangeListener(param0, param1);
    }

    public void removePropertyChangeListener(String param0, PropertyChangeListener param1) {
        this.support.removePropertyChangeListener(param0, param1);
    }

    public void removePropertyChangeListener(PropertyChangeListener param0) {
        this.support.removePropertyChangeListener(param0);
    }

}
