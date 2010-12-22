/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import javax.swing.Icon;
import javax.swing.origamist.JLabelHandlingTooltip;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;

/**
 * A label displaying its text even in its tooltip, decorated by the TooltipFactory plain method.
 * 
 * @author Martin Pecka
 */
public class JLabelWithTooltip extends JLabelHandlingTooltip
{

    /** */
    private static final long serialVersionUID = -8472752133896284504L;

    /**
     * 
     */
    public JLabelWithTooltip()
    {
        super();

    }

    /**
     * @param image
     * @param horizontalAlignment
     */
    public JLabelWithTooltip(Icon image, int horizontalAlignment)
    {
        super(image, horizontalAlignment);

    }

    /**
     * @param image
     */
    public JLabelWithTooltip(Icon image)
    {
        super(image);

    }

    /**
     * @param text
     * @param icon
     * @param horizontalAlignment
     */
    public JLabelWithTooltip(String text, Icon icon, int horizontalAlignment)
    {
        super(text, icon, horizontalAlignment);

    }

    /**
     * @param text
     * @param horizontalAlignment
     */
    public JLabelWithTooltip(String text, int horizontalAlignment)
    {
        super(text, horizontalAlignment);

    }

    /**
     * @param text
     */
    public JLabelWithTooltip(String text)
    {
        super(text);

    }

    @Override
    protected String decorateTooltipText(String text)
    {
        return ServiceLocator.get(TooltipFactory.class).getPlain(text);
    }

}
