/**
 * 
 */
package javax.swing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.net.URL;

/**
 * This class handles displaying an image on the background of the component that contains it.
 * 
 * To display the background image, just override <code>paintComponent</code> in the Swing component and call
 * <code>backgroundImageSupport.paintComponent()</code> from it.
 * 
 * @author Martin Pecka
 */
public class BackgroundImageSupport
{
    /** The image to display. */
    protected ImageIcon           image;

    /** The component which the background is drawn on. */
    protected JComponent          component;

    /** The position of the image. */
    protected PositionInComponent position;

    /** The directions to in which to repeat the background. */
    protected BackgroundRepeat    repeat;

    /**
     * Setup the background image for <code>component</code>.
     * 
     * @param image The image to be displayed.
     * @param component The component the image will be displayed on.
     * @param xPos Horizontal position of the background: can be a number (px from the left), string ("left", "center",
     *            "right") or a percent-string ("36%").
     * @param yPos Vertical position of the background: can be a number (px from the top), string ("top", "center",
     *            "bottom") or a percent-string ("36%").
     * @param repeat The repeat behavior. If repeating along an axis is enabled, the corresponding background position
     *            is ignored and the image is repeated along the axis.
     */
    public BackgroundImageSupport(ImageIcon image, JComponent component, String xPos, String yPos,
            BackgroundRepeat repeat)
    {
        this.image = image;
        MediaTracker mt = new MediaTracker(component);
        mt.addImage(image.getImage(), 1);
        try {
            mt.waitForID(1);
        } catch (InterruptedException e) {}
        this.component = component;
        this.position = new PositionInComponent(component, xPos, yPos, image.getIconWidth(), image.getIconWidth());
        this.repeat = repeat;
    }

    /**
     * Setup the background image for <code>component</code>.
     * 
     * @param image The image to be displayed.
     * @param component The component the image will be displayed on.
     * @param xPos Horizontal position of the background: the distance from the left side in <code>px</code>.
     * @param yPos Vertical position of the background: the distance from the top side in <code>px</code>.
     * @param repeat The repeat behavior. If repeating along an axis is enabled, the corresponding background position
     *            is ignored and the image is repeated along the axis.
     */
    public BackgroundImageSupport(ImageIcon image, JComponent component, int xPos, int yPos, BackgroundRepeat repeat)
    {
        this(image, component, String.valueOf(xPos), String.valueOf(yPos), repeat);
    }

    /**
     * Setup the background image for <code>component</code>.
     * 
     * @param image The image to be displayed.
     * @param component The component the image will be displayed on.
     * @param xPos Horizontal position of the background: can be a number (px from the left), string ("left", "center",
     *            "right") or a percent-string ("36%").
     * @param yPos Vertical position of the background: can be a number (px from the top), string ("top", "center",
     *            "bottom") or a percent-string ("36%").
     * @param repeat The repeat behavior. If repeating along an axis is enabled, the corresponding background position
     *            is ignored and the image is repeated along the axis.
     */
    public BackgroundImageSupport(Image image, JComponent component, String xPos, String yPos, BackgroundRepeat repeat)
    {
        this(new ImageIcon(image), component, xPos, yPos, repeat);
    }

    /**
     * Setup the background image for <code>component</code>.
     * 
     * @param image The image to be displayed.
     * @param component The component the image will be displayed on.
     * @param xPos Horizontal position of the background: the distance from the left side in <code>px</code>.
     * @param yPos Vertical position of the background: the distance from the top side in <code>px</code>.
     * @param repeat The repeat behavior. If repeating along an axis is enabled, the corresponding background position
     *            is ignored and the image is repeated along the axis.
     */
    public BackgroundImageSupport(Image image, JComponent component, int xPos, int yPos, BackgroundRepeat repeat)
    {
        this(new ImageIcon(image), component, xPos, yPos, repeat);
    }

    /**
     * Setup the background image for <code>component</code>.
     * 
     * @param image The image to be displayed.
     * @param component The component the image will be displayed on.
     * @param xPos Horizontal position of the background: can be a number (px from the left), string ("left", "center",
     *            "right") or a percent-string ("36%").
     * @param yPos Vertical position of the background: can be a number (px from the top), string ("top", "center",
     *            "bottom") or a percent-string ("36%").
     * @param repeat The repeat behavior. If repeating along an axis is enabled, the corresponding background position
     *            is ignored and the image is repeated along the axis.
     */
    public BackgroundImageSupport(URL image, JComponent component, String xPos, String yPos, BackgroundRepeat repeat)
    {
        this(new ImageIcon(image), component, xPos, yPos, repeat);
    }

    /**
     * Setup the background image for <code>component</code>.
     * 
     * @param image The image to be displayed.
     * @param component The component the image will be displayed on.
     * @param xPos Horizontal position of the background: the distance from the left side in <code>px</code>.
     * @param yPos Vertical position of the background: the distance from the top side in <code>px</code>.
     * @param repeat The repeat behavior. If repeating along an axis is enabled, the corresponding background position
     *            is ignored and the image is repeated along the axis.
     */
    public BackgroundImageSupport(URL image, JComponent component, int xPos, int yPos, BackgroundRepeat repeat)
    {
        this(new ImageIcon(image), component, xPos, yPos, repeat);
    }

    /**
     * This method should be called from the owner component's <code>paintComponent()</code> to draw the background.
     * 
     * Ideally, this should be called just after <code>super.paintComponent()</code> is called in the owner.
     * 
     * @param g The graphics to use for the drawing.
     */
    public void paintComponent(Graphics g)
    {
        Point loc = position.getLocation();
        if (repeat.repeatX)
            loc.x = 0;
        if (repeat.repeatY)
            loc.y = 0;

        int iw = image.getIconWidth();
        int ih = image.getIconHeight();

        int cw = component.getWidth();
        int ch = component.getHeight();

        for (int x = 0; x < (repeat.repeatX ? Math.ceil((cw / (double) iw)) : 1); x++) {
            for (int y = 0; y < (repeat.repeatY ? Math.ceil((ch / (double) ih)) : 1); y++) {
                g.drawImage(image.getImage(), loc.x + x * iw, loc.y + y * ih, null);
            }
        }
    }

    public enum BackgroundRepeat
    {
        /** Don't repeat along any axis. */
        NO_REPEAT(false, false),
        /** Repeat horizontally. */
        REPEAT_X(true, false),
        /** Repeat vertically. */
        REPEAT_Y(false, true),
        /** Repeat along both aces. */
        REPEAT_ALL(true, true);

        boolean repeatX = false;
        boolean repeatY = false;

        BackgroundRepeat(boolean x, boolean y)
        {
            repeatX = x;
            repeatY = y;
        }
    }

    protected class PositionInComponent
    {
        String     x;
        String     y;
        int        w;
        int        h;
        JComponent c;

        public PositionInComponent(JComponent c, String x, String y, int w, int h)
        {
            this.c = c;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public PositionInComponent(JComponent c, int x, int y, int w, int h)
        {
            this(c, String.valueOf(x), String.valueOf(y), w, h);
        }

        public Point getLocation()
        {
            int cw = c.getWidth();
            int ch = c.getHeight();

            Point result = new Point();

            if ("left".equals(x)) {
                x = "0";
            } else if ("center".equals(x)) {
                x = "50%";
            } else if ("right".equals(x)) {
                x = "100%";
            }

            try {
                result.x = Integer.parseInt(x);
            } catch (NumberFormatException e) {
                if (x.matches("%$")) {
                    try {
                        int percent = Integer.parseInt(x.substring(0, x.length() - 1));
                        if (percent < 0 || percent > 100)
                            throw new NumberFormatException();
                        result.x = (int) ((percent / 100d) * (cw - w));
                    } catch (NumberFormatException e1) {
                        result.x = 0;
                    }
                } else {
                    result.x = 0;
                }
            }

            if ("top".equals(y)) {
                y = "0";
            } else if ("center".equals(y)) {
                y = "50%";
            } else if ("bottom".equals(y)) {
                y = "100%";
            }

            try {
                result.y = Integer.parseInt(y);
            } catch (NumberFormatException e) {
                if (y.matches("%$")) {
                    try {
                        int percent = Integer.parseInt(y.substring(0, y.length() - 1));
                        if (percent < 0 || percent > 100)
                            throw new NumberFormatException();
                        result.y = (int) ((percent / 100d) * (ch - h));
                    } catch (NumberFormatException e1) {
                        result.y = 0;
                    }
                } else {
                    result.y = 0;
                }
            }

            return result;
        }

    }
}
