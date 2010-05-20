/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * A listener to change notifications
 * 
 * @author Martin Pecka
 * @param <T> Type of the item that is to be changed
 */
public interface ChangeNotificationListener<T>
{
    /**
     * Called when the listened list gets modified
     * 
     * @param change The change that has happened
     */
    void changePerformed(ChangeNotification<T> change);
}
