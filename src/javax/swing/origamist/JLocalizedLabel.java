/**
 * 
 */
package javax.swing.origamist;

import javax.swing.Icon;
import javax.swing.JLabel;

import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * A label displaying locale-dependent text.
 * 
 * @author Martin Pecka
 */
public class JLocalizedLabel extends JLabel
{

    /** */
    private static final long serialVersionUID = -7689745537418512427L;

    /** The localized string to display. */
    protected LocalizedString string;

    /**
     * Creates a <code>JLocalizedLabel</code> instance with the specified localized text, image, and horizontal
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
    public JLocalizedLabel(LocalizedString string, Icon icon, int horizontalAlignment)
    {
        super(string.toString(), icon, horizontalAlignment);
        this.string = string;
    }

    /**
     * Creates a <code>JLocalizedLabel</code> instance with the specified localized text and horizontal alignment.
     * The label is centered vertically in its display area.
     * 
     * @param string The localized string to display.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabel(LocalizedString string, int horizontalAlignment)
    {
        this(string, null, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabel</code> instance with the specified localized text.
     * The label is aligned against the leading edge of its display area, and centered vertically.
     * 
     * @param string The localized string to display.
     */
    public JLocalizedLabel(LocalizedString string)
    {
        this(string, null, LEADING);
    }

    /**
     * Creates a <code>JLocalizedLabel</code> instance with the specified localized text, image, and horizontal
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
    public JLocalizedLabel(String bundleName, String key, Icon icon, int horizontalAlignment)
    {
        this(new LocalizedString(bundleName, key), icon, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabel</code> instance with the specified localized text and horizontal alignment.
     * The label is centered vertically in its display area.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param horizontalAlignment One of the following constants
     *            defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>,
     *            <code>LEADING</code> or <code>TRAILING</code>.
     */
    public JLocalizedLabel(String bundleName, String key, int horizontalAlignment)
    {
        this(new LocalizedString(bundleName, key), null, horizontalAlignment);
    }

    /**
     * Creates a <code>JLocalizedLabel</code> instance with the specified localized text.
     * The label is aligned against the leading edge of its display area, and centered vertically.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     */
    public JLocalizedLabel(String bundleName, String key)
    {
        this(new LocalizedString(bundleName, key), null, LEADING);
    }

    @Override
    public String getText()
    {
        if (string == null)
            return "";
        return string.toString();
    }

    @Override
    public void setText(String text)
    {
    }

    /**
     * Set the localized string to be displayed by this label.
     * 
     * @param string The localized string to be displayed by this label.
     */
    public void setLocalizedString(LocalizedString string)
    {
        this.string = string;
        this.revalidate();
    }

    /**
     * @return The localized string that is being displayed by this label.
     */
    public LocalizedString getLocalizedString()
    {
        return string;
    }

}