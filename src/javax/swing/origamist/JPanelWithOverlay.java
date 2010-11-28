/** 
 * 
 */
package javax.swing.origamist;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * A JPanel that allows showing an overlay component above its content.
 * 
 * Add content components to getContent() and overlay components to getOverlay(). <b>Do not use add() directly on this
 * panel!</b>
 * 
 * @author Martin Pecka
 */
public class JPanelWithOverlay extends JPanel
{

    /** */
    private static final long serialVersionUID = 7763109228109028354L;

    /** The contents to be displayed. */
    protected JComponent      content;
    /** The overlay to be displayed on request. */
    protected JComponent      overlay;

    /**
     * Create a new panel with overlay with {@link FlowLayout} as the layout of the content.
     * 
     * Double buffering is set to <code>true</code>.
     */
    public JPanelWithOverlay()
    {
        this(true);
    }

    /**
     * Create a new panel with overlay with {@link FlowLayout} as the layout of the content.
     * 
     * @param isDoubleBuffered True for double-buffering, which uses additional memory space to achieve fast,
     *            flicker-free updates
     */
    public JPanelWithOverlay(boolean isDoubleBuffered)
    {
        this(new FlowLayout(), isDoubleBuffered);
    }

    /**
     * Create a new panel with overlay. Set the layout of the content to <code>layout</code>.
     * 
     * @param layout The layout for the content part of the panel.
     * @param isDoubleBuffered True for double-buffering, which uses additional memory space to achieve fast,
     *            flicker-free updates
     */
    public JPanelWithOverlay(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
        this.setLayout(new OverlayLayout(this));
        setContent(new JPanel());
        setOverlay(new JPanel());
        getContent().setLayout(layout);
    }

    /**
     * Create a new panel with overlay. Set the layout of the content to <code>layout</code>.
     * 
     * Double buffering is set to <code>true</code>.
     * 
     * @param layout The layout for the content part of the panel.
     */
    public JPanelWithOverlay(LayoutManager layout)
    {
        this(layout, true);
    }

    /**
     * @return The overlay container.
     */
    public JComponent getOverlay()
    {
        return overlay;
    }

    /**
     * Sets the component that represents the ovelay.
     * 
     * @param overlay The component to set.
     */
    public void setOverlay(JComponent overlay)
    {
        this.overlay = overlay;
        super.removeAll();
        if (overlay != null)
            super.add(overlay);
        if (content != null)
            super.add(content);
    }

    /**
     * @return The content container.
     */
    public JComponent getContent()
    {
        return content;
    }

    /**
     * Sets the content container.
     * 
     * @param content The content container.
     */
    public void setContent(JComponent content)
    {
        if (this.content != null)
            remove(this.content);
        this.content = content;
        if (content != null)
            add(content);
    }

    /**
     * Display the overlay over the content.
     */
    public void showOverlay()
    {
        if (getOverlay() != null)
            getOverlay().setVisible(true);
    }

    /**
     * Hide the overlay.
     */
    public void hideOverlay()
    {
        if (getOverlay() != null)
            getOverlay().setVisible(false);
    }

}
