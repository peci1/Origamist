/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.origamist.LocaleListCellRenderer;

import cz.cuni.mff.peckam.java.origamist.utils.LocaleComparator;

/**
 * A combo box displaying available locales.
 * 
 * @author Martin Pecka
 */
public class JLocaleComboBox extends JComboBox
{

    /** */
    private static final long       serialVersionUID = -4604148799954218486L;

    /** The cache of all available system locales. */
    protected static Vector<Locale> allLocales       = null;

    /**
     * 
     */
    public JLocaleComboBox()
    {
        this(null);
    }

    /**
     * @param suggested Locales that will be displayed at the top positions in the locales list.
     */
    public JLocaleComboBox(LinkedHashSet<Locale> suggested)
    {
        updateSuggested(suggested);

        setRenderer(new LocaleListCellRenderer());
    }

    /**
     * Updates the order of items to move <code>suggested</code> to the top.
     * 
     * @param suggested Locales that will be displayed at the top positions in the locales list.
     */
    public void updateSuggested(LinkedHashSet<Locale> suggested)
    {
        Object selected = getSelectedItem();

        if (suggested == null) {
            setModel(new DefaultComboBoxModel(getLocales()));
        } else {
            setModel(new DefaultComboBoxModel(moveSuggestedLocalesToTop(getLocales(), suggested)));
        }

        setSelectedItem(selected);
    }

    /**
     * @return All available locales, sorted alphabetically. If a locale exists in both no-country and country versions,
     *         only the country versions are preserved. If a locale is available only without a country, it is also
     *         preserved.
     */
    @SuppressWarnings("unchecked")
    protected Vector<Locale> getLocales()
    {
        if (allLocales == null) {

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

            allLocales = newLocales;
        }

        return (Vector<Locale>) allLocales.clone();
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
}
