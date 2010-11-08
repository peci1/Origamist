/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

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

}
