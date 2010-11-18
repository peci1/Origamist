/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Component;
import java.util.Locale;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import cz.cuni.mff.peckam.java.origamist.files.Category;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A renderer able to "render" File and Category objects.
 * 
 * @author Martin Pecka
 */
public class ListingTreeCellRenderer extends DefaultTreeCellRenderer
{

    /** */
    private static final long    serialVersionUID = 3865858017604226291L;

    /** The renderer used to render files. */
    protected final FileRenderer fileRenderer     = new FileRenderer();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, final Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus)
    {
        if (!(value instanceof DefaultMutableTreeNode))
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Object nodeVal = ((DefaultMutableTreeNode) value).getUserObject();
        if (nodeVal instanceof Category) {
            Locale l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
            String text = ((Category) nodeVal).getName(l);
            super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf, row, hasFocus);
            setIcon(expanded ? getOpenIcon() : getClosedIcon());
            return this;
        } else if (nodeVal instanceof File) {
            fileRenderer.configure((File) nodeVal, sel, hasFocus);
            return fileRenderer;
        } else {
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }
}
