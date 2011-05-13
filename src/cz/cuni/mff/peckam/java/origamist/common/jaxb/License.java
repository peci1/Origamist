//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.13 at 02:43:54 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.common.jaxb;

import java.net.URI;
import java.security.Permission;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import cz.cuni.mff.peckam.java.origamist.common.GeneratedClassBase;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
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
 * <p>Provided property: name
 * <p>Provided property: homepage
 * <p>Provided property: content
 * <p>Java class for License complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="License">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice>
 *           &lt;element name="homepage" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *           &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element name="permission" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}Permission" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "License", propOrder = {
    "name",
    "homepage",
    "content",
    "permission"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class License
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected String name;
    /**
     * Property name
     * 
     */
    public final static String NAME_PROPERTY = "name:cz.cuni.mff.peckam.java.origamist.common.jaxb.License";
    protected URI homepage;
    /**
     * Property homepage
     * 
     */
    public final static String HOMEPAGE_PROPERTY = "homepage:cz.cuni.mff.peckam.java.origamist.common.jaxb.License";
    protected String content;
    /**
     * Property content
     * 
     */
    public final static String CONTENT_PROPERTY = "content:cz.cuni.mff.peckam.java.origamist.common.jaxb.License";
    protected List<Permission> permission = new ObservableList<Permission>();
    /**
     * Property permission
     * 
     */
    public final static String PERMISSION_PROPERTY = "permission:cz.cuni.mff.peckam.java.origamist.common.jaxb.License";

    public License() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.common.License")) {
            init();
        }
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        String old = this.name;
        this.name = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(License.NAME_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the homepage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(URIAdapter.class)
    public URI getHomepage() {
        return homepage;
    }

    /**
     * Sets the value of the homepage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomepage(URI value) {
        URI old = this.homepage;
        this.homepage = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(License.HOMEPAGE_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        String old = this.content;
        this.content = value;
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(License.CONTENT_PROPERTY, old, value);
        }
    }

    /**
     * Gets the value of the permission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the permission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPermission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    public List<Permission> getPermission() {
        if (permission == null) {
            permission = new ObservableList<Permission>();
        }
        return this.permission;
    }

    /**
     * 
     * @return
     *     Return an array of all list fields defined on this class and its superclasses.
     */
    public List<?> [] getListFields() {
        return new List<?> [] {getPermission()};
    }

    /**
     * 
     * @return
     *     Return an array of property names of all list fields defined on this class and its superclasses. The order of the property names corresponds with the order of getListFields().
     */
    public String[] getListProperties() {
        return new String[] {PERMISSION_PROPERTY };
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof License)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final License that = ((License) object);
        {
            String lhsName;
            lhsName = this.getName();
            String rhsName;
            rhsName = that.getName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName), LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        {
            URI lhsHomepage;
            lhsHomepage = this.getHomepage();
            URI rhsHomepage;
            rhsHomepage = that.getHomepage();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "homepage", lhsHomepage), LocatorUtils.property(thatLocator, "homepage", rhsHomepage), lhsHomepage, rhsHomepage)) {
                return false;
            }
        }
        {
            String lhsContent;
            lhsContent = this.getContent();
            String rhsContent;
            rhsContent = that.getContent();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "content", lhsContent), LocatorUtils.property(thatLocator, "content", rhsContent), lhsContent, rhsContent)) {
                return false;
            }
        }
        {
            List<Permission> lhsPermission;
            lhsPermission = this.getPermission();
            List<Permission> rhsPermission;
            rhsPermission = that.getPermission();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permission", lhsPermission), LocatorUtils.property(thatLocator, "permission", rhsPermission), lhsPermission, rhsPermission)) {
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
            String theName;
            theName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
        {
            URI theHomepage;
            theHomepage = this.getHomepage();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "homepage", theHomepage), currentHashCode, theHomepage);
        }
        {
            String theContent;
            theContent = this.getContent();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "content", theContent), currentHashCode, theContent);
        }
        {
            List<Permission> thePermission;
            thePermission = this.getPermission();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permission", thePermission), currentHashCode, thePermission);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
