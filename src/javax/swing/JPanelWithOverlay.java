/** 
 * 
 */
package javax.swing;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

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
    protected JComponent      content          = null;
    /** The overlay to be displayed on request. */
    protected JComponent      overlay          = null;

    /** */
    public JPanelWithOverlay()
    {
        this(true);
    }

    /** */
    public JPanelWithOverlay(boolean isDoubleBuffered)
    {
        this(new FlowLayout(), isDoubleBuffered);
    }

    /** */
    public JPanelWithOverlay(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
        super.setLayout(new OverlayLayout(this));
        setOverlay(getOverlay());
    }

    /** */
    public JPanelWithOverlay(LayoutManager layout)
    {
        this(layout, true);
    }

    /**
     * @return The overlay container.
     */
    public JComponent getOverlay()
    {
        if (overlay == null)
            overlay = new JPanel();
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
        super.add(overlay);
        super.add(getContent());
    }

    /**
     * @return The content container.
     */
    public JComponent getContent()
    {
        if (content == null)
            content = new JPanel();
        return content;
    }

    /**
     * Sets the content container.
     * 
     * @param content The content container.
     */
    public void setContent(JComponent content)
    {
        remove(this.content);
        this.content = content;
        add(content);
    }

    /**
     * Display the overlay over the content.
     */
    public void showOverlay()
    {
        getOverlay().setVisible(true);;
    }

    /**
     * Hide the overlay.
     */
    public void hideOverlay()
    {
        getOverlay().setVisible(false);
    }

}
