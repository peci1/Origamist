/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.media.j3d.Canvas3D;
import javax.swing.origamist.JMultilineLabel;

import org.apache.log4j.Logger;
import org.w3c.tools.timers.EventHandler;
import org.w3c.tools.timers.EventManager;

import cz.cuni.mff.peckam.java.origamist.gui.common.OSDPanel;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * OSD panel for displaying help messages.
 * 
 * @author Martin Pecka
 */
public class HelpPanel extends OSDPanel implements ExtendedMessageBar
{

    /** The queue for timed actions. */
    protected final EventManager        eventManager = new EventManager();

    /** The label that displays the text. */
    protected JMultilineLabel           helpLabel    = new JMultilineLabel("");

    /** The currently displayed messages. */
    protected final List<String>        messages     = Collections.synchronizedList(new LinkedList<String>());

    /** The map that stores keys for the given messages. */
    protected final Map<Object, String> messageKeys  = Collections.synchronizedMap(new HashMap<Object, String>());

    /**
     * Construct a new help panel on the given canvas and with the given bounds.
     * 
     * @param canvas The canvas this panel is attached to.
     * @param x Top-left corner's x coordinate in px.
     * @param y Top-left corner's y coordinate in px.
     * @param width Width of the panel in px.
     * @param height Height of the panel in px.
     */
    public HelpPanel(Canvas3D canvas, int x, int y, int width, int height, boolean fromRight, boolean fromBottom)
    {
        super(canvas, x, y, width, height, fromRight, fromBottom);
        recreateHelpLabel();
        new Thread(eventManager).start();
    }

    /**
     * Recreate the help label.
     */
    protected void recreateHelpLabel()
    {
        helpLabel = new JMultilineLabel("");
        helpLabel.setSize(bounds.width, bounds.height);
        helpLabel.setOpaque(false);
        helpLabel.setFont(helpLabel.getFont().deriveFont(Font.PLAIN));
    }

    @Override
    public synchronized void showMessage(String message)
    {
        messages.add(0, message);
        repaint();
    }

    @Override
    public synchronized void showMessage(String message, Object key)
    {
        if (messageKeys.containsKey(key))
            removeMessage(key);
        messageKeys.put(key, message);
        showMessage(message);
    }

    @Override
    public synchronized void showL7dMessage(String bundle, String messageKey)
    {
        showMessage(new LocalizedString(bundle, messageKey).toString(), messageKey);
    }

    @Override
    public synchronized void showL7dMessage(String bundle, String messageKey, Object... params)
    {
        showMessage(MessageFormat.format(new LocalizedString(bundle, messageKey).toString(), params), messageKey);
    }

    @Override
    public synchronized void showMessage(String message, Integer milis)
    {
        messages.add(0, message);
        eventManager.registerTimer(milis != null && milis > 0 ? milis : getTimeout(message), new EventHandler() {
            @Override
            public void handleTimerEvent(Object data, long time)
            {
                synchronized (HelpPanel.this) {
                    messages.remove(data);
                }
                repaint();
            }
        }, message);
        repaint();
    }

    @Override
    public synchronized void showL7dMessage(String bundle, String messageKey, Integer milis)
    {
        showMessage(new LocalizedString(bundle, messageKey).toString(), milis, messageKey);
    }

    @Override
    public synchronized void showMessage(String message, Integer milis, final Object key)
    {
        if (messageKeys.containsKey(key))
            removeMessage(key);

        messageKeys.put(key, message);
        messages.add(0, message);
        eventManager.registerTimer(milis != null && milis > 0 ? milis : getTimeout(message), new EventHandler() {
            @Override
            public void handleTimerEvent(Object data, long time)
            {
                synchronized (HelpPanel.this) {
                    if (messageKeys.get(key) == data) {
                        String message = messageKeys.remove(key);
                        if (message != null) {
                            messages.remove(message);
                            repaint();
                        }
                    } // otherwise the message was removed manually
                }
            }
        }, message);
        repaint();
    }

    @Override
    public synchronized void showL7dMessage(String bundle, String messageKey, Integer milis, Object... params)
    {
        showL7dMessage(bundle, messageKey, milis, messageKey, params);
    }

    @Override
    public synchronized void showL7dMessage(String bundle, String messageKey, Integer milis, Object key,
            Object... params)
    {
        showMessage(MessageFormat.format(new LocalizedString(bundle, messageKey).toString(), params), milis, key);
    }

    @Override
    public synchronized void removeMessage(Object key)
    {
        String message = messageKeys.get(key);
        if (message != null) {
            messages.remove(message);
            messageKeys.remove(key);
        } else {
            messages.remove(key);
        }
        repaint();
    }

    @Override
    protected void paint(Graphics2D graphics)
    {
        if (helpLabel == null)
            recreateHelpLabel();

        graphics.setBackground(new Color(0, 0, 0, 0));
        graphics.clearRect(0, 0, bounds.width, bounds.height);

        updateLabelText();

        try {
            helpLabel.paint(graphics);
        } catch (Exception e) {
            try {
                recreateHelpLabel();
                updateLabelText();
                graphics.clearRect(0, 0, bounds.width, bounds.height);
                helpLabel.paint(graphics);
            } catch (Exception e2) {
                Logger.getLogger(getClass()).warn("Error while painting of the help panel's label.", e2);
            }
        }
    }

    /**
     * Update the text the label displays.
     */
    protected synchronized void updateLabelText()
    {
        if (messages == null)
            return;

        StringBuilder string = new StringBuilder("<html><head>");
        string.append("<style type=\"text/css\">");
        string.append("li {margin: 0px 0px 4px 0px;}");
        string.append("ul {list-style-type: none; padding: 0px; margin: 0px;}");
        string.append("</style></head>");
        string.append("<body><ul>");

        for (String message : messages) {
            string.append("<li>");
            if (message.matches("<[hH][tT][mM][lL]>.*")) {
                string.append(message.replaceAll("</*[hH][tT][mM][lL]>", "").replaceAll("</*[bB][oO][dD][yY]>", ""));
            } else {
                string.append(message.replaceAll("<", "&lt;"));
            }
            string.append("</li>");
        }

        string.append("</ul></body></html>");
        try {
            helpLabel.setText(string.toString());
        } catch (Exception e) {
            try {
                recreateHelpLabel();
                helpLabel.setText(string.toString());
            } catch (Exception e2) {
                Logger.getLogger(getClass()).warn("Error while setting text for help panel's label.", e2);
            }
        }
    }

    /**
     * Compute timeout of the given message based on its length.
     * 
     * @param message The message the timeout is computed for. If message contains HTML, ensure that all &lt; are
     *            properly encoded with &amp;lt; .
     * @return The timeout.
     */
    protected int getTimeout(String message)
    {
        // the first regex is for stripping HTML out of the text; the second one counts spaces
        return Math.max(2000, 200 * message.replaceAll("\\<[^>]*>", "").replaceAll("[^ ]", "").length());
    }
}
