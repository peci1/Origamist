/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.Service;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocaleConverter;
import cz.cuni.mff.peckam.java.origamist.utils.UnitDimensionWithLabel;

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
        List<UnitDimensionWithLabel> papers = new LinkedList<UnitDimensionWithLabel>();

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

            int numPapers = prefs.getInt("papers.count", 0);
            for (int i = 0; i < numPapers; i++) {
                String prefix = "papers." + i + ".";

                String label = prefs.get(prefix + "label", "");

                double width = prefs.getDouble(prefix + "width", 0d);
                double height = prefs.getDouble(prefix + "height", 0d);

                Unit unit = Unit.values()[0];
                try {
                    unit = Enum.valueOf(Unit.class, prefs.get(prefix + "unit", unit.toString()));
                } catch (NullPointerException e) {} catch (IllegalArgumentException e) {}

                double refLength = prefs.getDouble(prefix + "refLength", 0d);

                Unit refUnit = null;
                try {
                    refUnit = Enum.valueOf(Unit.class, prefs.get(prefix + "unit", null));
                } catch (NullPointerException e) {} catch (IllegalArgumentException e) {}

                UnitDimension dim = new UnitDimension();
                dim.setWidth(width);
                dim.setHeight(height);
                dim.setUnit(unit);
                dim.setReference(refUnit, refLength);

                UnitDimensionWithLabel paper = new UnitDimensionWithLabel(dim, label);
                papers.add(paper);
            }
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

        configuration.getPapers().clear();
        configuration.getPapers().addAll(papers);
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

        List<UnitDimensionWithLabel> papers = configuration.getPapers();
        prefs.putInt("papers.count", papers.size());
        for (int i = 0; i < papers.size(); i++) {
            UnitDimensionWithLabel paper = papers.get(i);
            String prefix = "papers." + i + ".";
            prefs.put(prefix + "label", paper.getLabel());
            prefs.putDouble(prefix + "width", paper.getDimension().getWidth());
            prefs.putDouble(prefix + "height", paper.getDimension().getHeight());
            prefs.put(prefix + "unit", paper.getDimension().getUnit().toString());
            if (paper.getDimension().getReferenceLength() != null)
                prefs.putDouble(prefix + "refLength", paper.getDimension().getReferenceLength());
            if (paper.getDimension().getReferenceUnit() != null)
                prefs.put(prefix + "refUnit", paper.getDimension().getReferenceUnit().toString());
        }

        prefs.flush();
    }

}
