/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cz.cuni.mff.peckam.java.origamist.files.CategoriesContainer;
import cz.cuni.mff.peckam.java.origamist.files.Category;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.files.FilesContainer;
import cz.cuni.mff.peckam.java.origamist.files.HierarchicalComponent;
import cz.cuni.mff.peckam.java.origamist.files.Listing;

/**
 * A data model for displaying a Listing in a JTree.
 * 
 * @author Martin Pecka
 */
public class ListingTreeModel implements TreeModel
{

    /** The listing this model wraps. */
    protected Listing                 listing            = null;

    /** The list of tree model listeners. */
    protected List<TreeModelListener> treeModelListeners = new LinkedList<TreeModelListener>();

    public ListingTreeModel(Listing listing)
    {
        this.listing = listing;
    }

    @Override
    public Listing getRoot()
    {
        return listing;
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        if (index < 0)
            return null;

        if (!(parent instanceof CategoriesContainer) || !(parent instanceof FilesContainer))
            return null;

        CategoriesContainer cParent = (CategoriesContainer) parent;
        FilesContainer fParent = (FilesContainer) parent;

        int numCategories = (cParent.getCategories() == null ? 0 : cParent.getCategories().getCategory().size());
        int numFiles = (fParent.getFiles() == null ? 0 : fParent.getFiles().getFile().size());

        if (index >= numCategories + numFiles)
            return null;

        if (index < numCategories) {
            return cParent.getCategories().getCategory().get(index);
        } else {
            return fParent.getFiles().getFile().get(index - numCategories);
        }
    }

    @Override
    public int getChildCount(Object parent)
    {
        if (!(parent instanceof CategoriesContainer) || !(parent instanceof FilesContainer))
            return 0;

        CategoriesContainer cParent = (CategoriesContainer) parent;
        FilesContainer fParent = (FilesContainer) parent;

        int numCategories = (cParent.getCategories() == null ? 0 : cParent.getCategories().getCategory().size());
        int numFiles = (fParent.getFiles() == null ? 0 : fParent.getFiles().getFile().size());

        return numCategories + numFiles;
    }

    @Override
    public boolean isLeaf(Object node)
    {
        return node instanceof File;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        if (!(newValue instanceof File || newValue instanceof Category))
            return;

        fireTreeStructureChanged(new TreeModelEvent(this, path.getParentPath()));
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        if (parent == null || child == null)
            return -1;

        if (!(parent instanceof CategoriesContainer) || !(parent instanceof FilesContainer))
            return -1;

        if (!(child instanceof HierarchicalComponent))
            return -1;

        HierarchicalComponent hChild = (HierarchicalComponent) child;
        CategoriesContainer cParent = (CategoriesContainer) parent;
        FilesContainer fParent = (FilesContainer) parent;

        if (!parent.equals(hChild.getParent()))
            return -1;

        if (child instanceof File) {
            int numCategories = (cParent.getCategories() == null ? 0 : cParent.getCategories().getCategory().size());
            if (fParent.getFiles() == null)
                return -1;
            int index = fParent.getFiles().getFile().indexOf(child);
            return index == -1 ? -1 : index + numCategories;
        } else if (child instanceof Category) {
            if (cParent.getCategories() == null)
                return -1;
            return cParent.getCategories().getCategory().indexOf(child);
        } else {
            return -1;
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
        treeModelListeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
        treeModelListeners.remove(l);
    }

    public void fireTreeNodesChanged(TreeModelEvent e)
    {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e)
    {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e)
    {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e)
    {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeStructureChanged(e);
        }
    }

}
