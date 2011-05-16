/**
 * 
 */
package javax.swing.origamist;

/**
 * Interface defining a panel/statusbar able to show messages.
 * 
 * @author Martin Pecka
 */
public interface MessageBar
{
    /**
     * Shows the given message.
     * 
     * @param message The message to show.
     */
    public void showMessage(String message);

    /**
     * Shows the given message loaded from resource bundle.
     * 
     * @param bundle The name of the resource bundle to load the key from.
     * @param messageKey The key of the message to show.
     */
    public void showL7dMessage(String bundle, String messageKey);

    /**
     * Shows the given message for the given time and then clears the message area.
     * 
     * @param message The message to show.
     * @param milis The time the message will be displayed for (in miliseconds). If <code>null</code>, autocompute the
     *            time from the length of the message.
     */
    public void showMessage(String message, Integer milis);

    /**
     * Shows the message from the given bundle for the given time and then clears the message area.
     * 
     * @param bundle The name of the resource bundle to load the key from.
     * @param messageKey The key of the message to show.
     * @param milis The time the message will be displayed for (in miliseconds). If <code>null</code>, autocompute the
     *            time from the length of the message.
     */
    public void showL7dMessage(String bundle, String messageKey, Integer milis);
}
