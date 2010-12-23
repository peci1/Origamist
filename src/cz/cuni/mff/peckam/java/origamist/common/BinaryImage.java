/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import sun.awt.image.ToolkitImage;

/**
 * This class represents an image.
 * 
 * @author Martin Pecka
 */
public class BinaryImage extends cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage
{
    /** The image this class holds. */
    @XmlTransient
    protected ImageIcon icon = null;

    /**
     * Return the content as an ImageIcon.
     * 
     * @return the content as an ImageIcon.
     */
    public ImageIcon getImageIcon()
    {
        if (icon == null)
            icon = new ImageIcon(value);
        return icon;
    }

    /**
     * Set the image from the given ImageIcon
     * 
     * @param icon The image to set
     */
    public void setImageIcon(ImageIcon icon)
    {
        if (icon == null) {
            value = null;
            this.icon = null;
            return;
        }

        RenderedImage image = null;
        if (icon.getImage() instanceof RenderedImage) {
            image = (RenderedImage) icon.getImage();
        } else if (icon.getImage() instanceof ToolkitImage) {
            image = ((ToolkitImage) icon.getImage()).getBufferedImage();
        }

        if (image == null) {
            value = null;
            this.icon = null;
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type.replaceAll("[^/]*/", ""), os);
            value = os.toByteArray();
            this.icon = icon;
        } catch (IOException e) {
            Logger.getLogger("application").warn("IO error while setting a thumbnail of origami.", e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {}
        }
    }
}
