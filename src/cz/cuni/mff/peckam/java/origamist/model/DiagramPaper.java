/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.awt.Color;

/**
 * Represents the paper the diagram is made of.
 * 
 * @author Martin Pecka
 */
public class DiagramPaper extends
        cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper
{
    /**
     * @return The background color of the paper.
     */
    public Color getBackgroundColor()
    {
        return color.getBackground();
    }

    /**
     * @param c The color to be set
     */
    public void setBackgroundColor(Color c)
    {
        color.setBackground(c);
    }
}
