/**
 * Copyright (c) 2010, Martin Pecka (peci1@seznam.cz)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name Martin Pecka nor the
 * names of contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Martin Pecka BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Component for drawing custom images
 * 
 * @author Martin Pecka
 */
public class JImage extends JPanel
{

    /**
     * 
     */
    private static final long serialVersionUID = 4988265017725943377L;

    /**
     * The image to draw
     */
    protected BufferedImage   image            = null;

    /**
     * @param image The Image to display
     */
    public JImage(BufferedImage image)
    {
        this.image = image;
        this.setOpaque(false);
    }

    /**
     * @return the image
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image)
    {
        this.image = image;
        repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        if (this.image == null)
            return new Dimension(0, 0);
        return new Dimension(this.image.getWidth(), this.image.getHeight());
    }

    public double getCurrentScale()
    {
        if (this.image == null)
            return 1d;
        return this.getWidth() / image.getWidth();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        if (image != null) {
            int w = getWidth();
            int h = getHeight();
            int iw = image.getWidth();
            int ih = image.getHeight();

            double scale = Math.min((double) w / iw, (double) h / ih);

            int drawW = (int) (iw * scale);
            int drawH = (int) (ih * scale);

            g2.drawImage(image, (w - drawW) / 2, (h - drawH) / 2, drawW, drawH, null);
        }
    }
}
