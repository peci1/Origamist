/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

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
public class ListingTree extends JTree
{

    /** */
    private static final long serialVersionUID = 7977020048548617471L;

    /**
     * 
     */
    public ListingTree()
    {
        super();
    }

    /**
     * @param value
     */
    public ListingTree(Hashtable<?, ?> value)
    {
        super(value);
    }

    /**
     * @param value
     */
    public ListingTree(Object[] value)
    {
        super(value);
    }

    /**
     * @param newModel
     */
    public ListingTree(TreeModel newModel)
    {
        super(newModel);
    }

    /**
     * @param root
     * @param asksAllowsChildren
     */
    public ListingTree(TreeNode root, boolean asksAllowsChildren)
    {
        super(root, asksAllowsChildren);
    }

    /**
     * @param root
     */
    public ListingTree(TreeNode root)
    {
        super(root);
    }

    /**
     * @param value
     */
    public ListingTree(Vector<?> value)
    {
        super(value);
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus)
    {
        Locale l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
        if (value instanceof Listing) {
            return ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.gui.Gui", l).getString(
                    "listingRootNodeText");
        } else if (value instanceof Category) {
            return ((Category) value).getName(l);
        } else if (value instanceof File) {
            return ((File) value).getName(l);
        } else {
            return value.toString();
        }
    }

}
