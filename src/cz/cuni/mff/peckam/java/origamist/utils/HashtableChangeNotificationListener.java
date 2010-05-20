/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Hashtable;
import java.util.Locale;

import cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString;

/**
 * This change notification listener projects the changes in the given
 * hashtable.
 * 
 * @author Martin Pecka
 */
public class HashtableChangeNotificationListener implements
        ChangeNotificationListener<LangString>
{

    /**
     * The Hashtable bound to this listener.
     */
    protected Hashtable<Locale, String> table = null;

    /**
     * @param table The Hashtable bound to this listener
     */
    public HashtableChangeNotificationListener(Hashtable<Locale, String> table)
    {
        this.table = table;
    }

    @Override
    public void changePerformed(ChangeNotification<LangString> change)
    {
        switch (change.getChangeType()) {
            case ADD:
                table.put(change.getItem().getLang(), change.getItem()
                        .getValue());
                break;
            case CHANGE:
                table.remove(change.getOldItem().getLang());
                table.put(change.getItem().getLang(), change.getItem()
                        .getValue());
                break;
            case REMOVE:
                table.remove(change.getItem().getLang());
                break;
        }
    }

}
