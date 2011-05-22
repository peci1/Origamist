/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.awt.Insets;
import java.util.Locale;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * Options for PNG export.
 * 
 * @author Martin Pecka
 */
public class PNGExportOptions implements ExportOptions
{
    /** Dpi of the paper. */
    protected double  dpi            = 72;

    /** Locale of the step descriptions. */
    protected Locale  locale         = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();

    /** Whether to draw the diagram's background. */
    protected boolean withBackground = true;

    /** The insets of the page in pixels. */
    protected Insets  pageInsets     = new Insets(25, 25, 25, 25);

    /**
     * @param dpi The dots per inch value of the exported paper.
     * @param locale Locale of the step descriptions.
     * @param insets The insets of the page in pixels.
     * @param withBackground Whether to draw the diagram's background.
     */
    public PNGExportOptions(double dpi, Locale locale, Insets pageInsets, boolean withBackground)
    {
        this.dpi = dpi;
        this.locale = locale;
        this.pageInsets = pageInsets;
        this.withBackground = withBackground;
    }

    /**
     * @param dpi The dots per inch value of the exported paper.
     * @param locale Locale of the step descriptions.
     * @param insets The insets of the page in pixels.
     */
    public PNGExportOptions(double dpi, Locale locale, Insets pageInsets)
    {
        this.dpi = dpi;
        this.locale = locale;
        this.pageInsets = pageInsets;
    }

    /**
     * @param dpi The dots per inch value of the exported paper.
     * @param locale Locale of the step descriptions.
     */
    public PNGExportOptions(double dpi, Locale locale)
    {
        this.dpi = dpi;
        this.locale = locale;
    }

    /**
     * @param dpi The dots per inch value of the exported paper.
     * @param insets The insets of the page in pixels.
     */
    public PNGExportOptions(double dpi, Insets pageInsets)
    {
        this.dpi = dpi;
        this.pageInsets = pageInsets;
    }

    /**
     * @param locale Locale of the step descriptions.
     * @param insets The insets of the page in pixels.
     */
    public PNGExportOptions(Locale locale, Insets pageInsets)
    {
        this.locale = locale;
        this.pageInsets = pageInsets;
    }

    /**
     * @param dpi The dots per inch value of the exported paper.
     */
    public PNGExportOptions(double dpi)
    {
        this.dpi = dpi;
    }

    /**
     * @param locale Locale of the step descriptions.
     */
    public PNGExportOptions(Locale locale)
    {
        this.locale = locale;
    }

    /**
     * @param insets The insets of the page in pixels.
     */
    public PNGExportOptions(Insets pageInsets)
    {
        this.pageInsets = pageInsets;
    }

    /**
     */
    public PNGExportOptions()
    {
    }

    /**
     * @return The dpi.
     */
    public double getDpi()
    {
        return dpi;
    }

    /**
     * @param dpi The dpi to set.
     */
    public void setDpi(double dpi)
    {
        this.dpi = dpi;
    }

    /**
     * @return Locale of the step descriptions.
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * @param locale Locale of the step descriptions.
     */
    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    @Override
    public Insets getPageInsets()
    {
        return pageInsets;
    }

    /**
     * @param pageInsets The insets of the page in pixels.
     */
    public void setPageInsets(Insets pageInsets)
    {
        this.pageInsets = pageInsets;
    }

    /**
     * @return Whether to draw the diagram's background.
     */
    public boolean isWithBackground()
    {
        return withBackground;
    }

    /**
     * @param withBackground Whether to draw the diagram's background.
     */
    public void setWithBackground(boolean withBackground)
    {
        this.withBackground = withBackground;
    }

}
