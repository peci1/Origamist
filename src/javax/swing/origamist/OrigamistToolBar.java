/**
 * 
 */
package javax.swing.origamist;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A toolbar that has convenience methods for adding buttons with locale-dependent values.
 * 
 * @author Martin Pecka
 */
public class OrigamistToolBar extends JToolBar
{

    /** */
    private static final long serialVersionUID = 7248513028554332970L;

    /** The application localization texts. */
    protected ResourceBundle  messages;

    /** The name of the resource bundle this toolbar will fetch the localizations from. */
    protected String          bundleName;

    /**
     * @param bundleName The name of the resource bundle this toolbar will fetch the localizations from.
     */
    public OrigamistToolBar(String bundleName)
    {
        super();
        this.bundleName = bundleName;
        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                ResourceBundle oldAppMessages = messages;
                messages = ResourceBundle.getBundle(OrigamistToolBar.this.bundleName, (Locale) evt.getNewValue());
                if (oldAppMessages == null || !oldAppMessages.equals(messages))
                    firePropertyChange("messages", oldAppMessages, messages);
            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("locale", l);
        l.propertyChange(new PropertyChangeEvent(this, "locale", null, ServiceLocator.get(ConfigurationManager.class)
                .get().getLocale()));
    }

    /**
     * Create a normal toolbar button.
     * 
     * @param action The action the button should invoke.
     * @param bundleName The base of the resource bundle strings used to configure this button.
     * @param iconName The name that will be appended to the path <code>/resources/images/</code> to find the icon.
     * @return A normal toolbar button.
     */
    public JButton createToolbarButton(Action action, final String bundleName, final String iconName)
    {
        return createToolbarItem(new JButton(), action, bundleName, iconName);
    }

    /**
     * Create a toolbar dropdown button.
     * 
     * @param action The action the button should invoke when clicked on the main area.
     * @param bundleName The base of the resource bundle strings used to configure this button.
     * @param iconName The name that will be appended to the path <code>/resources/images/</code> to find the icon.
     * @return A toolbar dropdown button.
     */
    public JDropDownButton createToolbarDropdownButton(Action action, final String bundleName, final String iconName)
    {
        return createToolbarItem(new JDropDownButton(new JButton()), action, bundleName, iconName);
    }

    /**
     * Create a toolbar dropdown button.
     * 
     * @param button The button to use for the item.
     * @param action The action the button should invoke when clicked on the main area.
     * @param bundleName The base of the resource bundle strings used to configure this button.
     * @param iconName The name that will be appended to the path <code>/resources/images/</code> to find the icon.
     * @return A toolbar dropdown button.
     */
    public <T extends JDropDownButton> T createToolbarDropdownButton(T button, Action action, final String bundleName,
            final String iconName)
    {
        return createToolbarItem(button, action, bundleName, iconName);
    }

    /**
     * Create a toolbar dropdown button's item.
     * 
     * @param action The action the item should invoke.
     * @param bundleName The base of the resource bundle strings used to configure this button.
     * @param iconName The name that will be appended to the path <code>/resources/images/</code> to find the icon.
     * @return A toolbar dropdown button's item.
     */
    public JMenuItem createToolbarDropdownItem(Action action, final String bundleName, final String iconName)
    {
        return createToolbarDropdownItem(new JMenuItem(), action, bundleName, iconName);
    }

    /**
     * Create a toolbar dropdown button's item.
     * 
     * @param item The menu item to setup.
     * @param action The action the item should invoke.
     * @param bundleName The base of the resource bundle strings used to configure this button.
     * @param iconName The name that will be appended to the path <code>/resources/images/</code> to find the icon.
     * @return A toolbar dropdown button's item.
     */
    public <T extends AbstractButton> T createToolbarDropdownItem(T item, Action action, final String bundleName,
            final String iconName)
    {
        item.setBorder(BorderFactory.createCompoundBorder(item.getBorder(), BorderFactory.createEmptyBorder(3, 0, 3, 0)));
        return createToolbarItem(item, action, bundleName, iconName);
    }

    /**
     * Configure the given AbstractButton to fire the given action, load its data from the resources beginning with
     * <code>bundleName</code> and load the icon from <code>/resources/images/</code>, if <code>iconName</code> is not
     * <code>null</code>.
     * 
     * These bundle name suffixes are recognized:
     * <dl>
     * <dt>"" (empty suffix):</dt>
     * <dd>the text of the button</dd>
     * <dt>.hideText</dt>
     * <dd>if set (to any other value than "false"), don't display the text in the toolbar</dd>
     * <dt>.tooltip:</dt>
     * <dd>the tooltip for the button</dd>
     * <dt>.mnemonic:</dt>
     * <dd>the mnemonic for the button (the letter that will appear underlined in the button's text)</dd>
     * <dt>.accelerator:</dt>
     * <dd>the accelerator to be used with this button; if mnemonic is not set, use the keyCode of this accelerator as
     * mnemonic</dd>
     * </dl>
     * 
     * @param <T> The type of the button.
     * @param button The button to configure.
     * @param action The action the button should invoke.
     * @param bundleName The base of the resource bundle strings used to configure this button.
     * @param iconName The name that will be appended to the path <code>/resources/images/</code> to find the icon.
     * @return The button given in <code>button</code> param, configured.
     */
    public <T extends AbstractButton> T createToolbarItem(final T button, final Action action, final String bundleName,
            final String iconName)
    {
        if (bundleName == null)
            throw new NullPointerException("Tried to create toolbar item without giving the corresponding bundle name.");

        button.setOpaque(false);

        button.setAction(action);
        if (iconName != null) {
            URL url = getClass().getResource("/resources/images/" + iconName);
            if (url != null)
                button.setIcon(new ImageIcon(url));
        }

        final AbstractButton btn;
        if (button instanceof JDropDownButton) {
            btn = ((JDropDownButton) button).getMainButton();
        } else if (button instanceof JMenuItem) {
            btn = null;
        } else {
            btn = button;
        }

        final PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                boolean hideText = false;
                try {
                    String hide = messages.getString(bundleName + ".hideText");
                    if (!"false".equals(hide))
                        hideText = true;
                } catch (MissingResourceException e) {}

                try {
                    if (!hideText)
                        button.setText(messages.getString(bundleName));
                    else
                        button.setText("");
                } catch (MissingResourceException e) {
                    button.setText("");
                }

                if (btn != null) {
                    if (btn.getText() == null || btn.getText().length() == 0) {
                        btn.setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        btn.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                }

                String mnemonic = null;
                try {
                    mnemonic = messages.getString(bundleName + ".mnemonic");
                } catch (MissingResourceException e) {}
                String accelerator = null;
                try {
                    accelerator = messages.getString(bundleName + ".accelerator");
                } catch (MissingResourceException e) {}

                KeyStroke accStroke = KeyStroke.getKeyStroke(accelerator);
                KeyStroke mnemStroke = KeyStroke.getKeyStroke(mnemonic);
                if (mnemStroke == null)
                    mnemStroke = accStroke;

                if (evt.getOldValue() != null && (evt.getOldValue() instanceof ResourceBundle)) {
                    try {
                        String oldAcc = ((ResourceBundle) evt.getOldValue()).getString(bundleName + ".accelerator");
                        KeyStroke oldStroke = KeyStroke.getKeyStroke(oldAcc);
                        if (oldStroke != null) {
                            getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(oldStroke);
                        }
                    } catch (MissingResourceException e) {}
                }

                if (accStroke != null) {
                    if ((button instanceof JMenuItem) && !(button instanceof JMenu))
                        ((JMenuItem) button).setAccelerator(accStroke);
                    // TODO although this should enable the key shortcuts in the application-wide manner, it doesn't do
                    // that... instead, after a key shortcut is used, the user has to click in the application in order
                    // to be able to use another key shortcut
                    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(accStroke, action);
                    getActionMap().put(action, action);
                }
                if (mnemStroke != null) {
                    button.setMnemonic(mnemStroke.getKeyCode());
                }

                try {
                    String title = null;
                    try {
                        title = messages.getString(bundleName);
                    } catch (MissingResourceException e) {}
                    String tooltip = ServiceLocator.get(TooltipFactory.class).getDecorated(
                            messages.getString(bundleName + ".tooltip"), title, iconName, accStroke);
                    button.setToolTipText(tooltip);
                    button.getAccessibleContext().setAccessibleDescription(tooltip);
                } catch (MissingResourceException e) {}
            }
        };
        listener.propertyChange(new PropertyChangeEvent(this, "messages", null, messages));
        addPropertyChangeListener("messages", listener);

        if (btn != null && btn.getIcon() != null) {
            if (btn != button) {
                btn.setMinimumSize(new Dimension(btn.getIcon().getIconWidth() + 3, btn.getIcon().getIconHeight() + 3));
            } else {
                button.setMinimumSize(new Dimension(btn.getIcon().getIconWidth() + 2, btn.getIcon().getIconHeight() + 2));
            }
        }

        return button;
    }

    /**
     * Create a horizontal "separator" that can have a title and can be used in a {@link JPopupMenu}.
     * 
     * @param bundleName The resource bundle string used to get this separator's text.
     * @return A horizontal "separator" that can have a title and can be used in a {@link JPopupMenu}.
     */
    public JTitledSeparator createToolbarDropdownSeparator(final String bundleName)
    {
        final JTitledSeparator separator = new JTitledSeparator("");

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                try {
                    separator.setTitle(messages.getString(bundleName));
                } catch (MissingResourceException e) {
                    separator.setTitle("");
                }
            }
        };
        listener.propertyChange(new PropertyChangeEvent(this, "messages", null, messages));
        addPropertyChangeListener("messages", listener);

        return separator;
    }

}
