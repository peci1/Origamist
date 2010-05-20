/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Locale;

/**
 * Provides conversion between Java Locale and XML language types
 * 
 * @author Martin Pecka
 */
public class LocaleConverter
{
    /**
     * Parse Java Locale from XML language
     * 
     * @param s
     * @return locale
     */
    public static Locale parse(String s)
    {
        return new Locale(s.replaceAll("-.*", ""), s.replaceAll(".*-", ""));
    }

    /**
     * Parse XML language from Java Locale
     * 
     * @param l
     * @return the string representation of the locale
     */
    public static String print(Locale l)
    {
        return l.getISO3Language() + "-" + l.getISO3Country();
    }
}
