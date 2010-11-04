/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.configuration.ConfigurationManagerImpl;
import cz.cuni.mff.peckam.java.origamist.logging.GUIAppender;
import cz.cuni.mff.peckam.java.origamist.services.HashCodeAndEqualsHelperImpl;
import cz.cuni.mff.peckam.java.origamist.services.JAXBListingHandler;
import cz.cuni.mff.peckam.java.origamist.services.JAXBOrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.HashCodeAndEqualsHelper;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ListingHandler;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;

/**
 * Common GUI elements for both the viewer and editor.
 * 
 * Provided properties:
 * messages (protected property)
 * 
 * @author Martin Pecka
 */
public abstract class CommonGui extends JApplet
{

    private static final long serialVersionUID = -9021667515698972438L;

    /**
     * The localized messages for the common GUI classes.
     */
    protected ResourceBundle  messages         = null;

    /**
     * The common message formater that can be used over the GUI. It has its locale set to the configured one
     */
    protected MessageFormat   format           = null;

    @Override
    public void init()
    {
        super.init();

        registerServices();

        setupLoggers();

        final Configuration config = ServiceLocator.get(ConfigurationManager.class).get();

        final String bundleName = "cz.cuni.mff.peckam.java.origamist.gui.Gui";

        messages = ResourceBundle.getBundle(bundleName, config.getLocale());
        format = new MessageFormat("", config.getLocale());
        config.addPropertyChangeListener("locale", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (!messages.getLocale().equals(evt.getNewValue())) {
                    ResourceBundle oldMessages = messages;
                    messages = ResourceBundle.getBundle(bundleName, (Locale) evt.getNewValue());
                    CommonGui.this.firePropertyChange("messages", oldMessages, messages);
                    MessageFormat oldFormat = format;
                    format = new MessageFormat("", (Locale) evt.getNewValue());
                    CommonGui.this.firePropertyChange("format", oldFormat, format);
                }
            }
        });

        // to allow transparent JCanvas3D background
        System.setProperty("j3d.transparentOffScreen", "true");

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run()
                {
                    createComponents();
                    buildLayout();
                }
            });
        } catch (InterruptedException e) {
            Logger.getLogger("application").l7dlog(Level.FATAL, "guiInitializationInterrupted", e);
        } catch (InvocationTargetException e) {
            Logger.getLogger("application").l7dlog(Level.FATAL, "guiInitializationException", e.getCause());
        }
    }

    /**
     * Create and setup all the form components.
     */
    protected abstract void createComponents();

    /**
     * Setup the form layout.
     */
    protected abstract void buildLayout();

    /**
     * Register all common services.
     */
    protected void registerServices()
    {
        ServiceLocator.add(OrigamiHandler.class, new JAXBOrigamiHandler(this.getDocumentBase()));
        ServiceLocator.add(ListingHandler.class, new JAXBListingHandler());
        ServiceLocator.add(ConfigurationManager.class, new ConfigurationManagerImpl());
        ServiceLocator.add(HashCodeAndEqualsHelper.class, new HashCodeAndEqualsHelperImpl());
    }

    protected void setupLoggers()
    {
        BasicConfigurator.configure();

        LogManager.getRootLogger().addAppender(new GUIAppender(this));

        final Logger l = Logger.getLogger("application");
        l.setResourceBundle(ResourceBundle.getBundle("application", ServiceLocator.get(ConfigurationManager.class)
                .get().getLocale()));
        l.setLevel(Level.ALL);
        l.addAppender(new GUIAppender(this));
    }

    @Override
    public void start()
    {
        super.start();
    }

    @Override
    public void stop()
    {
        super.stop();
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}
