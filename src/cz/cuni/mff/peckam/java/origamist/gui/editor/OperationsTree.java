/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import cz.cuni.mff.peckam.java.origamist.model.Operation;
import cz.cuni.mff.peckam.java.origamist.model.OperationContainer;
import cz.cuni.mff.peckam.java.origamist.utils.MutableHashMap;

/**
 * The tree that displays operations.
 * 
 * @author Martin Pecka
 */
public class OperationsTree extends JTree
{

    /** */
    private static final long                                   serialVersionUID   = 6867603232306832702L;

    /** The map that maps operations to their corresponding tree nodes. */
    protected MutableHashMap<Operation, DefaultMutableTreeNode> nodesForOperations = new MutableHashMap<Operation, DefaultMutableTreeNode>();

    {
        setRootVisible(false);
        setShowsRootHandles(true);
        setSelectionModel(new DefaultTreeSelectionModel());
        getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
    }

    /**
     * 
     */
    public OperationsTree()
    {
        super();
    }

    /**
     * @param newModel
     */
    public OperationsTree(TreeModel newModel)
    {
        super(newModel);
    }

    /**
     * @param root
     */
    public OperationsTree(TreeNode root)
    {
        super(root);
    }

    /**
     * Get the tree model that represents the given list of operations.
     * 
     * @param operations The operations to be represented.
     * @return The tree model representing the given operations.
     */
    public TreeModel getModelForOperations(List<Operation> operations)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setAllowsChildren(true);
        createOperationsStructure(root, operations);
        return new DefaultTreeModel(root);
    }

    /**
     * Recursively add the given list of operations under the given root.
     * 
     * @param root The tree node to add the operations under.
     * @param operations The operations to add.
     */
    protected void createOperationsStructure(DefaultMutableTreeNode root, List<Operation> operations)
    {
        if (operations != null) {
            for (Operation o : operations) {
                DefaultMutableTreeNode node = createNode(o);
                node.setAllowsChildren(false);
                root.add(node);
                if (o instanceof OperationContainer) {
                    node.setAllowsChildren(true);
                    createOperationsStructure(node, ((OperationContainer) o).getOperations());
                }
            }
        }
    }

    /**
     * Create the node for the given operation.
     * 
     * @param operation The operation to create the node for.
     * @return The node.
     */
    protected DefaultMutableTreeNode createNode(Operation operation)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(operation);
        if (operation instanceof OperationContainer) {
            node.setAllowsChildren(true);
        } else {
            node.setAllowsChildren(false);
        }
        nodesForOperations.put(operation, node);
        return node;
    }

    /**
     * Remove the given operation from this tree.
     * 
     * @param operation The operation to remove.
     */
    public void removeOperation(Operation operation)
    {
        MutableTreeNode node = nodesForOperations.forcedGet(operation);
        if (node != null) {
            nodesForOperations.forcedRemove(operation);
            ((DefaultTreeModel) getModel()).removeNodeFromParent(node);
            repaint();
        }
    }

    /**
     * Add the operation under the tree's top level (just under the single root).
     * 
     * @param operation The operation to add.
     */
    public void addTopLevelOperation(Operation operation)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
        DefaultMutableTreeNode node = createNode(operation);
        ((DefaultTreeModel) getModel()).insertNodeInto(node, root, root.getChildCount());
        addSelectionPath(new TreePath(node.getPath()));
        repaint();
    }

    /**
     * Return the node that holds the given operation.
     * 
     * @param operation The operation to find node for.
     * @return The node for the operation.
     */
    public TreeNode getNodeForOperation(Operation operation)
    {
        return nodesForOperations.forcedGet(operation);
    }

    /**
     * Scroll the tree so that the given operation's node is visible.
     * <p>
     * Only works if the tree is under a {@link javax.swing.JScrollPane}.
     * 
     * @param operation The operation to make visible.
     */
    public void scrollToOperation(Operation operation)
    {
        DefaultMutableTreeNode node = nodesForOperations.forcedGet(operation);
        if (node != null)
            scrollPathToVisible(new TreePath(node.getPath()));
    }

    /**
     * Force recomputing of the cached cell heights.
     */
    public void recomputeHeights()
    {
        // changing the model is one of the easiest ways to recompute the cell heights
        TreeModel model = getModel();
        int[] selected = getSelectionRows();

        setModel(new DefaultTreeModel(null));
        setModel(model);

        setSelectionRows(selected);
    }

}
