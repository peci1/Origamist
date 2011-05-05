/**
 * 
 */
package javax.swing.origamist;

import javax.swing.DefaultListModel;

/**
 * A {@link javax.swing.JList} implementation that allows explicit requests to recompute row heights.
 * 
 * @author Martin Pecka
 */
public class JList extends javax.swing.JList
{

    /** */
    private static final long serialVersionUID = -8001998421679099186L;

    /**
     * Force recomputing of the cached cell heights.
     */
    public void recomputeHeights()
    {
        // the cache is located in BasicListUI and is cleared in this property change
        firePropertyChange("model", new DefaultListModel(), new DefaultListModel());
    }

}
