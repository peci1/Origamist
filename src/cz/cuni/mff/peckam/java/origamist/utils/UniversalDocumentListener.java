/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A document listener that performs a universal action on insert/update/remove.
 * 
 * @author Martin Pecka
 */
public abstract class UniversalDocumentListener implements DocumentListener
{

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        update(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        update(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        update(e);
    }

    /**
     * Perform the update.
     * 
     * @param e
     */
    protected abstract void update(DocumentEvent e);

}
