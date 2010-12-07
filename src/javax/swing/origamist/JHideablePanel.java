/**
 * 
 */
package javax.swing.origamist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A panel with a button that allows hiding that panel.
 * 
 * @author Martin Pecka
 */
public class JHideablePanel extends JPanel
{
    /** */
    private static final long serialVersionUID = -6265414666873979043L;

    /** The button that hides/shows the panel's content. */
    protected JButton         hideButton;
    /** The content of the panel. */
    protected JPanel          content;

    /** The arrow to show when the content is shown. */
    protected Icon            shownIcon;
    /** The arrow to show when the content is hidden. */
    protected Icon            hiddenIcon;

    /**
     * Create the panel that has a button to show/hide its contents.
     * 
     * @param buttonAlignment Alignment of the button. The direction of the arrow for shown/hidden state is computed
     *            from this value. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     */
    public JHideablePanel(String buttonAlignment)
    {
        this(buttonAlignment, getDefaultArrowAlignmentFromButtonAlignment(buttonAlignment));
    }

    /**
     * Create the panel that has a button to show/hide its contents.
     * 
     * @param buttonAlignment Alignment of the button. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param arrowDirection Direction of the arrow when the content is shown. The direction of the arrow when the
     *            content is hidden will be the opposite of this one. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     */
    public JHideablePanel(String buttonAlignment, String arrowDirection)
    {
        this(buttonAlignment, arrowDirection, getOppositeAlignment(arrowDirection));
    }

    /**
     * Create the panel that has a button to show/hide its contents.
     * 
     * @param buttonAlignment Alignment of the button. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param shownArrowDirection Direction of the arrow when the content is shown. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param hiddenArrowDirection Direction of the arrow when the content is hidden. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     */
    public JHideablePanel(String buttonAlignment, String shownArrowDirection, String hiddenArrowDirection)
    {
        this(getDefaultArrowButton(), buttonAlignment, shownArrowDirection, hiddenArrowDirection);
    }

    /**
     * Create the panel that has a button to show/hide its contents.
     * 
     * @param arrowButton The button that serves to show/hide the content.
     * @param buttonAlignment Alignment of the button. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param shownArrowDirection Direction of the arrow when the content is shown. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param hiddenArrowDirection Direction of the arrow when the content is hidden. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     */
    public JHideablePanel(JButton arrowButton, String buttonAlignment, String shownArrowDirection,
            String hiddenArrowDirection)
    {
        this(new JPanel(), arrowButton, buttonAlignment, shownArrowDirection, hiddenArrowDirection);
    }

    /**
     * Create the panel that has a button to show/hide its contents.
     * 
     * @param content The component containing the content of the panel.
     * @param arrowButton The button that serves to show/hide the content.
     * @param buttonAlignment Alignment of the button. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param shownArrowDirection Direction of the arrow when the content is shown. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @param hiddenArrowDirection Direction of the arrow when the content is hidden. One of
     *            <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     */
    public JHideablePanel(JPanel content, JButton arrowButton, final String buttonAlignment,
            String shownArrowDirection, String hiddenArrowDirection)
    {
        shownIcon = getIconForDirection(shownArrowDirection);
        hiddenIcon = getIconForDirection(hiddenArrowDirection);

        this.content = content;
        this.hideButton = arrowButton;

        hideButton.setIcon(shownIcon);
        hideButton.setText(null);
        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (JHideablePanel.this.content.isVisible()) {
                    hide();
                } else {
                    show();
                }
            }
        });

        setLayout(new BorderLayout());

        add(hideButton, buttonAlignment);
        add(content, BorderLayout.CENTER);
    }

    /**
     * Show the content.
     */
    public void show()
    {
        content.setVisible(true);
        hideButton.setIcon(shownIcon);
    }

    /**
     * Hide the content.
     */
    public void hide()
    {
        content.setVisible(false);
        hideButton.setIcon(hiddenIcon);
    }

    /**
     * @return the content
     */
    public JPanel getContent()
    {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(JPanel content)
    {
        this.content = content;
    }

    /**
     * @return The default button to be used as the arrow button.
     */
    protected static JButton getDefaultArrowButton()
    {
        // Just setting the icon will result in odd behavior with JButton. The icon isn't shown in the center, but in
        // the top left corner. That is the reason why paintComponent is overridden here.
        JButton but = new JButton() {
            /** */
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g)
            {
                Icon icon = getIcon();
                setIcon(null);
                super.paintComponent(g);
                setIcon(icon);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate((getWidth() - icon.getIconWidth()) / 2, (getHeight() - icon.getIconHeight()) / 2);
                icon.paintIcon(this, g2, 0, 0);
            }
        };
        but.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        return but;
    }

    /**
     * Set all sizes (max,min,default) of the component.
     * 
     * @param comp The component to set size of.
     * @param dimension The new dimension of the component.
     */
    protected static void setSize(JComponent comp, Dimension dimension)
    {
        comp.setSize(dimension);
        comp.setMaximumSize(dimension);
        comp.setMinimumSize(dimension);
    }

    /**
     * Return the alignment opposite to the one given.
     * 
     * @param alignment The alignment to be reversed. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @return The alignment opposite to the one given.
     */
    protected static String getOppositeAlignment(String alignment)
    {
        if (BorderLayout.NORTH.equals(alignment)) {
            return BorderLayout.SOUTH;
        } else if (BorderLayout.SOUTH.equals(alignment)) {
            return BorderLayout.NORTH;
        } else if (BorderLayout.EAST.equals(alignment)) {
            return BorderLayout.WEST;
        } else if (BorderLayout.WEST.equals(alignment)) {
            return BorderLayout.EAST;
        } else {
            return BorderLayout.SOUTH;
        }
    }

    /**
     * Return the alignment of an arrow computed from the alignment of the button.
     * 
     * @param buttonAlignment The alignment of the button. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @return The alignment of an arrow computed from the alignment of the button.
     */
    protected static String getDefaultArrowAlignmentFromButtonAlignment(String buttonAlignment)
    {
        return getOppositeAlignment(buttonAlignment);
    }

    /**
     * Return the icon of an arrow pointing to the given direction.
     * 
     * @param direction The direction the arrow points to. One of <code>BorderLayout.(NORTH|SOUTH|EAST|WEST)</code>.
     * @return The icon of an arrow pointing to the given direction.
     */
    protected static Icon getIconForDirection(String direction)
    {
        if (BorderLayout.NORTH.equals(direction)) {
            return new NorthIcon();
        } else if (BorderLayout.SOUTH.equals(direction)) {
            return new SouthIcon();
        } else if (BorderLayout.EAST.equals(direction)) {
            return new EastIcon();
        } else if (BorderLayout.WEST.equals(direction)) {
            return new WestIcon();
        } else {
            return new SouthIcon();
        }
    }

    /**
     * An arrow icon.
     * 
     * @author Martin Pecka
     */
    protected static class Arrow implements Icon
    {

        /** Color of the arrow. */
        protected Color   arrowColor = Color.black;
        /** The points the arrow consists of. */
        protected Polygon points;
        /** Cached width of the arrow. */
        protected int     width;
        /** Cached height of the arrow. */
        protected int     height;

        /**
         * Creates a triangle arrow.
         * 
         * @param x The first vertex of the triangle.
         * @param y The second vertex of the triangle.
         * @param z The third vertex of the triangle.
         */
        public Arrow(Point x, Point y, Point z)
        {
            points = new Polygon();
            points.addPoint(x.x, x.y);
            points.addPoint(y.x, y.y);
            points.addPoint(z.x, z.y);
            updateSize();
        }

        /**
         * Creates the arrow from the given polygon.
         * 
         * @param polygon The polygon to draw by this icon.
         */
        public Arrow(Polygon polygon)
        {
            points = polygon;
            updateSize();
        }

        /**
         * Recomputes the size of the icon from the polygon.
         */
        protected void updateSize()
        {
            Rectangle size = points.getBounds();
            width = (int) size.getWidth();
            height = (int) size.getHeight();
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            g.setColor(arrowColor);
            ((Graphics2D) g).getRenderingHints().add(
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            g.drawPolygon(points);
            g.fillPolygon(points);
        }

        @Override
        public int getIconWidth()
        {
            return width;
        }

        @Override
        public int getIconHeight()
        {
            return height;
        }

    }

    /**
     * An arrow with its head pointing to the south.
     * 
     * @author Martin Pecka
     */
    protected static class SouthIcon extends Arrow
    {
        public SouthIcon()
        {
            super(new Point(0, 0), new Point(10, 0), new Point(5, 7));
        }
    }

    /**
     * An arrow with its head pointing to the north.
     * 
     * @author Martin Pecka
     */
    protected static class NorthIcon extends Arrow
    {
        public NorthIcon()
        {
            super(new Point(0, 7), new Point(10, 7), new Point(5, 0));
        }
    }

    /**
     * An arrow with its head pointing to the west.
     * 
     * @author Martin Pecka
     */
    protected static class WestIcon extends Arrow
    {
        public WestIcon()
        {
            super(new Point(7, 0), new Point(7, 10), new Point(0, 5));
        }
    }

    /**
     * An arrow with its head pointing to the east.
     * 
     * @author Martin Pecka
     */
    protected static class EastIcon extends Arrow
    {
        public EastIcon()
        {
            super(new Point(0, 0), new Point(0, 10), new Point(7, 5));
        }
    }
}
