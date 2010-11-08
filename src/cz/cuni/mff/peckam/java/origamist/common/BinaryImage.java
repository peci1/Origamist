/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * This class represents an image.
 * 
 * @author Martin Pecka
 */
public class BinaryImage extends cz.cuni.mff.peckam.java.origamist.common.jaxb.BinaryImage
{
    /**
     * Return the content as an ImageIcon.
     * 
     * @return the content as an ImageIcon.
     */
    public ImageIcon getImageIcon()
    {
        try {
            return new ImageIcon(new BASE64Decoder().decodeBuffer(new ByteArrayInputStream(value)));
        } catch (IOException e) {
            Logger.getLogger("application").warn("IO error while loading a thumbnail of origami.", e);
            return null;
        }
    }

    /**
     * Set the image from the given ImageIcon
     * 
     * @param icon The image to set
     */
    public void setImageIcon(ImageIcon icon)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) icon.getImage(), type.replaceAll("[^/]*/", ""), os);
            new BASE64Encoder().encode(os.toByteArray(), os2);
            value = os2.toByteArray();
        } catch (IOException e) {
            Logger.getLogger("application").warn("IO error while setting a thumbnail of origami.", e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {}
            try {
                os2.close();
            } catch (IOException e) {}
        }
    }
}
