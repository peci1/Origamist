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
    protected Object[] parameters = new Object[0];

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
        if (parameters != null)
            this.parameters = parameters;
        else
            this.parameters = new Object[0];
        updateText();
    }

    /**
     * Set the parameter with the given index.
     * 
     * @param index The index of the parameter in the list of parameters.
     * @param parameter The new value to set.
     * 
     * @throws ArrayIndexOutOfBoundsException If <code>index</code> is &lt;0 or is greather than or equal to the size of
     *             the parameters array that was last set in the constructor or in the
     *             {@link ParametrizedLocalizedString#setParameters(Object...)} method.
     */
    public void setParameter(int index, Object parameter) throws ArrayIndexOutOfBoundsException
    {
        parameters[index] = parameter;
        updateText();
    }

    /**
     * @return The array of parameters of the string. Never returns <code>null</code>.
     */
    public Object[] getParameters()
    {
        return parameters;
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
