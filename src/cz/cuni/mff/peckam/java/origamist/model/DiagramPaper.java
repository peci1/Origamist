/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.awt.Color;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents the paper the diagram is made of.
 * 
 * @author Martin Pecka
 */
@XmlTransient
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
