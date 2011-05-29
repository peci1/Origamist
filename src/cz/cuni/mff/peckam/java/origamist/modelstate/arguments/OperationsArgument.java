/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import cz.cuni.mff.peckam.java.origamist.gui.editor.OperationsTree;
import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.model.Operation;
import cz.cuni.mff.peckam.java.origamist.model.OperationContainer;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * Argument that fetches the selected operations.
 * 
 * @author Martin Pecka
 */
public class OperationsArgument extends OperationArgument implements OperationsTreeDataReceiver
{

    protected List<Operation> operations = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public OperationsArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public boolean isComplete()
    {
        return operations != null && operations.size() > 0;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return null;
    }

    public List<Operation> getOperations()
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return operations;
    }

    @Override
    public void readDataFromObject(OperationsTree tree)
    {
        if (tree.getSelectionCount() > 0) {

            operations = new LinkedList<Operation>();
            final Map<Operation, TreePath> paths = new HashMap<Operation, TreePath>();
            LinkedHashMap<OperationContainer, TreePath> selectedContainers = new LinkedHashMap<OperationContainer, TreePath>();

            for (TreePath path : tree.getSelectionPaths()) {
                Object userObject = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                if (!(userObject instanceof OperationContainer)) {
                    operations.add((Operation) userObject);
                    paths.put((Operation) userObject, path);
                } else {
                    selectedContainers.put((OperationContainer) userObject, path);
                }
            }
            for (Entry<OperationContainer, TreePath> entry : selectedContainers.entrySet()) {
                for (Operation o : entry.getKey().getOperations()) {
                    if (!operations.contains(o)) {
                        operations.add(o);
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) entry.getValue().getLastPathComponent();
                        TreeNode child = null;
                        @SuppressWarnings("unchecked")
                        Enumeration<TreeNode> enu = node.children();
                        while (enu.hasMoreElements()) {
                            TreeNode childNode = enu.nextElement();
                            if (((DefaultMutableTreeNode) childNode).getUserObject() == o) {
                                child = (DefaultMutableTreeNode) childNode;
                                break;
                            }
                        }
                        paths.put(o, entry.getValue().pathByAddingChild(child));
                    }
                }
            }

            TreeSelectionModel oldSelModel = tree.getSelectionModel();
            tree.setSelectionModel(new DefaultTreeSelectionModel());
            tree.getSelectionModel().setSelectionMode(DefaultTreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            tree.setSelectionPaths(paths.values().toArray(new TreePath[] {}));

            if (operations.size() == 0)
                return;

            Collections.sort(operations, new Comparator<Operation>() {
                @Override
                public int compare(Operation o1, Operation o2)
                {
                    TreePath path1 = paths.get(o1);
                    TreePath path2 = paths.get(o2);

                    while (path1.getPathCount() > path2.getPathCount())
                        path1 = path1.getParentPath();
                    while (path2.getPathCount() > path1.getPathCount())
                        path2 = path2.getParentPath();

                    while (path1.getPathCount() > 0
                            && path2.getPathCount() > 0
                            && path1.getParentPath().getLastPathComponent() != path2.getParentPath()
                                    .getLastPathComponent()) {
                        path1 = path1.getParentPath();
                        path2 = path2.getParentPath();
                    }

                    DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) path1.getLastPathComponent();
                    DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) path2.getLastPathComponent();
                    int index1 = node1.getParent().getIndex(node1);
                    int index2 = node2.getParent().getIndex(node2);
                    if (index1 < index2)
                        return -1;
                    else if (index1 > index2)
                        return 1;
                    else
                        return 0;
                }
            });

            ResourceBundle messages = ResourceBundle.getBundle("editor", ServiceLocator.get(ConfigurationManager.class)
                    .get().getLocale());

            if (JOptionPane.showConfirmDialog(null,
                    messages.getString("operation.argument.last.operations.confirm.message"),
                    messages.getString("operation.argument.last.operations.confirm.title"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                operations = null;
                tree.setSelectionModel(oldSelModel);
                return;
            }
            tree.setSelectionModel(oldSelModel);
            support.firePropertyChange(COMPLETE_PROPERTY, false, true);
        } else {
            operations = null;
        }
    }

    @Override
    public String getL7dUserTip()
    {
        return new LocalizedString(OperationArgument.class.getName(), "operations.user.tip").toString() + "<br/>"
                + super.getL7dUserTip();
    }

}