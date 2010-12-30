/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.PropertyChangeSource;
import cz.cuni.mff.peckam.java.origamist.utils.UnitDimensionWithLabel;

/**
 * A configuration of the program.
 * 
 * Properties that fire PropertyChangeEvent when they are changed:
 * locale
 * diagramLocale
 * lastExportPath
 * preferredUnit
 * defaultAuthorName
 * defaultAuthorHomepage
 * 
 * @author Martin Pecka
 */
public class Configuration extends PropertyChangeSource
{

    /**
     * General locale of the program.
     */
    protected Locale                                 locale                = Locale.getDefault();

    /**
     * The preferred locale for diagrams. If null, means that it is the same as
     * locale.
     */
    protected Locale                                 diagramLocale         = null;

    /** The path that was last used for exporting a model/listing. */
    protected File                                   lastExportPath        = null;

    /** The path that was last used for opening a model/listing. */
    protected File                                   lastOpenPath          = null;

    /** The URL that was last used for opening a model/listing. */
    protected URL                                    lastOpenURL           = null;

    /**
     * The preferred measurement unit. <code>null</code> means that the unit defined in the corresponding
     * {@link cz.cuni.mff.peckam.java.origamist.model.UnitDimension} should be used for printing.
     */
    protected Unit                                   preferredUnit         = null;

    /** The name the author uses for diagrams created by him/her. */
    protected String                                 defaultAuthorName     = null;

    /** The homepage the author uses for diagrams created by him/her. */
    protected String                                 defaultAuthorHomepage = null;

    /** The list of user-defined papers. */
    protected ObservableList<UnitDimensionWithLabel> papers                = new ObservableList<UnitDimensionWithLabel>();

    /**
     * @return the locale
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale)
    {
        Locale oldLocale = this.locale;
        this.locale = locale;
        firePropertyChange("locale", oldLocale, locale);
    }

    /**
     * @return the diagramLocale
     */
    public Locale getDiagramLocale()
    {
        return diagramLocale == null ? locale : diagramLocale;
    }

    /**
     * @param diagramLocale the diagramLocale to set
     */
    public void setDiagramLocale(Locale diagramLocale)
    {
        Locale oldLocale = this.diagramLocale;
        this.diagramLocale = diagramLocale;
        firePropertyChange("diagramLocale", oldLocale, diagramLocale);
    }

    /**
     * @return the lastExportPath
     */
    public File getLastExportPath()
    {
        return lastExportPath;
    }

    /**
     * @param lastExportPath the lastExportPath to set
     */
    public void setLastExportPath(File lastExportPath)
    {
        File oldPath = this.lastExportPath;
        this.lastExportPath = lastExportPath;
        firePropertyChange("lastExportPath", oldPath, lastExportPath);
    }

    /**
     * @return the lastOpenPath
     */
    public File getLastOpenPath()
    {
        return lastOpenPath;
    }

    /**
     * @param lastOpenPath the lastOpenPath to set
     */
    public void setLastOpenPath(File lastOpenPath)
    {
        File oldPath = this.lastOpenPath;
        this.lastOpenPath = lastOpenPath;
        firePropertyChange("lastOpenPath", oldPath, lastOpenPath);
    }

    /**
     * @return the lastOpenURL
     */
    public URL getLastOpenURL()
    {
        return lastOpenURL;
    }

    /**
     * @param lastOpenURL the lastOpenURL to set
     */
    public void setLastOpenURL(URL lastOpenURL)
    {
        URL oldURL = this.lastOpenURL;
        this.lastOpenURL = lastOpenURL;
        firePropertyChange("lastOpenURL", oldURL, lastOpenURL);
    }

    /**
     * @return The preferred measurement unit. <code>null</code> means that the unit defined in the corresponding
     *         {@link cz.cuni.mff.peckam.java.origamist.model.UnitDimension} should be used for printing.
     */
    public Unit getPreferredUnit()
    {
        return preferredUnit;
    }

    /**
     * @param preferredUnit The preferred measurement unit. <code>null</code> means that the unit defined in the
     *            corresponding {@link cz.cuni.mff.peckam.java.origamist.model.UnitDimension} should be used for
     *            printing.
     */
    public void setPreferredUnit(Unit preferredUnit)
    {
        Unit oldUnit = this.preferredUnit;
        this.preferredUnit = preferredUnit;
        firePropertyChange("preferredUnit", oldUnit, preferredUnit);
    }

    /**
     * @return the defaultAuthorName
     */
    public String getDefaultAuthorName()
    {
        return defaultAuthorName;
    }

    /**
     * @param defaultAuthorName the defaultAuthorName to set
     */
    public void setDefaultAuthorName(String defaultAuthorName)
    {
        String oldName = this.defaultAuthorName;
        this.defaultAuthorName = defaultAuthorName;
        firePropertyChange("defaultAuthorName", oldName, defaultAuthorName);
    }

    /**
     * @return the defaultAuthorHomepage
     */
    public String getDefaultAuthorHomepage()
    {
        return defaultAuthorHomepage;
    }

    /**
     * @param defaultAuthorHomepage the defaultAuthorHomepage to set
     */
    public void setDefaultAuthorHomepage(String defaultAuthorHomepage)
    {
        String oldHomepage = this.defaultAuthorHomepage;
        this.defaultAuthorHomepage = defaultAuthorHomepage;
        firePropertyChange("defaultAuthorHomepage", oldHomepage, defaultAuthorHomepage);
    }

    /**
     * @return the papers
     */
    public ObservableList<UnitDimensionWithLabel> getPapers()
    {
        return papers;
    }

    /**
     * Adds a {@link ResourceBundleListener} as a {@link PropertyChangeListener} and runs it with the current locale.
     * 
     * @param l The listener to add.
     */
    public void addAndRunResourceBundleListener(ResourceBundleListener l)
    {
        addPropertyChangeListener(l.getPropertyName(), l);
        l.propertyChange(new PropertyChangeEvent(this, l.getPropertyName(), null, l.getProperty()));
    }

    /**
     * A listener that calls <code>updateText</code> for the given <code>key</code> from a <code>resourceBundle</code>
     * whenever the locale <code>propertyName</code> changes.
     * 
     * @author Martin Pecka
     */
    public abstract static class ResourceBundleListener implements PropertyChangeListener
    {
        /** The bundle to monitor. */
        protected String         bundleName;
        /** The key from the monitored bundle. */
        protected String         key;
        /** The name of the property of the monitored {@link Locale}. */
        protected String         propertyName;

        /** The actual resource bundle. */
        protected ResourceBundle bundle;

        /** Configuration for static access "hacking". */
        protected Configuration  config = ServiceLocator.get(ConfigurationManager.class).get();

        /**
         * @param bundleName The bundle to monitor.
         * @param key The key from the monitored bundle.
         * @param propertyName The name of the property of the monitored {@link Locale}.
         */
        protected ResourceBundleListener(String bundleName, String key, String propertyName)
        {
            this.bundleName = bundleName;
            this.key = key;
            this.propertyName = propertyName;
        }

        /**
         * @return The name of the property of the monitored {@link Locale}.
         */
        public String getPropertyName()
        {
            return propertyName;
        }

        /**
         * @return The monitored {@link Locale}.
         */
        public abstract Locale getProperty();

        /**
         * Called when the resource bundle changes.
         * 
         * @param text The translated text.
         */
        protected abstract void updateText(String text);

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            bundle = ResourceBundle.getBundle(bundleName, getProperty());
            updateText(bundle.getString(key));
        }
    }

    /**
     * A listener monitoring changes to <code>locale</code> and reading <code>key</code> from a
     * <code>resourceBundle</code> whenever the locale changes.
     * 
     * @author Martin Pecka
     */
    public abstract static class LocaleListener extends ResourceBundleListener
    {

        /**
         * @param bundleName The bundle to monitor.
         * @param key The key from the monitored bundle.
         */
        protected LocaleListener(String bundleName, String key)
        {
            super(bundleName, key, "locale");
        }

        @Override
        public Locale getProperty()
        {
            return config.getLocale();
        }

    }

}
