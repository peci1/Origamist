/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.origamist.JLocalizedButton;
import javax.swing.origamist.UnitListCellRenderer;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * The window with the settings of the viewer or editor.
 * 
 * @author Martin Pecka
 */
public abstract class AbstractSettingsFrame extends JDialog
{
    /** */
    private static final long        serialVersionUID = 7682128196176312776L;

    /** Combobox for selecting the language of the application. */
    protected final JComboBox        appLocaleComboBox;

    /** Combobox for selecting the default language of displayed origamis. */
    protected final JComboBox        diagramLocaleComboBox;

    /** Combobox for selecting the default measurement unit. */
    protected final JComboBox        preferredUnitComboBox;

    /** Button for applying the changes. */
    protected final JLocalizedButton okBtn;

    /** Button for cancelling the changes. */
    protected final JLocalizedButton cancelBtn;

    public AbstractSettingsFrame()
    {
        PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                setTitle(ResourceBundle.getBundle("application", (Locale) evt.getNewValue())
                        .getString("settings.title"));
            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("locale", l);
        l.propertyChange(new PropertyChangeEvent(this, "locale", null, ServiceLocator.get(ConfigurationManager.class)
                .get().getLocale()));

        appLocaleComboBox = new JLocaleComboBox(getAppSuggestedLocales());
        diagramLocaleComboBox = new JLocaleComboBox(getDiagramSuggestedLocales());

        preferredUnitComboBox = new JComboBox(getUnits());
        preferredUnitComboBox.setRenderer(new UnitListCellRenderer());
        preferredUnitComboBox.setSelectedItem(ServiceLocator.get(ConfigurationManager.class).get().getPreferredUnit());

        okBtn = new JLocalizedButton.OKButton();

        cancelBtn = new JLocalizedButton.CancelButton();
        cancelBtn.setAction(new CancelAction());

        buildLayout();

        pack();

        setModalityType(ModalityType.APPLICATION_MODAL);
    }

    /**
     * Adds the components to this frame.
     */
    protected abstract void buildLayout();

    /**
     * @return The locales suggested for the application.
     */
    protected LinkedHashSet<Locale> getAppSuggestedLocales()
    {
        LinkedHashSet<Locale> result = new LinkedHashSet<Locale>();

        result.add(ServiceLocator.get(ConfigurationManager.class).get().getLocale());
        result.add(ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale());
        result.add(Locale.getDefault());

        return result;
    }

    /**
     * @return The locales suggested for the displayed origamis.
     */
    protected abstract LinkedHashSet<Locale> getDiagramSuggestedLocales();

    /**
     * Return all defined units and place a <code>null</code> item at the beginning of the list.
     * 
     * @return All defined units and place a <code>null</code> item at the beginning of the list.
     */
    protected Vector<Unit> getUnits()
    {
        Vector<Unit> units = new Vector<Unit>(Arrays.asList(Unit.values()));
        units.add(0, null);
        return units;
    }

    /**
     * Action to cancel changes made in this dialog and close it.
     * 
     * @author Martin Pecka
     */
    class CancelAction extends AbstractAction
    {

        /** */
        private static final long serialVersionUID = 487038857956049431L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setVisible(false);
        }

    }
}
