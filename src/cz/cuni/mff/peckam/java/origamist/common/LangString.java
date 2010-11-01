/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.util.Locale;

/**
 * A string with a locale assigned to it.
 * 
 * @author Martin Pecka
 */
public class LangString extends cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString
{

    /**
     * Generate a default LangString.
     */
    public LangString()
    {
        super();
    }

    public LangString(String s, Locale l)
    {
        super();
        this.setValue(s);
        this.setLang(l);
    }
}
