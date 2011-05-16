/**
 * 
 */
package javax.swing.origamist;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

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
        // changing the model is one of the easiest ways to recompute the cell heights
        ListModel model = getModel();
        int[] selected = getSelectedIndices();

        setModel(new DefaultListModel());
        setModel(model);

        setSelectedIndices(selected);
    }

}
