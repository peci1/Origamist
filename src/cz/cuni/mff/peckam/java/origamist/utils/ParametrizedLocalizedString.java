/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

/**
 * A string that keeps itself synchronized with the currently selected application locale. The string can be
 * parametrized by some localization parameters.
 * 
 * @author Martin Pecka
 */
public class ParametrizedLocalizedString extends LocalizedString
{

    /** The parameters of the string. */
    protected Object[] parameters = null;

    /**
     * Create a localized string by reading <code>key</code> from a resource bundle with name <code>name</code> and
     * applying <code>params</code> as the localization parameters.
     * 
     * @param bundleName The name of the resource bundle (the bundle will be retrieved by calling
     *            <code>ResourceBundle.getBundle(bundleName, locale)</code>)
     * @param key The key to search for in the resource bundle.
     * @param params The parameters to set for this string.
     */
    public ParametrizedLocalizedString(String bundleName, String key, Object... params)
    {
        super(bundleName, key);
        setParameters(params);
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Object... parameters)
    {
        this.parameters = parameters;
        updateText();
    }

    @Override
    protected void updateText()
    {
        // this will happen when calling super() in constructor
        if (parameters == null) {
            text = "";
            return;
        }

        // will set the pattern into this.text
        super.updateText();

        try {
            MessageFormat format = new MessageFormat(this.text, getLocale());
            try {
                this.text = format.format(parameters);
            } catch (IllegalArgumentException e) {
                this.text = "";
                Logger.getLogger("application").warn(
                        "Illegal parameters to produce parametrized string from a resource with key " + key, e);
            }
        } catch (IllegalArgumentException e) {
            this.text = "";
            Logger.getLogger("application").warn(
                    "Illegal pattern syntax for a parametrized string from a resource with key " + key, e);
        }
    }
}
