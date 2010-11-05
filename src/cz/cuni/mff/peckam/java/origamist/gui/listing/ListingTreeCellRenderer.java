/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ListingTreeCellRenderer extends DefaultTreeCellRenderer
{

    /** */
    private static final long serialVersionUID = 3865858017604226291L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus)
    {
        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        // TODO Auto-generated method stub
    }

}
