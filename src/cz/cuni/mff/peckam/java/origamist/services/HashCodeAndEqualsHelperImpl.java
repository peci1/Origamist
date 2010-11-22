/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;

import cz.cuni.mff.peckam.java.origamist.services.interfaces.HashCodeAndEqualsHelper;

/**
 * This class should aid in creating hashCode() and equals() methods.
 * 
 * Inspired by http://forums.sun.com/thread.jspa?forumID=20&threadID=5345358
 * 
 * @author Martin Pecka
 */
public class HashCodeAndEqualsHelperImpl extends Service implements HashCodeAndEqualsHelper
{

    @Override
    public int hashCode(ImageIcon icon)
    {
        int prime = 37;
        if (icon.getImage() instanceof BufferedImage) {
            final int newW = 100;
            final int newH = 100;

            BufferedImage bi = (BufferedImage) icon.getImage();
            final int w = bi.getWidth();
            final int h = bi.getHeight();
            final BufferedImage scaledImg = new BufferedImage(newW, newH, bi.getType());
            final Graphics2D g = scaledImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(bi, 0, 0, newW, newH, 0, 0, w, h, null);
            g.dispose();

            int[] rgbs = new int[newW * newH];
            scaledImg.getRGB(0, 0, newW, newH, rgbs, 0, newW);

            int hash = Arrays.hashCode(rgbs);
            hash = hash * prime + w;
            hash = hash * prime + h;

            return hash;
        } else {
            return prime * icon.getIconWidth() + icon.getIconWidth();
        }
    }

    @Override
    public boolean equals(ImageIcon icon1, ImageIcon icon2)
    {
        int w1 = icon1.getIconWidth();
        int h1 = icon1.getIconHeight();

        int w2 = icon2.getIconWidth();
        int h2 = icon2.getIconHeight();

        if (icon1.getImage() instanceof BufferedImage) {
            if (icon2.getImage() instanceof BufferedImage) {
                final int newW = 100;
                final int newH = 100;

                BufferedImage bi1 = (BufferedImage) icon1.getImage();
                final BufferedImage scaledImg1 = new BufferedImage(newW, newH, bi1.getType());
                final Graphics2D g1 = scaledImg1.createGraphics();
                g1.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g1.drawImage(bi1, 0, 0, newW, newH, 0, 0, w1, h1, null);
                g1.dispose();

                int[] rgbs1 = new int[newW * newH];
                scaledImg1.getRGB(0, 0, newW, newH, rgbs1, 0, newW);

                BufferedImage bi2 = (BufferedImage) icon2.getImage();
                final BufferedImage scaledImg2 = new BufferedImage(newW, newH, bi2.getType());
                final Graphics2D g2 = scaledImg2.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(bi2, 0, 0, newW, newH, 0, 0, w2, h2, null);
                g2.dispose();

                int[] rgbs2 = new int[newW * newH];
                scaledImg1.getRGB(0, 0, newW, newH, rgbs2, 0, newW);

                for (int i = 0; i < rgbs1.length; i++) {
                    if (rgbs1[i] != rgbs2[i])
                        return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            if (icon2.getImage() instanceof BufferedImage) {
                return false;
            } else {
                return w1 == w2 && h1 == h2;
            }
        }
    }

}
