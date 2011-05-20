/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
public class LastOperationsArgument extends OperationArgument implements OperationsTreeDataReceiver
{

    /** The list of operations. */
    protected List<Operation> operations = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public LastOperationsArgument(boolean required, String resourceBundleKey)
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

    /**
     * @return The selected operations.
     */
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
            for (TreePath path : tree.getSelectionPaths()) {
                if (path.getPathCount() != 2) // only select topmost nodes under the root
                    continue;
                Object userObject = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                if (!(userObject instanceof OperationContainer)) {
                    operations.add((Operation) userObject);
                    paths.put((Operation) userObject, path);
                }
            }
            tree.setSelectionPaths(paths.values().toArray(new TreePath[] {}));

            if (operations.size() == 0)
                return;

            Collections.sort(operations, new Comparator<Operation>() {
                @Override
                public int compare(Operation o1, Operation o2)
                {
                    DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) paths.get(o1).getLastPathComponent();
                    DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) paths.get(o2).getLastPathComponent();
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
            Operation last = operations.get(operations.size() - 1);
            DefaultMutableTreeNode lastSelected = (DefaultMutableTreeNode) paths.get(last).getLastPathComponent();
            int index = lastSelected.getParent().getIndex(lastSelected);
            if (index >= lastSelected.getParent().getChildCount() - 2) {
                if (JOptionPane.showConfirmDialog(null,
                        messages.getString("operation.argument.last.operations.confirm.message"),
                        messages.getString("operation.argument.last.operations.confirm.title"),
                        JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                    operations = null;
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        messages.getString("operation.argument.last.operations.only.last.message"),
                        messages.getString("operation.argument.last.operations.only.last.title"),
                        JOptionPane.ERROR_MESSAGE);
                operations = null;
                return;
            }
            support.firePropertyChange(COMPLETE_PROPERTY, false, true);
        } else {
            operations = null;
        }
    }

    @Override
    public String getL7dUserTip()
    {
        return new LocalizedString(OperationArgument.class.getName(), "last.operations.user.tip").toString() + "<br/>"
                + super.getL7dUserTip();
    }

}
