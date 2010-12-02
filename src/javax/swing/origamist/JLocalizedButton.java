/**
 * 
 */
package javax.swing.origamist;

import javax.swing.Icon;
import javax.swing.JButton;

import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * A button displaying locale-dependent text.
 * 
 * @author Martin Pecka
 */
public class JLocalizedButton extends JButton
{

    /** */
    private static final long serialVersionUID = -1529957672711729484L;

    /** The localized string to be displayed as the button's text. */
    protected LocalizedString string           = null;

    /**
     * Creates a button with initial localized text and an icon.
     * 
     * @param string The localized string to be displayed as the button's text
     * @param icon The Icon image to display on the button.
     */
    public JLocalizedButton(LocalizedString string, Icon icon)
    {
        super(string.toString(), icon);
        this.string = string;
    }

    /**
     * Creates a button with localized text.
     * 
     * @param string The localized string to be displayed as the button's text.
     */
    public JLocalizedButton(LocalizedString string)
    {
        this(string, null);
    }

    /**
     * Creates a button with initial localized text and an icon.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param icon The Icon image to display on the button.
     */
    public JLocalizedButton(String bundleName, String key, Icon icon)
    {
        this(new LocalizedString(bundleName, key), icon);
    }

    /**
     * Creates a button with localized text.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     */
    public JLocalizedButton(String bundleName, String key)
    {
        this(new LocalizedString(bundleName, key));
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
     * Set the localized string to be displayed as the button's text.
     * 
     * @param string The localized string to be displayed as the button's text.
     */
    public void setLocalizedString(LocalizedString string)
    {
        this.string = string;
        this.revalidate();
    }

    /**
     * @return The localized string to be displayed as the button's text.
     */
    public LocalizedString getLocalizedString()
    {
        return string;
    }

    /**
     * A button with localized OK text.
     * 
     * @author Martin Pecka
     */
    public static class OKButton extends JLocalizedButton
    {
        /** */
        private static final long serialVersionUID = -8182343469257052316L;

        public OKButton()
        {
            super("application", "buttons.ok");
        }
    }

    /**
     * A button with localized Cancel text.
     * 
     * @author Martin Pecka
     */
    public static class CancelButton extends JLocalizedButton
    {

        /** */
        private static final long serialVersionUID = 7038180210010076163L;

        public CancelButton()
        {
            super("application", "buttons.cancel");
        }
    }

    /**
     * A button with localized Apply text.
     * 
     * @author Martin Pecka
     */
    public static class ApplyButton extends JLocalizedButton
    {

        /** */
        private static final long serialVersionUID = -7039491954425811172L;

        public ApplyButton()
        {
            super("application", "buttons.apply");
        }
    }

    /**
     * A button with localized Yes text.
     * 
     * @author Martin Pecka
     */
    public static class YesButton extends JLocalizedButton
    {

        /** */
        private static final long serialVersionUID = 5884007570410317893L;

        public YesButton()
        {
            super("application", "buttons.yes");
        }
    }

    /**
     * A button with localized No text.
     * 
     * @author Martin Pecka
     */
    public static class NoButton extends JLocalizedButton
    {

        /** */
        private static final long serialVersionUID = -649338587069402719L;

        public NoButton()
        {
            super("application", "buttons.no");
        }
    }

}
