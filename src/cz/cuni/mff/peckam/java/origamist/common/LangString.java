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
public class LangString extends cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString implements Cloneable
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

    @Override
    public String toString()
    {
        return getLang().toString() + ":" + getValue();
    }

    @Override
    public LangString clone()
    {
        return new LangString(this.value, this.lang);
    }
}
