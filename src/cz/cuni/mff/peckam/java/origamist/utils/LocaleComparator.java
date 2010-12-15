package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Comparator;
import java.util.Locale;

/**
 * A comparator of locales based on their textual representation.
 * 
 * @author Martin Pecka
 */
public class LocaleComparator implements Comparator<Locale>
{
    @Override
    public int compare(Locale o1, Locale o2)
    {
        if (o1 == null) {
            if (o2 == null)
                return 0;
            else
                return -1;
        } else {
            if (o2 == null) {
                return 1;
            } else {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        }
    }
}