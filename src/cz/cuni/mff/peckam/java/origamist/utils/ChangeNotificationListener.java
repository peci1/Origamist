/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * A listener to change notifications
 * 
 * @param T Type of the elements in the observed list.
 * 
 * @author Martin Pecka
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
