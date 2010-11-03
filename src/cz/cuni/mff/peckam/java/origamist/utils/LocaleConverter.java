/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Locale;

/**
 * Provides conversion between Java Locale and XML language types.
 * 
 * @author Martin Pecka
 */
public class LocaleConverter
{
    /**
     * Parse Java Locale from a string used in XML.
     * 
     * @param s The string to parse.
     * @return locale The locale parsed from the string. Will return <code>null</code>, if the string is
     *         <code>null</code> or if it isn't a valid string generated by <code>LocaleConverter.print()</code>.
     */
    public static Locale parse(String s)
    {
        if (s == null)
            return null;
        if (!s.matches(".*-.*"))
            return null;
        return new Locale(s.replaceAll("-.*", ""), s.replaceAll(".*-", ""));
    }

    /**
     * Parse a string to be used in XML from the Java Locale
     * 
     * @param l The locale to parse.
     * @return The string representation of the locale. Will return <code>null</code>, if the given locale is
     *         <code>null</code>.
     */
    public static String print(Locale l)
    {
        if (l == null)
            return null;
        return l.getLanguage() + "-" + l.getCountry();
    }
}
