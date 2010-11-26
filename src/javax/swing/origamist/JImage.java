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
package javax.swing.origamist;

import static java.lang.Math.abs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Component for drawing images instead of showing them as a label's icon.
 * 
 * You can use setAlignmentX() and setAlignmentY() to specify the position of the image if it doesn't have the same
 * aspect ratio as this component. Allowed values are JComponent.TOP_ALIGNMENT, JComponent.CENTER_ALIGNMENT,
 * JComponent.BOTTOM_ALIGNMENT for y values, and JComponent.LEFT_ALIGNMENT, JComponent.CENTER_ALIGNMENT,
 * JComponent.RIGHT_ALIGNMENT for x values.
 * 
 * @author Martin Pecka
 */
public class JImage extends JPanel
{

    /** */
    private static final long                   serialVersionUID   = 4988265017725943377L;

    /** The image to display. */
    protected Image                             image              = null;

    /** The scaled instances of the image to display. */
    final protected Hashtable<Dimension, Image> scaledImages       = new Hashtable<Dimension, Image>();

    /** The currently used (and probably scaled) image to display. */
    protected Image                             currentImage       = null;

    /** The listener to be fired when the image maybe needs resizing. */
    protected ComponentListener                 imageScaleListener = null;

    protected MediaTracker                      mediaTracker       = new MediaTracker(this);

    /**
     * @param image The Image to display
     */
    public JImage(Image image)
    {
        this.setOpaque(false);
        imageScaleListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                if (JImage.this.image == null)
                    return;
                final Dimension size = e.getComponent().getSize();
                Image current = scaledImages.get(size);
                if (current != null) {
                    currentImage = current;
                    revalidate();
                    repaint();
                } else if (size.width > 0 && size.height > 0) {
                    final int w = getWidth();
                    final int h = getHeight();

                    try {
                        mediaTracker.waitForID(0);
                    } catch (InterruptedException e1) {}

                    int iw = JImage.this.image.getWidth(null);
                    int ih = JImage.this.image.getHeight(null);

                    double scale = Math.min((double) w / iw, (double) h / ih);

                    int drawW = (int) (iw * scale);
                    int drawH = (int) (ih * scale);

                    currentImage = JImage.this.image.getScaledInstance(drawW, drawH, Image.SCALE_SMOOTH);
                    mediaTracker.addImage(currentImage, currentImage.hashCode(), drawW, drawH);
                    scaledImages.put(size, currentImage);
                    try {
                        mediaTracker.waitForID(currentImage.hashCode());
                    } catch (InterruptedException e1) {}
                    revalidate();
                    repaint();

                }
            }
        };
        addComponentListener(imageScaleListener);

        setImage(image);
    }

    /**
     * @return The displayed image.
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * @return The currently used (probably scaled) instance of the image.
     */
    public Image getCurrentImage()
    {
        return currentImage;
    }

    /**
     * @param image The image to display.
     */
    public void setImage(Image image)
    {
        if (this.image != null)
            mediaTracker.removeImage(this.image);
        this.image = image;
        mediaTracker.addImage(image, 0);
        scaledImages.clear();
        imageScaleListener.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        if (this.image == null)
            return new Dimension(0, 0);

        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {}

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        if (width == -1 || height == -1)
            return new Dimension(0, 0);

        return new Dimension(width, height);
    }

    /**
     * @return The scale the given image is displayed at.
     */
    public double getCurrentScale()
    {
        if (this.currentImage == null)
            return 1d;

        int width = currentImage.getWidth(null);

        if (width == -1)
            return 1d;

        return this.getWidth() / width;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        if (currentImage != null) {
            int w = getWidth();
            int h = getHeight();

            int iw = currentImage.getWidth(null);
            int ih = currentImage.getHeight(null);

            // the image should have been loaded yet, but for sure...
            if (iw == -1 || ih == -1) {
                return;
            }

            int left;
            if (abs(getAlignmentX() - JComponent.LEFT_ALIGNMENT) < 0.000001f) {
                left = 0;
            } else if (abs(getAlignmentX() - JComponent.CENTER_ALIGNMENT) < 0.000001f) {
                left = (w - iw) / 2;
            } else {
                left = w - iw;
            }

            int top;
            if (abs(getAlignmentY() - JComponent.TOP_ALIGNMENT) < 0.000001f) {
                top = 0;
            } else if (abs(getAlignmentY() - JComponent.CENTER_ALIGNMENT) < 0.000001f) {
                top = (h - ih) / 2;
            } else {
                top = h - ih;
            }

            g2.drawImage(currentImage, left, top, iw, ih, null);
        }
    }
}
