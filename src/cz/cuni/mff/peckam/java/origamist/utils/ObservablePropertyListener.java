/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * A listener for changes in an observable property.
 * 
 * @author Martin Pecka
 */
public interface ObservablePropertyListener<T>
{
    /**
     * Perform actions on property change.
     * 
     * @param evt The event object describing the property change.
     */
    void changePerformed(ObservablePropertyEvent<? extends T> evt);
}
