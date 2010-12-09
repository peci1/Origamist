/**
 * 
 */
package javax.swing.origamist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * A label copying its value to its tooltip.
 * 
 * @author Martin Pecka
 */
public class JLabelHandlingTooltip extends JLabel
{

    /** */
    private static final long serialVersionUID = 5548585264640535623L;

    {
        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                setToolTipText(decorateTooltipText(getText()));
            }
        };
        addPropertyChangeListener("text", l);
        l.propertyChange(new PropertyChangeEvent(this, "text", null, getText()));
    }

    /**
     * 
     */
    public JLabelHandlingTooltip()
    {
        super();

    }

    /**
     * @param image
     * @param horizontalAlignment
     */
    public JLabelHandlingTooltip(Icon image, int horizontalAlignment)
    {
        super(image, horizontalAlignment);

    }

    /**
     * @param image
     */
    public JLabelHandlingTooltip(Icon image)
    {
        super(image);

    }

    /**
     * @param text
     * @param icon
     * @param horizontalAlignment
     */
    public JLabelHandlingTooltip(String text, Icon icon, int horizontalAlignment)
    {
        super(text, icon, horizontalAlignment);

    }

    /**
     * @param text
     * @param horizontalAlignment
     */
    public JLabelHandlingTooltip(String text, int horizontalAlignment)
    {
        super(text, horizontalAlignment);

    }

    /**
     * @param text
     */
    public JLabelHandlingTooltip(String text)
    {
        super(text);

    }

    /**
     * Format the text to be shown in tooltip.
     * 
     * This implementation does nothing to the text.
     * 
     * @param text The text to format.
     * @return The formatted text.
     */
    protected String decorateTooltipText(String text)
    {
        return text;
    }

}
