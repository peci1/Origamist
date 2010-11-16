/**
 * 
 */
package javax.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.Hashtable;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * A high-quality antialiased rounded border.
 * 
 * @author Keilly, http://blog.keilly.com/2008/05/creating-swing-widget-part-2-border.html
 */
public class RoundedBorder extends AbstractBorder
{
    /** */
    private static final long                              serialVersionUID = 1473481894085410085L;

    /** The insets of the border. */
    protected Insets                                       insets;
    /** The stroke to draw the line with. */
    protected Stroke                                       stroke           = null;
    /** Color of the drawn line. */
    protected Color                                        strokeColor      = Color.BLACK;
    /** The size of the arc to use. */
    protected int                                          arc;
    /** The width of the stroke to use. */
    protected float                                        strokeWidth      = 0f;

    // TODO maybe make this static and add <code>strokeWidth</code> to the search key
    /** The cached rectangles containing the content based on the requested size. */
    protected Hashtable<Dimension, RoundRectangle2D.Float> rect             = new Hashtable<Dimension, RoundRectangle2D.Float>();

    /**
     * Simple rounded border with no outline.
     * 
     * @param arc The size of the arc to use.
     */
    public RoundedBorder(int arc)
    {
        this.arc = arc;
        int i = (int) (arc / Math.PI) / 2;
        insets = new Insets(i, i, i, i);
    }

    /**
     * Rounded border with an outline.
     * 
     * @param arc The size of the arc to use.
     * @param strokeWidth Width of the outline.
     * @param color Color of the outline.
     */
    public RoundedBorder(int arc, float strokeWidth, Color color)
    {
        this.arc = arc;
        int i = (int) ((arc / Math.PI) + ((strokeWidth * 2) / (Math.PI)));
        insets = new Insets(i, i, i, i);
        this.stroke = new BasicStroke(strokeWidth);
        this.strokeColor = color;
        this.strokeWidth = strokeWidth;
    }

    /**
     * Returns the round rectangle that is used to draw the inner area of the component.
     * 
     * @param dim The outer dimension of the component.
     * @return The round rectangle that is used to draw the inner area of the component.
     */
    public RoundRectangle2D getRoundRectangle(Dimension dim)
    {
        if (rect.get(dim) == null) {
            int i = (int) strokeWidth / 2;
            rect.put(dim, new RoundRectangle2D.Float(i, i, dim.width - strokeWidth, dim.height - strokeWidth, arc, arc));
        }
        return rect.get(dim);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension dim = new Dimension(width, height);
        if (stroke != null) {
            int i = (int) strokeWidth / 2;
            if (rect.get(dim) == null)
                rect.put(dim, new RoundRectangle2D.Float(i, i, width - strokeWidth, height - strokeWidth, arc, arc));
            g2.translate(x, y);
            g2.setColor(c.getBackground());
            g2.fill(rect.get(dim));
            g2.setColor(strokeColor);
            g2.setStroke(stroke);
            g2.draw(rect.get(dim));
        } else {
            if (rect.get(dim) == null)
                rect.put(dim, new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            g2.translate(x, y);
            g2.setColor(c.getBackground());
            g2.fill(rect.get(dim));
        }
    }

    @Override
    public Insets getBorderInsets(Component c)
    {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets)
    {
        return insets;
    }

    @Override
    public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height)
    {
        return getInteriorRectangle(c, this, x, y, width, height);
    }

    public static Rectangle getInteriorRectangle(Component c, Border b, int x, int y, int width, int height)
    {
        Insets insets;
        if (b != null)
            insets = b.getBorderInsets(c);
        else
            insets = new Insets(0, 0, 0, 0);
        return new Rectangle(x + insets.left, y + insets.top, width - insets.right - insets.left, height - insets.top
                - insets.bottom);
    }
}
