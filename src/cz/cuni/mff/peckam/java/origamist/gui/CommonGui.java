/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import cz.cuni.mff.peckam.java.origamist.configuration.ConfigurationManagerImpl;
import cz.cuni.mff.peckam.java.origamist.services.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.JAXBOrigamiLoader;
import cz.cuni.mff.peckam.java.origamist.services.OrigamiLoader;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

/**
 * Common GUI elements for both the viewer and editor
 * 
 * @author Martin Pecka
 */
public abstract class CommonGui extends JApplet
{

    private static final long serialVersionUID = -9021667515698972438L;

    /**
     * The localized messages for the GUI classes. Normally this would be
     * private, but we want to use this bundle also in the derived classes.
     */
    protected ResourceBundle  messages         = null;

    /**
     * The common message formater that can be used over the GUI. It has its
     * locale set to the configured one
     */
    protected MessageFormat   format           = null;

    /**
     * Create an applet that is the base for both viewer and editor
     */
    public CommonGui()
    {
        // TODO get the configured locale; also listen to the configured locale
        // changes
        messages = ResourceBundle
                .getBundle("cz.cuni.mff.peckam.java.origamist.gui.Gui");
        // TODO also set the locale and listen for changes
        format = new MessageFormat("");
    }

    @Override
    public void init()
    {
        super.init();

        registerServices();

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
            System.err.println(messages
                    .getString("guiInitializationInterrupted"));
            throw new RuntimeException(messages.getString("runtimeException"),
                    e);
        } catch (InvocationTargetException e) {
            format.applyPattern(messages
                    .getString("guiInitializationException"));

            System.err.println(format.format(new Object[] { e.getCause() }));
            throw new RuntimeException(messages.getString("runtimeException"),
                    e.getCause());
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
        ServiceLocator.add(OrigamiLoader.class, new JAXBOrigamiLoader());
        ServiceLocator.add(ConfigurationManager.class,
                new ConfigurationManagerImpl());
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
