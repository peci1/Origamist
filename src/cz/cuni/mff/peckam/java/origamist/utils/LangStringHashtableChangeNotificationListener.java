/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Hashtable;
import java.util.Locale;

import cz.cuni.mff.peckam.java.origamist.common.LangString;

/**
 * Element adapter for LangString.
 * 
 * @author Martin Pecka
 */
public class LangStringHashtableChangeNotificationListener extends
        HashtableChangeNotificationListener<LangString, Locale, String>
{

    public LangStringHashtableChangeNotificationListener(Hashtable<Locale, String> table)
    {
        super(table, new HashtableElementAdapter<LangString, Locale, String>() {
            @Override
            public Locale getKey(LangString item)
            {
                return item.getLang();
            }

            @Override
            public String getValue(LangString item)
            {
                return item.getValue();
            }
        });
    }

}
