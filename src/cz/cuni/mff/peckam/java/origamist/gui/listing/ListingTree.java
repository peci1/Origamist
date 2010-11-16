/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A JTree displaying the list of loaded files and categories.
 * 
 * @author Martin Pecka
 */
public class ListingTree extends JTree
{

    /** */
    private static final long serialVersionUID           = 7977020048548617471L;

    /**
     * The expanded nodes. A hashtable would serve better, but we workaround the unability to use hashcode() of the
     * changing nodes.
     */
    protected List<TreePath>  expanded                   = new LinkedList<TreePath>();

    /** If false, do not fire TreeExpansionListeners' events. */
    protected boolean         fireTreeExpansionListeners = true;

    public ListingTree(Listing listing)
    {
        super(new ListingTreeModel(listing));
        setToolTipText("");
        TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setSelectionModel(selectionModel);
        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new ListingTreeCellRenderer());
        addTreeSelectionListener(new ListingTreeSelectionListener());
        addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event)
            {
                if (!fireTreeExpansionListeners)
                    return;
                expanded.add(event.getPath());
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event)
            {
                expanded.remove(event.getPath());
            }
        });
        ServiceLocator.get(ConfigurationManager.class).get()
                .addPropertyChangeListener("diagramLocale", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        invalidate();
                    }
                });
    }

    /**
     * Expands all paths that were expanded before the model reload.
     */
    public void restoreExpanded()
    {
        fireTreeExpansionListeners = false;
        for (TreePath path : expanded) {
            expandPath(path);
        }
        fireTreeExpansionListeners = true;
    }

    @Override
    public String getToolTipText(MouseEvent event)
    {
        if (getRowForLocation(event.getX(), event.getY()) == -1)
            return null;
        TreePath curPath = getPathForLocation(event.getX(), event.getY());
        Object comp = curPath.getLastPathComponent();
        if (comp instanceof File) {
            FileRenderer fc = (FileRenderer) getCellRenderer().getTreeCellRendererComponent(this, comp, false, false,
                    true, 0, false);
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
            FileRenderer fc = (FileRenderer) getCellRenderer().getTreeCellRendererComponent(this, comp, false, false,
                    true, 0, false);
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
