//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.27 at 06:31:40 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.common.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.peckam.java.origamist.common.GeneratedClassBase;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Provided property: image
 * <p>Java class for Image complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Image">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="image" type="{http://www.mff.cuni.cz/~peckam/java/origamist/common/v1}BinaryImage"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name = "Image", propOrder = {
    "image"
})
@XmlSeeAlso({
    Thumbnail.class
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Image
    extends GeneratedClassBase
    implements Equals, HashCode
{

    protected cz.cuni.mff.peckam.java.origamist.common.BinaryImage image;
    /**
     * Property image
     * 
     */
    public final static String IMAGE_PROPERTY = "image:cz.cuni.mff.peckam.java.origamist.common.jaxb.Image";

    public Image() {
        if (getClass().getName().equals("cz.cuni.mff.peckam.java.origamist.common.jaxb.Image")) {
            init();
        }
    }

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage }
     *     
     */
    @XmlElement(required = true, type = cz.cuni.mff.peckam.java.origamist.common.BinaryImage.class)
    public cz.cuni.mff.peckam.java.origamist.common.BinaryImage getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage }
     *     
     */
    public void setImage(cz.cuni.mff.peckam.java.origamist.common.BinaryImage value) {
        cz.cuni.mff.peckam.java.origamist.common.BinaryImage old = this.image;
        this.image = ((cz.cuni.mff.peckam.java.origamist.common.BinaryImage) value);
        if (((old!= value)&&((old == null)||(value == null)))||((old!= null)&&(!old.equals(value)))) {
            support.firePropertyChange(Image.IMAGE_PROPERTY, old, value);
        }
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Image)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Image that = ((Image) object);
        {
            cz.cuni.mff.peckam.java.origamist.common.BinaryImage lhsImage;
            lhsImage = this.getImage();
            cz.cuni.mff.peckam.java.origamist.common.BinaryImage rhsImage;
            rhsImage = that.getImage();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "image", lhsImage), LocatorUtils.property(thatLocator, "image", rhsImage), lhsImage, rhsImage)) {
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
            cz.cuni.mff.peckam.java.origamist.common.BinaryImage theImage;
            theImage = this.getImage();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "image", theImage), currentHashCode, theImage);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
