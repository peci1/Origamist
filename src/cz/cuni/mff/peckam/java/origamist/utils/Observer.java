/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * An observer of changes.
 * 
 * @param T Type of the elements in the observed list.
 * 
 * @author Martin Pecka
 */
public interface Observer<T>
{
    /**
     * Called when the observed object gets modified
     * 
     * @param change The change that has happened.
     */
    void changePerformed(ChangeNotification<T> change);
}