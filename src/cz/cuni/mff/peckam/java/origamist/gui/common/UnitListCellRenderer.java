package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import cz.cuni.mff.peckam.java.origamist.model.UnitHelper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * A renderer to display available units.
 * 
 * @author Martin Pecka
 */
public class UnitListCellRenderer extends DefaultListCellRenderer
{
    /** Cache for the localized names of the units. */
    protected final Hashtable<Unit, LocalizedString> unitLabels;

    /**
     * The label for <code>null</code> unit (to show the unit set in the
     * {@link cz.cuni.mff.peckam.java.origamist.model.UnitDimension}).
     */
    protected final LocalizedString                  nullUnitLabel;

    /**
     * @param settingsFrame
     */
    public UnitListCellRenderer()
    {
        nullUnitLabel = new LocalizedString("application", "units.default");
        unitLabels = new Hashtable<Unit, LocalizedString>(Unit.values().length);
        for (Unit u : Unit.values()) {
            unitLabels.put(u, UnitHelper.getUnitDescription(u, true));
        }
    }

    /** */
    private static final long serialVersionUID = 8031715960897791362L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null) {
            setText(nullUnitLabel.toString());
        } else {
            setText(unitLabels.get(value).toString());
        }
        return result;
    }
}