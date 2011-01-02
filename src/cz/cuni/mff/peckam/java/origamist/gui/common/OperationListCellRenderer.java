/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import cz.cuni.mff.peckam.java.origamist.model.Operation;
import cz.cuni.mff.peckam.java.origamist.model.OperationsHelper;

/**
 * A renderer for operations in a step.
 * 
 * @author Martin Pecka
 */
public class OperationListCellRenderer extends DefaultListCellRenderer
{

    /** */
    private static final long serialVersionUID = -8983928724421088263L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        JLabel result = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // TODO improve the value of the displayed information

        Operation operation = (Operation) value;
        result.setText(OperationsHelper.toString(operation.getType()));

        return result;
    }

}
