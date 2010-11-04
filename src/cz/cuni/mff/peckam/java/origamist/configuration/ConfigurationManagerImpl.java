/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocaleConverter;

/**
 * A default implementation of a configuration manager that stores the configuration in Preferences.
 * 
 * @author Martin Pecka
 */
public class ConfigurationManagerImpl implements ConfigurationManager
{

    /** The active configuration. */
    protected Configuration configuration = null;

    /** The preferences node associated with this configuration manager. */
    protected Preferences   prefs         = null;

    /**
     * Load the configuration from the preferences. If the configuration is already loaded, this reloads the values from
     * the properties into the active configuration (and will fire appropriate <code>PropertyChangeEvent</code>s).
     */
    public void load()
    {
        if (configuration == null)
            configuration = new Configuration();

        Locale locale = null;
        Locale diagramLocale = null;

        try {
            prefs = Preferences.userNodeForPackage(ConfigurationManager.class);
            locale = LocaleConverter.parse(prefs.get("locale", null));
            diagramLocale = LocaleConverter.parse(prefs.get("diagramLocale", null));
        } catch (SecurityException e) {
            System.err
                    .println("Couldn't load configuration because of security constraints. Using default configuration.");
            // the default configuration will be loaded further, because all values will remain null
        }

        if (locale == null)
            locale = Locale.getDefault();
        configuration.setLocale(locale);

        if (diagramLocale == null)
            diagramLocale = Locale.getDefault();
        configuration.setDiagramLocale(diagramLocale);
    }

    @Override
    public Configuration get()
    {
        if (configuration == null)
            load();
        return configuration;
    }

    @Override
    public void persist() throws BackingStoreException
    {
        if (prefs == null || configuration == null)
            return;

        String tmp = LocaleConverter.print(configuration.getLocale());
        prefs.put("locale", tmp == null ? "" : tmp);

        tmp = LocaleConverter.print(configuration.getDiagramLocale());
        prefs.put("diagramLocale", tmp == null ? "" : tmp);

        prefs.flush();
    }

}
