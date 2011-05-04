/**
 * 
 */
package javax.swing.origamist;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 * A simple statusbar with customizable regions.
 * 
 * @author Martin Pecka
 */
public class JStatusBar extends JPanel implements MessageBar
{

    /** Needed for serialization */
    private static final long               serialVersionUID  = -2772216384360313722L;

    /**
     * Name of the default area
     */
    public static final String              DEFAULT_AREA_NAME = "default";

    /**
     * The label used for displaying status messages in default area
     */
    protected JLabel                        defaultLabel      = new JLabel();

    /** The text set by a non-timed-out showMessage on the default area. */
    protected String                        fixedDefaultText  = " ";

    /** The messages with timeout to be displayed. */
    protected Queue<TimedOutMessage>        timedOutMessages  = new LinkedList<TimedOutMessage>();

    /**
     * Areas of the statusbar (always contains an area called DEFAULT_AREA_NAME
     * with a label)
     */
    protected Hashtable<String, JComponent> areas             = new Hashtable<String, JComponent>();

    /**
     * The border all areas will have
     */
    protected Border                        areaBorder        = BorderFactory.createEtchedBorder();

    /**
     * Sets the border all areas will have
     * 
     * @param border The border all areas will have
     */
    public void setAreaBorder(Border border)
    {
        areaBorder = border;
    }

    /**
     * Timer used for displaying timed messages.
     */
    protected final Timer timer = new Timer(2000, null);

    {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel defaultArea = new JPanel();
        defaultArea.setAlignmentX(LEFT_ALIGNMENT);
        defaultArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, defaultArea.getMaximumSize().height));
        defaultArea.setLayout(new BoxLayout(defaultArea, BoxLayout.LINE_AXIS));

        defaultLabel.setAlignmentX(LEFT_ALIGNMENT);
        defaultLabel.setFont(defaultLabel.getFont().deriveFont(11f).deriveFont(Font.PLAIN));
        defaultArea.add(defaultLabel);

        addArea(DEFAULT_AREA_NAME, defaultArea);

        timer.setRepeats(false);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (timedOutMessages.isEmpty()) {
                    defaultLabel.setText(fixedDefaultText);
                } else {
                    TimedOutMessage message = timedOutMessages.poll();
                    timer.setInitialDelay(message.milis);
                    defaultLabel.setText(message.message);
                    timer.start();
                }
            }
        });
    }

    /**
     * Adds a new area to the statusbar
     * 
     * You can use area.setAlignmentX() to set its position in the statusbar
     * 
     * @param name Name of the area
     * @param area The area to add
     */
    public synchronized void addArea(String name, JComponent area)
    {
        if (areas.contains(name))
            return;

        if (areas.size() > 0) {
            int maxHeight = 0;
            for (JComponent a : areas.values()) {
                if (a.getPreferredSize().height > maxHeight)
                    maxHeight = a.getPreferredSize().height;
            }

            if (area.getPreferredSize().height < maxHeight) {
                area.setPreferredSize(new Dimension(area.getPreferredSize().width, maxHeight));
            } else if (area.getPreferredSize().height > maxHeight) {
                for (JComponent a : areas.values()) {
                    a.setPreferredSize(new Dimension(a.getPreferredSize().width, area.getPreferredSize().height));
                }
            }
        }

        area.setBorder(areaBorder);
        area.setAlignmentY(TOP_ALIGNMENT);

        areas.put(name, area);
        add(area);
    }

    /**
     * Removes area of the given name
     * 
     * @param name Name of the area to be removed
     */
    public synchronized void removeArea(String name)
    {
        // one cannot delete the default area
        if (name.equals(DEFAULT_AREA_NAME))
            return;

        remove(areas.get(name));
        areas.remove(name);
    }

    /**
     * @param name Name of the area you want
     * @return The area of the given name or null if it does not exist
     */
    public synchronized JComponent getArea(String name)
    {
        return areas.get(name);
    }

    /**
     * @return The default area
     */
    public synchronized JComponent getDefaultArea()
    {
        return getArea(DEFAULT_AREA_NAME);
    }

    @Override
    public synchronized void showMessage(String message)
    {
        if (message == null)
            return;

        if (getDefaultArea().isAncestorOf(defaultLabel)) {
            fixedDefaultText = message;
            if (!timer.isRunning()) {
                defaultLabel.setText(message);
            }
        }
    }

    @Override
    public synchronized void showMessage(String message, Integer milis)
    {
        if (message == null)
            return;

        int timeout;
        if (milis != null)
            timeout = milis;
        else
            timeout = getTimeout(message);

        if (!timer.isRunning()) {
            timer.setInitialDelay(timeout);
            defaultLabel.setText(message);
            timer.start();
        } else {
            timedOutMessages.add(new TimedOutMessage(message, timeout));
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

    /**
     * A message string with timeout information.
     * 
     * @author Martin Pecka
     */
    protected class TimedOutMessage
    {
        /** The message to display. */
        protected String message;
        /** The number of miliseconds the message has to be displayed for. */
        protected int    milis;

        /**
         * @param message The message to display.
         * @param milis The number of miliseconds the message has to be displayed for.
         */
        public TimedOutMessage(String message, int milis)
        {
            this.message = message;
            this.milis = milis;
        }
    }
}