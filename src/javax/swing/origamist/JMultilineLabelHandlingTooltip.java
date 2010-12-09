/**
 * 
 */
package javax.swing.origamist;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A multiline label copying its content to its tooltip.
 * 
 * @author Martin Pecka
 */
public class JMultilineLabelHandlingTooltip extends JMultilineLabel
{

    /** */
    private static final long serialVersionUID = -8982186543105385545L;

    {
        DocumentListener l = new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                setToolTipText(decorateTooltipText(getText()));
            }
        };
        this.getDocument().addDocumentListener(l);
        l.changedUpdate(null);
    }

    /**
     * @param text
     */
    public JMultilineLabelHandlingTooltip(String text)
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
        return "<html>" + text.replaceAll("<br[ /]*>", "\n").replaceAll("<[^>]*\\>", "").trim() + "</html>";
    }

}
