/**
 * 
 */
package javax.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.TitledBorder;

/**
 * A horizontal menu separator with title.
 * 
 * @author Martin Pecka
 */
public class JTitledSeparator extends JPanel
{

    /** */
    private static final long serialVersionUID = -304296629715050067L;

    /**
     * @param title The title that would be written over the separator line.
     */
    public JTitledSeparator(String title)
    {
        setBorder(new TitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY), title));
        setTitleColor(new Color(127, 127, 127));
    }

    /**
     * Set the title to be displayed.
     * 
     * @param title The title to be displayed.
     */
    public void setTitle(String title)
    {
        ((TitledBorder) getBorder()).setTitle(title);
    }

    /**
     * Set the font of the title.
     * 
     * @param font The font of the title.
     */
    public void setTitleFont(Font font)
    {
        ((TitledBorder) getBorder()).setTitleFont(font);
    }

    /**
     * Set the color of the title.
     * 
     * @param color The color of the title.
     */
    public void setTitleColor(Color color)
    {
        ((TitledBorder) getBorder()).setTitleColor(color);
    }

    /**
     * Set the color of the separator line.
     * 
     * @param color The color of the line.
     */
    public void setLineColor(Color color)
    {
        ((TitledBorder) getBorder()).setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, color));
    }

    @Override
    public Dimension getPreferredSize()
    {
        int height;
        if (getGraphics() != null) {
            // this one is more precise
            height = getGraphics().getFontMetrics(((TitledBorder) getBorder()).getTitleFont()).getHeight();
        } else {
            height = ((TitledBorder) getBorder()).getTitleFont().getSize();
        }
        return new Dimension(0, height);
    }

}
