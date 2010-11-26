/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A string that keeps itself synchronized with the currently selected application locale.
 * 
 * @author Martin Pecka
 */
public class LocalizedString
{

    /** The key to search for in the resource bundle. */
    protected String key;
    /** The cached text to display. */
    protected String text;
    /** The name of the resource bundle to use. */
    protected String bundleName;

    /**
     * Create a localized string by reading <code>key</code> from a resource bundle with name <code>name</code>.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     */
    public LocalizedString(String bundleName, String key)
    {
        this.key = key;
        this.bundleName = bundleName;
        updateText();
        installLocaleListener();
    }

    /**
     * The locale has changed, so reload the text from the bundle.
     */
    protected void updateText()
    {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, getLocale());
        try {
            text = bundle.getString(key);
        } catch (MissingResourceException e) {
            text = "";
        }
    }

    /**
     * Return the currently active locale.
     * 
     * You can override this in child classes to reflect another locale. Then you should also override
     * {@link #installLocaleListener()}.
     * 
     * @return The currently active locale.
     */
    protected Locale getLocale()
    {
        return ServiceLocator.get(ConfigurationManager.class).get().getLocale();
    }

    /**
     * Add a listener to the active configuration that will call {@link #updateText()} every time the locale has
     * changed.
     * 
     * You can override this in child classes to reflect another locale. Then you should also override
     * {@link #getLocale()} .
     */
    protected void installLocaleListener()
    {
        ServiceLocator.get(ConfigurationManager.class).get()
                .addPropertyChangeListener("locale", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        updateText();
                    }
                });
    }

    @Override
    public String toString()
    {
        return text;
    }
}
