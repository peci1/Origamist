/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Component;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cz.cuni.mff.peckam.java.origamist.files.Category;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ListingTreeCellRenderer extends DefaultTreeCellRenderer
{

    /** */
    private static final long           serialVersionUID = 3865858017604226291L;

    /** The renderers used to render the files. */
    protected Hashtable<File, FileCell> fileRenderers    = new Hashtable<File, FileCell>();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus)
    {
        Locale l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
        ResourceBundle messages = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.gui.Gui", l);

        if (value instanceof Listing || value instanceof Category) {
            String text;
            if (value instanceof Listing) {
                text = messages.getString("listingRootNodeText");
            } else {
                text = ((Category) value).getName(l);
            }
            super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf, row, hasFocus);
            setIcon(expanded ? getOpenIcon() : getClosedIcon());
            return this;
        } else if (value instanceof File) {
            if (fileRenderers.get(value) == null) {
                fileRenderers.put((File) value, new FileCell((File) value));
            }
            FileCell cell = fileRenderers.get(value);
            cell.configure(sel, hasFocus);
            return cell;
        } else {
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }
}
