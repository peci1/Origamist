//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.10 at 08:01:47 odp. CET 
//


package cz.cuni.mff.peckam.java.origamist.files.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for Category complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Category">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/}LangString" maxOccurs="unbounded"/>
 *         &lt;element name="files" type="{http://www.mff.cuni.cz/~peckam/java/origamist/files/}Files" minOccurs="0"/>
 *         &lt;element name="categories" type="{http://www.mff.cuni.cz/~peckam/java/origamist/files/}Categories" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Category", propOrder = {
    "name",
    "files",
    "categories"
})
public class Category
    implements Equals, HashCode
{

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.LangString.class)
    protected List<cz.cuni.mff.peckam.java.origamist.common.LangString> name;
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.files.Files.class)
    protected cz.cuni.mff.peckam.java.origamist.files.Files files;
    @XmlElement(type = cz.cuni.mff.peckam.java.origamist.files.Categories.class)
    protected cz.cuni.mff.peckam.java.origamist.files.Categories categories;

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
            name = new ObservableList<cz.cuni.mff.peckam.java.origamist.common.LangString>();
        }
        return this.name;
    }

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
        this.files = value;
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
        this.categories = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Category)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Category that = ((Category) object);
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
        {
            String lhsId;
            lhsId = this.getId();
            String rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
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
            List<cz.cuni.mff.peckam.java.origamist.common.LangString> theName;
            theName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
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
        {
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
