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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.configuration.ConfigurationManagerImpl;
import cz.cuni.mff.peckam.java.origamist.services.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.JAXBListingLoader;
import cz.cuni.mff.peckam.java.origamist.services.JAXBOrigamiLoader;
import cz.cuni.mff.peckam.java.origamist.services.ListingLoader;
import cz.cuni.mff.peckam.java.origamist.services.OrigamiLoader;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

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
                    format = new MessageFormat("", (Locale) evt.getNewValue());
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
            dieWithException(e, messages.getString("guiInitializationInterrupted"));
        } catch (InvocationTargetException e) {
            dieWithException(e.getCause(), messages.getString("guiInitializationException"));
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
        ServiceLocator.add(OrigamiLoader.class, new JAXBOrigamiLoader(this.getDocumentBase()));
        ServiceLocator.add(ListingLoader.class, new JAXBListingLoader());
        ServiceLocator.add(ConfigurationManager.class, new ConfigurationManagerImpl());
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

    /**
     * Presents the given exception to the user and ends the program.
     * 
     * @param e The exception that was thrown.
     * @param desc The human-readable description of the exception.
     */
    protected void dieWithException(final Throwable throwable, final String desc)
    {
        final String desc2;
        if (desc != null)
            desc2 = desc;
        else
            desc2 = messages.getString("unknownError");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                System.err.println(desc + ":\n" + throwable + "\nCause:\n" + throwable.getCause());
                format.applyPattern(messages.getString("dieWithException"));
                JOptionPane.showMessageDialog(getRootPane(),
                        format.format(new String[] { desc2, throwable.toString() }),
                        messages.getString("dieWithExceptionTitle"), JOptionPane.ERROR_MESSAGE);
                stop();
            }
        });
    }
}
