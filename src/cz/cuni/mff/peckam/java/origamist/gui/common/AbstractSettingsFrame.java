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

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocaleComparator;

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

        appLocaleComboBox = new JComboBox(moveSuggestedLocalesToTop(getLocales(), getAppSuggestedLocales()));
        appLocaleComboBox.setRenderer(new LocaleListCellRenderer());

        diagramLocaleComboBox = new JComboBox(moveSuggestedLocalesToTop(getLocales(), getDiagramSuggestedLocales()));
        diagramLocaleComboBox.setRenderer(new LocaleListCellRenderer());

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
     * @return All available locales, sorted alphabetically. If a locale exists in both no-country and country versions,
     *         only the country versions are preserved. If a locale is available only without a country, it is also
     *         preserved.
     */
    protected Vector<Locale> getLocales()
    {
        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, new LocaleComparator());

        Vector<Locale> newLocales = new Vector<Locale>(locales.length);

        for (int i = 0; i < locales.length; i++) {
            Locale l = locales[i];
            if (l.toString().length() > 2) {
                newLocales.add(l);
            } else {
                if (i < locales.length - 1) {
                    if (!l.getLanguage().equals(locales[i + 1].getLanguage()))
                        newLocales.add(l);
                } else {
                    newLocales.add(l);
                }
            }
        }

        return newLocales;
    }

    /**
     * Removes all locales that are in <code>suggested</code> from <code>locales</code> and inserts them at the
     * beginning of the vector.
     * 
     * @param locales The vector to do the operation on.
     * @param suggested The locales to be moved to the top.
     * @return The same instance that was given in <code>locales</code>.
     */
    protected Vector<Locale> moveSuggestedLocalesToTop(Vector<Locale> locales, LinkedHashSet<Locale> suggested)
    {
        locales.removeAll(suggested);
        locales.addAll(0, suggested);
        return locales;
    }

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
