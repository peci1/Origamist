/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import java.util.Locale;

/**
 * A configuration of the program.
 * 
 * @author Martin Pecka
 */
public class Configuration
{

    /**
     * General locale of the program.
     */
    protected Locale locale        = Locale.getDefault();

    /**
     * The preferred locale for diagrams. If null, means that it is the same as
     * locale.
     */
    protected Locale diagramLocale = null;

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
        this.locale = locale;
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
        this.diagramLocale = diagramLocale;
    }

}
