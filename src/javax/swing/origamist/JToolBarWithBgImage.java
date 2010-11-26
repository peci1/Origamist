/**
 * 
 */
package javax.swing.origamist;

import java.awt.Graphics;

/**
 * A JToolBar with background image support added.
 * 
 * @author Martin Pecka
 */
public class JToolBarWithBgImage extends OrigamistToolBar
{
    /** */
    private static final long        serialVersionUID = -1484600700012452480L;

    protected BackgroundImageSupport bg               = null;

    /**
     * @return the backgroundImage
     */
    public BackgroundImageSupport getBackgroundImage()
    {
        return bg;
    }

    /**
     * @param bg the backgroundImage to set
     */
    public void setBackgroundImage(BackgroundImageSupport bg)
    {
        this.bg = bg;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (bg != null)
            bg.paintComponent(g);
    }
}
