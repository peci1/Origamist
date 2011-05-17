/**
 * 
 */
package javax.swing.origamist;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;

/**
 * A dropdown button that can contain some buttons that belong to a specified {@link BoundButtonGroup}. If a button from
 * the button group gets selected, the dropdown's main button will display the selected button's text and icon and it
 * will be set pressed. If the group's selection isn't a button from this dropdown, the dropdown will display a default
 * text and icon.
 * 
 * The display of the text and icon of the selected subitem is performed via a bound property listener, so the main
 * button's values will change together with the selected button's values.
 * 
 * @author Martin Pecka
 */
public class JDropDownButtonReflectingSelectionGroup extends JDropDownButton
{

    /** */
    private static final long        serialVersionUID = -8836179816349374400L;

    /** The button group this dropdown will reflect. */
    protected BoundButtonGroup       group;

    /** The listener that will listen to the <code>selection</code> property of the group. */
    protected PropertyChangeListener listener;

    /** The listener that will listen to the <code>text</code> and <code>icon</code> properties of the selected button. */
    protected PropertyChangeListener buttonValueListener;

    /** The currently selected button in the group, if it is contained in this button's popup. */
    protected AbstractButton         selection        = null;

    /** The default text of the button. */
    protected String                 text             = "";

    /** The default tooltip of the button. */
    protected String                 tooltip          = "";

    /** The default icon. */
    protected Icon                   icon             = null;

    /** The default mnemonic. */
    protected int                    mnemonic         = 0;

    /** The model to be used for main button. */
    protected DefaultButtonModel     selectedModel    = null;

    /**
     * @param mainButton
     * @param group The button group this dropdown will reflect.
     */
    public JDropDownButtonReflectingSelectionGroup(AbstractButton mainButton, BoundButtonGroup group)
    {
        super(mainButton);
        setGroup(group);

    }

    /**
     * @param icon
     * @param size
     * @param group The button group this dropdown will reflect.
     */
    public JDropDownButtonReflectingSelectionGroup(Icon icon, Dimension size, BoundButtonGroup group)
    {
        super(icon, size);
        setGroup(group);
    }

    /**
     * @param icon
     * @param group The button group this dropdown will reflect.
     */
    public JDropDownButtonReflectingSelectionGroup(Icon icon, BoundButtonGroup group)
    {
        super(icon);
        setGroup(group);
    }

    /**
     * @param mainButton
     * @param arrowButton
     * @param group The button group this dropdown will reflect.
     */
    public JDropDownButtonReflectingSelectionGroup(JRolloverButton mainButton, JRolloverButton arrowButton,
            BoundButtonGroup group)
    {
        super(mainButton, arrowButton);
        setGroup(group);
    }

    /**
     * @param mainButton
     * @param group The button group this dropdown will reflect.
     */
    public JDropDownButtonReflectingSelectionGroup(JRolloverButton mainButton, BoundButtonGroup group)
    {
        super(mainButton);
        setGroup(group);
    }

    @Override
    protected void init()
    {
        super.init();

        selectedModel = new DefaultButtonModel() {
            /** */
            private static final long serialVersionUID = 3516987790537298167L;

            @Override
            public boolean isArmed()
            {
                if (selection != null)
                    return true;
                else
                    return super.isArmed();
            }

            @Override
            public boolean isPressed()
            {
                if (selection != null)
                    return true;
                else
                    return super.isPressed();
            }
        };
        mainButton.setModel(selectedModel);
        mainButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // because Swing doesn't fire a button's action if it is clicked and already is "pressed" before the
                // click, we need to simulate the button's action manually
                mainButton.getAction().actionPerformed(new ActionEvent(e.getSource(), e.getID(), "click"));
            }
        });

        buttonValueListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (!AbstractButton.TEXT_CHANGED_PROPERTY.equals(evt.getPropertyName())
                        && !AbstractButton.ICON_CHANGED_PROPERTY.equals(evt.getPropertyName()))
                    return;

                configureMainButtonFromSubItem((AbstractButton) evt.getSource());
            }
        };

        listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (!"selection".equals(evt.getPropertyName()))
                    return;
                if (group == null) // shouldn't happen, but for sure
                    return;

                ButtonModel oldSelection = (ButtonModel) evt.getOldValue();
                ButtonModel newSelection = (ButtonModel) evt.getNewValue();
                AbstractButton oldBtn = group.getButtonForModel(oldSelection);
                AbstractButton newBtn = group.getButtonForModel(newSelection);

                // if popup doesn't contain the new button, then we want this to behave as if nothing was selected
                if (!popup.isAncestorOf(newBtn))
                    newBtn = null;

                if (oldSelection == newSelection)
                    return;

                selection = newBtn;

                if (oldBtn != null) {
                    oldBtn.removePropertyChangeListener(buttonValueListener);
                }
                if (newBtn != null) {
                    newBtn.addPropertyChangeListener(AbstractButton.TEXT_CHANGED_PROPERTY, buttonValueListener);
                    newBtn.addPropertyChangeListener(AbstractButton.ICON_CHANGED_PROPERTY, buttonValueListener);
                    configureMainButtonFromSubItem(newBtn);
                } else if (oldBtn != null) {
                    configureMainButtonToDefault();
                }
            }
        };
    }

    /**
     * Set the main button's values from the item's values.
     * 
     * @param item The source of values.
     */
    protected void configureMainButtonFromSubItem(AbstractButton item)
    {
        mainButton.setText(item.getText());
        mainButton.setIcon(item.getIcon());
        mainButton.setToolTipText(item.getToolTipText());
        mainButton.setMnemonic(item.getMnemonic());
        invalidate();
        repaint();
    }

    /**
     * Set the default values to the main button.
     */
    protected void configureMainButtonToDefault()
    {
        mainButton.setText(text);
        mainButton.setIcon(icon);
        mainButton.setToolTipText(tooltip);
        mainButton.setMnemonic(mnemonic);
        invalidate();
        repaint();
    }

    /**
     * @return The button group this dropdown will reflect.
     */
    public BoundButtonGroup getGroup()
    {
        return group;
    }

    /**
     * @param group The button group this dropdown will reflect.
     */
    public void setGroup(BoundButtonGroup group)
    {
        if (group == this.group)
            return;

        selection = null;

        if (this.group != null)
            this.group.removePropertyChangeListener(listener);
        this.group = group;
        if (group != null)
            group.addPropertyChangeListener("selection", listener);
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);
        this.text = text;
    }

    @Override
    public void setIcon(Icon defaultIcon)
    {
        super.setIcon(defaultIcon);
        this.icon = defaultIcon;
    }

    @Override
    public void setToolTipText(String text)
    {
        super.setToolTipText(text);
        tooltip = text;
    }

    @Override
    public void setMnemonic(int mnemonic)
    {
        super.setMnemonic(mnemonic);
        this.mnemonic = mnemonic;
    }

}
