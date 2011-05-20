/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import javax.swing.origamist.MessageBar;

/**
 * A message bar that allows multiple messages to be displayed at once, and allowing to remove the messages from it.
 * 
 * @author Martin Pecka
 */
public interface ExtendedMessageBar extends MessageBar
{

    /**
     * Show the given message. The passed key can be used to remove the message.
     * <p>
     * If a message with the same key exists, it will be removed first.
     * 
     * @param message The message to display. HTML is allowed.
     * @param key The key that can be used to remove the message. Don't pass <code>null</code>.
     */
    void showMessage(String message, Object key);

    /**
     * {@inheritDoc}
     * <p>
     * The given messageKey is used as the key for message removal.
     */
    @Override
    void showL7dMessage(String bundle, String messageKey);

    /**
     * Shows the given message loaded from resource bundle and transformed by {@link java.text.MessageFormat} with the
     * given arguments. The given messageKey can be used as key to remove the message.
     * <p>
     * HTML is allowed in the loaded string.
     * 
     * @param bundle The name of the resource bundle to load the key from.
     * @param messageKey The key of the message to show.
     * @param params The parameters to be passed to {@link java.text.MessageFormat}.
     */
    void showL7dMessage(String bundle, String messageKey, Object... params);

    /**
     * @param milis {@inheritDoc} Milis are also autocomputed if a negative value is given.
     */
    @Override
    void showMessage(String message, Integer milis);

    /**
     * {@inheritDoc}
     * <p>
     * The given messageKey is used as the key to remove this message. If a message with this key exists, it will be
     * first removed.
     * 
     * @param milis {@inheritDoc} Milis are also autocomputed if a negative value is given.
     */
    @Override
    void showL7dMessage(String bundle, String messageKey, Integer milis);

    /**
     * Shows the given message for the given time and then removes it. The passed key can be used to remove the message
     * before the timeout.
     * <p>
     * If a message with the given key exists, it will be first removed.
     * 
     * @param message The message to show. HTML is allowed.
     * @param milis The time the message will be displayed for (in miliseconds). If <code>null</code> or negative,
     *            autocompute the time from the length of the message.
     * @param key The key that can be used to remove the message. Don't pass <code>null</code>.
     */
    void showMessage(String message, Integer milis, final Object key);

    /**
     * Shows the given message loaded from resource bundle for the given time and then removes it.
     * <p>
     * The given messageKey is used as the key to remove this message. If a message with this key exists, it will be
     * first removed.
     * <p>
     * HTML is allowed in the loaded string.
     * 
     * @param bundle The name of the resource bundle to load the key from.
     * @param messageKey The key of the message to show.
     * @param milis The time the message will be displayed for (in miliseconds). If <code>null</code> or negative,
     *            autocompute the time from the length of the message.
     * @param params The parameters to be passed to {@link java.text.MessageFormat}..
     */
    void showL7dMessage(String bundle, String messageKey, Integer milis, Object... params);

    /**
     * Shows the given message loaded from resource bundle for the given time and then removes it.
     * <p>
     * If a message with the given key exists, it will be first removed.
     * <p>
     * HTML is allowed in the loaded string.
     * <p>
     * You have to pass an extra <code>(Object[])null</code> to the method as the last argument if you don't want to
     * provide any parameters.
     * 
     * @param bundle The name of the resource bundle to load the key from.
     * @param messageKey The key of the message to show.
     * @param milis The time the message will be displayed for (in miliseconds). If <code>null</code> or negative,
     *            autocompute the time from the length of the message.
     * @param key The key that can be used to remove the message. Don't pass <code>null</code>.
     * @param params The parameters to be passed to {@link java.text.MessageFormat}..
     */
    void showL7dMessage(String bundle, String messageKey, Integer milis, Object key, Object... params);

    /**
     * Remove the message for the specified key. If no message exists for the given key, a message equal to the key is
     * searched to be removed. If both methods fail, nothing happens.
     * 
     * @param key The key of the removed message.
     */
    void removeMessage(Object key);

}