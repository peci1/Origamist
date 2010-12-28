/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.Service;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocaleConverter;

/**
 * A default implementation of a configuration manager that stores the configuration in Preferences.
 * 
 * @author Martin Pecka
 */
public class ConfigurationManagerImpl extends Service implements ConfigurationManager
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
        File lastExportPath = null;
        File lastOpenPath = null;
        URL lastOpenURL = null;
        Unit preferredUnit = null;
        String defaultAuthorName = null;
        String defaultAuthorHomepage = null;

        try {
            prefs = Preferences.userNodeForPackage(ConfigurationManager.class);
            locale = LocaleConverter.parse(prefs.get("locale", null));
            diagramLocale = LocaleConverter.parse(prefs.get("diagramLocale", null));
            lastExportPath = new File(prefs.get("lastExportPath", System.getProperty("user.dir")));
            lastOpenPath = new File(prefs.get("lastOpenPath", System.getProperty("user.dir")));
            try {
                lastOpenURL = new URL(prefs.get("lastOpenURL", "."));
            } catch (MalformedURLException e1) {
                try {
                    lastOpenURL = new URL(".");
                } catch (MalformedURLException e) {}
            }
            try {
                preferredUnit = Enum.valueOf(Unit.class, prefs.get("preferredUnit", null));
            } catch (NullPointerException e) {} catch (IllegalArgumentException e) {}

            defaultAuthorName = prefs.get("defaultAuthorName", System.getProperty("user.name", ""));
            defaultAuthorHomepage = prefs.get("defaultAuthorHomepage", "");
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

        configuration.setLastExportPath(lastExportPath);
        configuration.setLastOpenPath(lastOpenPath);
        configuration.setLastOpenURL(lastOpenURL);

        configuration.setPreferredUnit(preferredUnit);
        configuration.setDefaultAuthorName(defaultAuthorName);
        configuration.setDefaultAuthorHomepage(defaultAuthorHomepage);
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

        prefs.put("lastExportPath", configuration.getLastExportPath().toString());
        prefs.put("lastOpenPath", configuration.getLastOpenPath().toString());
        if (configuration.getLastOpenURL() == null) {
            prefs.remove("lastOpenURL");
        } else {
            prefs.put("lastOpenURL", configuration.getLastOpenURL().toString());
        }

        if (configuration.getPreferredUnit() == null) {
            prefs.remove("preferredUnit");
        } else {
            prefs.put("preferredUnit", configuration.getPreferredUnit().toString());
        }

        prefs.put("defaultAuthorName", configuration.getDefaultAuthorName());
        prefs.put("defaultAuthorHomepage", configuration.getDefaultAuthorHomepage());

        prefs.flush();
    }

}
