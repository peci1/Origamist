/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import javax.swing.Icon;
import javax.swing.origamist.JLocalizedLabel;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * A localized label displaying its text even in its tooltip, decorated by the TooltipFactory plain method.
 * 
 * @author Martin Pecka
 */
public class JLocalizedLabelWithTooltip extends JLocalizedLabel
{

    /** */
    private static final long serialVersionUID = -6673079012781387068L;

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text, image, and
     * horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * The text is on the trailing edge of the image.
     * 
     * All constructors refer to this one.
     * 
     * @param string The localized string to display.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(LocalizedString string, Icon icon, int horizontalAlignment)
    {
        super(string, icon, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text and horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * 
     * @param string The localized string to display.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(LocalizedString string, int horizontalAlignment)
    {
        super(string, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text, image, and
     * horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * The text is on the trailing edge of the image.
     * 
     * All constructors refer to this one.
     * 
     * @param string The localized string to display.
     * @param stringFormat The string to be used in String.format() with the localized string as the 1st argument. If
     *            <code>null</code> or not a valid one-argument pattern, only the localized string is shown.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(LocalizedString string, String stringFormat, Icon icon, int horizontalAlignment)
    {
        super(string, stringFormat, icon, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text and horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * 
     * @param string The localized string to display.
     * @param stringFormat The string to be used in String.format() with the localized string as the 1st argument. If
     *            <code>null</code> or not a valid one-argument pattern, only the localized string is shown.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(LocalizedString string, String stringFormat, int horizontalAlignment)
    {
        super(string, stringFormat, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text.
     * The label is aligned against the leading edge of its display area, and centered vertically.
     * 
     * @param string The localized string to display.
     * @param stringFormat The string to be used in String.format() with the localized string as the 1st argument. If
     *            <code>null</code> or not a valid one-argument pattern, only the localized string is shown.
     */
    public JLocalizedLabelWithTooltip(LocalizedString string, String stringFormat)
    {
        super(string, stringFormat);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text.
     * The label is aligned against the leading edge of its display area, and centered vertically.
     * 
     * @param string The localized string to display.
     */
    public JLocalizedLabelWithTooltip(LocalizedString string)
    {
        super(string);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text, image, and
     * horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * The text is on the trailing edge of the image.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(String bundleName, String key, Icon icon, int horizontalAlignment)
    {
        super(bundleName, key, icon, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text and horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(String bundleName, String key, int horizontalAlignment)
    {
        super(bundleName, key, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text, image, and
     * horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * The text is on the trailing edge of the image.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param stringFormat The string to be used in String.format() with the localized string as the 1st argument. If
     *            <code>null</code> or not a valid one-argument pattern, only the localized string is shown.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(String bundleName, String key, String stringFormat, Icon icon,
            int horizontalAlignment)
    {
        super(bundleName, key, stringFormat, icon, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text and horizontal
     * alignment.
     * The label is centered vertically in its display area.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param stringFormat The string to be used in String.format() with the localized string as the 1st argument. If
     *            <code>null</code> or not a valid one-argument pattern, only the localized string is shown.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabelWithTooltip(String bundleName, String key, String stringFormat, int horizontalAlignment)
    {
        super(bundleName, key, stringFormat, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text.
     * The label is aligned against the leading edge of its display area, and centered vertically.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param stringFormat The string to be used in String.format() with the localized string as the 1st argument. If
     *            <code>null</code> or not a valid one-argument pattern, only the localized string is shown.
     */
    public JLocalizedLabelWithTooltip(String bundleName, String key, String stringFormat)
    {
        super(bundleName, key, stringFormat);
    }

    /**
     * Creates a <code>JLocalizedLabelWithTooltip</code> instance with the specified localized text.
     * The label is aligned against the leading edge of its display area, and centered vertically.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     */
    public JLocalizedLabelWithTooltip(String bundleName, String key)
    {
        super(bundleName, key);
    }

    @Override
    protected String decorateTooltipText(String text)
    {
        return ServiceLocator.get(TooltipFactory.class).getPlain(text);
    }

}
