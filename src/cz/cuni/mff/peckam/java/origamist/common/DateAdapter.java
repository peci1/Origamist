/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

/**
 * Adapter for converting XmlGregorianCalendar to java.util.Date().
 * 
 * @author Martin Pecka
 */
public class DateAdapter
{
    /**
     * Parse the date from the given string.
     * 
     * @param s The string to parse.
     * @return The date for the given string.
     * 
     * @throws IllegalArgumentException If string parameter does not conform to lexical value space defined in XML
     *             Schema Part 2: Datatypes for xsd:Date.
     */
    public static Date parseDate(String s)
    {
        return DatatypeConverter.parseDate(s).getTime();
    }

    /**
     * Convert the given date to a string.
     * 
     * @param dt The date to convert.
     * @return The date's string representation (must be parsable by {@link #parseDate(String)}).
     */
    public static String printDate(Date dt)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return DatatypeConverter.printDate(cal);
    }
}
