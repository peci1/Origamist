/**
 * 
 */
package javax.swing.origamist;

/**
 * Interface defining a panel/statusbar able to show messages
 * 
 * @author Martin Pecka
 */
public interface MessageBar
{
    /**
     * Shows the given message
     * 
     * @param message The message to show
     */
    public void showMessage(String message);

    /**
     * Shows the given message for the given time and then clears the message
     * area
     * 
     * @param message The message to show
     * @param milis The time the message will be displayed for (in miliseconds)
     */
    public void showMessage(String message, int milis);
}
