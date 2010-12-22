/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import javax.swing.origamist.JMultilineLabelHandlingTooltip;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;

/**
 * A multiline label displaying its text even in its tooltip, decorated by the TooltipFactory plain method.
 * 
 * @author Martin Pecka
 */
public class JMultilineLabelWithTooltip extends JMultilineLabelHandlingTooltip
{

    /** */
    private static final long serialVersionUID = -3236939046912764643L;

    /**
     * @param text
     */
    public JMultilineLabelWithTooltip(String text)
    {
        super(text);
    }

    @Override
    protected String decorateTooltipText(String text)
    {
        return ServiceLocator.get(TooltipFactory.class).getPlain(super.decorateTooltipText(text));
    }
}
