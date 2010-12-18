package javax.swing.origamist;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

/**
 * A menu item that behaves as a <code>JToggleButton</code> (and can be assigned to a group).
 * 
 * @author Martin Pecka
 */
public class JToggleMenuItem extends JMenuItem
{

    /** */
    private static final long serialVersionUID = -8363837608437818982L;

    /**
     * 
     */
    public JToggleMenuItem()
    {
        super();
    }

    /**
     * @param a
     */
    public JToggleMenuItem(Action a)
    {
        super(a);
    }

    /**
     * @param icon
     */
    public JToggleMenuItem(Icon icon)
    {
        super(icon);
    }

    /**
     * @param text
     * @param icon
     */
    public JToggleMenuItem(String text, Icon icon)
    {
        super(text, icon);
    }

    /**
     * @param text
     * @param mnemonic
     */
    public JToggleMenuItem(String text, int mnemonic)
    {
        super(text, mnemonic);
    }

    /**
     * @param text
     */
    public JToggleMenuItem(String text)
    {
        super(text);
    }

    @Override
    protected void init(String text, Icon icon)
    {
        super.init(text, icon);
        setModel(new ToggleMenuItemModel());
    }

    /**
     * The model that reflects the <code>selected</code> property also into the <code>armed</code> property.
     * 
     * @author Martin Pecka
     */
    public static class ToggleMenuItemModel extends JToggleButton.ToggleButtonModel
    {
        /** */
        private static final long serialVersionUID = -3361032392540775508L;

        @Override
        public boolean isArmed()
        {
            return super.isArmed() || isSelected();
        }
    }

}