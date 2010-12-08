/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
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
    protected String                                 key;
    /** The cached text to display. */
    protected String                                 text;
    /** The name of the resource bundle to use. */
    protected String                                 bundleName;
    /** The cache of resource bundles for the active locale. */
    private static Hashtable<String, ResourceBundle> bundles = new Hashtable<String, ResourceBundle>();
    /** The cached application locale. */
    private static Locale                            locale  = ServiceLocator.get(ConfigurationManager.class).get()
                                                                     .getLocale();

    static {
        ServiceLocator.get(ConfigurationManager.class).get()
                .addPropertyChangeListener("locale", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        bundles.clear();
                        locale = (Locale) evt.getNewValue();
                    }
                });
    }

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
        try {
            text = getBundle().getString(key);
        } catch (MissingResourceException e) {
            text = "";
        }
    }

    /**
     * @return The resource bundle associated with this localized string in the current locale.
     */
    protected ResourceBundle getBundle()
    {
        if (bundles.get(bundleName) == null) {
            bundles.put(bundleName, ResourceBundle.getBundle(bundleName, locale));
        }
        return bundles.get(bundleName);
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
        return locale;
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
