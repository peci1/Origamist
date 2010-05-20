/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.awt.Color;

/**
 * Provides conversion between Java Color and XML Color types
 * 
 * @author Martin Pecka
 */
public class ColorConverter
{

    /**
     * Parse Java Color from XML Color
     * 
     * @param s
     * @return color
     */
    public static Color parse(String s)
    {
        return new Color(Integer.parseInt(s.substring(1, 3), 16), Integer
                .parseInt(s.substring(3, 5), 16), Integer.parseInt(s.substring(
                5, 7), 16));
    }

    /**
     * Parse XML Color from Java Color
     * 
     * @param c
     * @return the string representation of the color
     */
    public static String print(Color c)
    {
        return String.format("#%x%x%x", c.getRed(), c.getGreen(), c.getBlue());
    }

}
