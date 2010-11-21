/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.logging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.gui.CommonGui;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * This appender writes the appMessages to the console. FATAL appMessages are also shown as error popups to the user.
 * 
 * By default, it uses System.err and a PatternLayout.
 * 
 * @author Martin Pecka
 */
public class GUIAppender extends ConsoleAppender
{

    /** The localized appMessages for the "application" bundle. */
    protected ResourceBundle messages = null;

    /** The message formater that can be used. It has its locale set to the configured one. */
    protected MessageFormat  format   = null;

    /** The GUI we can use to show error popups. */
    protected CommonGui      gui      = null;

    /**
     * Use PatternLayout and System.err.
     * 
     * @param gui The GUI we can use to show error popups.
     */
    public GUIAppender(CommonGui gui)
    {
        this(gui, new PatternLayout(), ConsoleAppender.SYSTEM_ERR);
    }

    /**
     * @param gui The GUI we can use to show error popups.
     * @param layout The layout to use.
     * @param target Either SYSTEM_OUT or SYSTEM_ERR.
     */
    public GUIAppender(CommonGui gui, Layout layout, String target)
    {
        super(layout, target);
        this.gui = gui;

        final Configuration config = ServiceLocator.get(ConfigurationManager.class).get();
        messages = ResourceBundle.getBundle("application", config.getLocale());
        format = new MessageFormat("", config.getLocale());
        config.addPropertyChangeListener("locale", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (!messages.getLocale().equals(evt.getNewValue())) {
                    messages = ResourceBundle.getBundle("application", (Locale) evt.getNewValue());
                    format = new MessageFormat("", (Locale) evt.getNewValue());
                }
            }
        });
    }

    /**
     * Use SYSTEM_ERR as the default target.
     * 
     * @param gui The GUI we can use to show error popups.
     * @param layout The layout to use.
     */
    public GUIAppender(CommonGui gui, Layout layout)
    {
        this(gui, layout, ConsoleAppender.SYSTEM_ERR);
    }

    @Override
    public synchronized void doAppend(LoggingEvent event)
    {
        super.doAppend(event);
        if (event.getLevel().isGreaterOrEqual(Level.FATAL)) {
            final Throwable t = event.getThrowableInformation() == null ? null : event.getThrowableInformation()
                    .getThrowable();
            String desc = this.layout.format(event);
            final String desc2 = desc != null ? desc : messages.getString("unknownError");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    format.applyPattern(messages.getString("dieWithException"));
                    JOptionPane.showMessageDialog(gui.getRootPane(),
                            format.format(new String[] { desc2, t == null ? "" : t.toString() }),
                            messages.getString("dieWithExceptionTitle"), JOptionPane.ERROR_MESSAGE);
                    gui.stop();
                }
            });
        }
    }

}
