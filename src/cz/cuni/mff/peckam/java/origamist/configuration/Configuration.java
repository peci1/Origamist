/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.io.File;
import java.util.Locale;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.utils.PropertyChangeSource;

/**
 * A configuration of the program.
 * 
 * Properties that fire PropertyChangeEvent when they are changed:
 * locale
 * diagramLocale
 * lastExportPath
 * preferredUnit
 * 
 * @author Martin Pecka
 */
public class Configuration extends PropertyChangeSource
{

    /**
     * General locale of the program.
     */
    protected Locale locale         = Locale.getDefault();

    /**
     * The preferred locale for diagrams. If null, means that it is the same as
     * locale.
     */
    protected Locale diagramLocale  = null;

    /** The path that was last used for exporting a model/listing. */
    protected File   lastExportPath = null;

    /**
     * The preferred measurement unit. <code>null</code> means that the unit defined in the corresponding
     * {@link cz.cuni.mff.peckam.java.origamist.model.UnitDimension} should be used for printing.
     */
    protected Unit   preferredUnit  = null;

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

}
