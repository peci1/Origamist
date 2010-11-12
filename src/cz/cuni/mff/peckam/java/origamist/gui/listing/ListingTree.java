/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import cz.cuni.mff.peckam.java.origamist.files.File;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ListingTree extends JTree
{

    /** */
    private static final long serialVersionUID = 7977020048548617471L;

    {
        setToolTipText("");
        TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setSelectionModel(selectionModel);
    }

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
    public String getToolTipText(MouseEvent event)
    {
        if (getRowForLocation(event.getX(), event.getY()) == -1)
            return null;
        TreePath curPath = getPathForLocation(event.getX(), event.getY());
        Object comp = curPath.getLastPathComponent();
        if (comp instanceof File) {
            FileCell fc = (FileCell) getCellRenderer().getTreeCellRendererComponent(this, comp, false, false, true, 0,
                    false);
            Rectangle entryBounds = this.getPathBounds(curPath);
            int x = (int) (event.getX() - entryBounds.getX());
            int y = (int) (event.getY() - entryBounds.getY());
            MouseEvent e = new MouseEvent((Component) event.getSource(), event.getID(), event.getWhen(),
                    event.getModifiers(), x, y, event.getXOnScreen(), event.getYOnScreen(), event.getClickCount(),
                    true, event.getButton());
            return fc.getToolTipText(e);
        } else {
            return null;
        }
    }

    @Override
    public Point getToolTipLocation(MouseEvent event)
    {
        if (getRowForLocation(event.getX(), event.getY()) == -1)
            return super.getToolTipLocation(event);
        TreePath curPath = getPathForLocation(event.getX(), event.getY());
        Object comp = curPath.getLastPathComponent();
        if (comp instanceof File) {
            FileCell fc = (FileCell) getCellRenderer().getTreeCellRendererComponent(this, comp, false, false, true, 0,
                    false);
            Rectangle entryBounds = this.getPathBounds(curPath);
            int x = (int) (event.getX() - entryBounds.getX());
            int y = (int) (event.getY() - entryBounds.getY());
            MouseEvent e = new MouseEvent((Component) event.getSource(), event.getID(), event.getWhen(),
                    event.getModifiers(), x, y, event.getXOnScreen(), event.getYOnScreen(), event.getClickCount(),
                    true, event.getButton());
            Point loc = fc.getToolTipLocation(e);
            if (loc != null) {
                loc.x += entryBounds.x;
                loc.y += entryBounds.y;
                return loc;
            }
        }
        return super.getToolTipLocation(event);
    }

}
