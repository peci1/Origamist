// Copyright (C) 2005 Mammoth Software LLC
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
//
// Contact the author at: info@mammothsoftware.com
package javax.swing.origamist;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * A button that uses a mouse listener to indicate rollover.
 * 
 * @author m. bangham
 * @author Martin Pecka - added Javadoc and some modifications.
 *         Copyright 2005 Mammoth Software LLC
 */
public class JRolloverButton extends JButton
{

    /** If true, the size was explicitly set. */
    protected boolean         isSizeSet        = false;

    /** */
    private static final long serialVersionUID = 3091885225750513834L;

    /** The border of the component. */
    protected Border          compBorder       = super.getBorder();

    /** The outer border to be used when the button is rolled over. */
    protected Border          rolloverBorder   = BorderFactory.createRaisedBevelBorder();

    /**
     * Create the rollover button with no icon.
     */
    public JRolloverButton()
    {
        init();
        initRolloverListener();
    }

    /**
     * Creates the rollover button with the given icon and set its size to the given one.
     * 
     * @param icon The icon to display.
     * @param size The size of the button.
     */
    public JRolloverButton(Icon icon, Dimension size)
    {
        this(icon, size, true);
    }

    /**
     * Creates the rollover button with the given icon and set its size to the given one.
     * 
     * If <code>isRollover</code> is false, the button will not behave as rollover.
     * 
     * @param icon The icon to display.
     * @param size The size of the button.
     * @param isRollover Whether the button has to behave as rollover.
     */
    public JRolloverButton(Icon icon, Dimension size, boolean isRollover)
    {
        super(icon);
        init();
        if (isRollover)
            initRolloverListener();

        if (size != null)
            isSizeSet = true;
        setFixedSize(size);
    }

    /**
     * Create the rollover button with no icon and set its size to the given one.
     * 
     * @param size The size of the button.
     * @param isRollover Whether the button has to behave as rollover.
     */
    public JRolloverButton(Dimension size, boolean isRollover)
    {
        this((Icon) null, size, isRollover);
    }

    /**
     * Create the rollover button with icon loaded from the given action and set the action as the button's action. Also
     * set the size to the given one.
     * 
     * @param action The action to perform and which to take the icon from.
     * @param size The size of the button.
     */
    public JRolloverButton(Action action, Dimension size)
    {
        this((Icon) action.getValue(Action.SMALL_ICON), size);
        // Note: using setAction(action) causes both icon and text
        // to be displayed on toolbar.
        addActionListener(action);
    }

    /**
     * Creates the rollover button initialized by the given button.
     * 
     * @param button The button to initialize this button from.
     */
    public JRolloverButton(AbstractButton button)
    {
        this(button, button.getPreferredSize(), true);
        isSizeSet = false;
    }

    /**
     * Creates the rollover button initialized by the given button.
     * 
     * If <code>isRollover</code> is false, the button will not behave as rollover.
     * 
     * @param button The button to initialize this button from.
     * @param isRollover Whether the button has to behave as rollover.
     */
    public JRolloverButton(AbstractButton button, boolean isRollover)
    {
        this(button, button.getPreferredSize(), isRollover);
        isSizeSet = false;
    }

    /**
     * Creates the rollover button initialized by the given button and set its size to the given one.
     * 
     * @param button The button to initialize this button from.
     * @param size The size of the button.
     */
    public JRolloverButton(AbstractButton button, Dimension size)
    {
        this(button, size, true);
    }

    /**
     * Creates the rollover button initialized by the given button and set its size to the given one.
     * 
     * If <code>isRollover</code> is false, the button will not behave as rollover.
     * 
     * @param button The button to initialize this button from.
     * @param size The size of the button.
     * @param isRollover Whether the button has to behave as rollover.
     */
    public JRolloverButton(AbstractButton button, Dimension size, boolean isRollover)
    {
        this(button.getIcon(), size, isRollover);
        initFromButton(button);
    }

    /**
     * Initialize the button.
     */
    protected void init()
    {
        setRequestFocusEnabled(false);
        setRolloverEnabled(true);

        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (!isSizeSet) {
                    setPreferredSize(null);
                    setFixedSize(getPreferredSize());
                }
            }
        };
        addPropertyChangeListener("text", l);
        addPropertyChangeListener("icon", l);
    }

    /**
     * Set some properties of this rollover button from the given JButton.
     * 
     * @param button The button to initialize this button from.
     */
    protected void initFromButton(AbstractButton button)
    {
        setIcon(button.getIcon());
        setDisabledIcon(button.getDisabledIcon());
        setPressedIcon(button.getPressedIcon());
        setDisabledSelectedIcon(button.getDisabledSelectedIcon());
        setSelectedIcon(button.getSelectedIcon());
        setText(button.getText());
        if (button.isBackgroundSet())
            setBackground(button.getBackground());
        if (button.isForegroundSet())
            setForeground(button.getForeground());
        setBorder(button.getBorder());
        if (button.isCursorSet())
            setCursor(button.getCursor());
        setDisplayedMnemonicIndex(button.getDisplayedMnemonicIndex());
        setEnabled(button.isEnabled());
        if (button.isFontSet())
            setFont(button.getFont());
        setMnemonic(button.getMnemonic());
        setToolTipText(button.getToolTipText());
        setVisible(button.isVisible());
        setOpaque(button.isOpaque());
    }

    /**
     * Set the size of the button to the given one and don't allow resizing.
     * 
     * @param size The size to set.
     */
    protected void setFixedSize(Dimension size)
    {
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    /**
     * Adds a mouse listener to this button.
     */
    protected void initRolloverListener()
    {
        MouseListener l = new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                setRollover(true);
            }

            public void mouseExited(MouseEvent e)
            {
                setRollover(false);
            }
        };
        addMouseListener(l);
    }

    /**
     * Makes the button to appear as rolled over according to the given argument.
     * 
     * @param rollover Whether the button should look rolled over.
     */
    public void setRollover(boolean rollover)
    {
        if (rollover) {
            /*
             * Borders can have different insets - get the size and force it
             * so the new rollover border doesn't change the button size.
             */
            if (!(JRolloverButton.super.getBorder() instanceof RolloverBorder))
                JRolloverButton.super.setBorder(new RolloverBorder(getRolloverBorder(), getBorder()));
            getModel().setRollover(true);
        } else {
            if (JRolloverButton.super.getBorder() instanceof RolloverBorder)
                JRolloverButton.super.setBorder(((RolloverBorder) JRolloverButton.super.getBorder()).getInsideBorder());
            getModel().setRollover(false);
        }
    }

    /**
     * @return The border that is to be used when the mouse is over the button.
     */
    protected Border getRolloverBorder()
    {
        return rolloverBorder;
    }

    /**
     * @param rolloverBorder The border that is to be used when the mouse is over the button.
     */
    public void setRolloverBorder(Border rolloverBorder)
    {
        this.rolloverBorder = rolloverBorder;
    }

    @Override
    public void setBorder(Border border)
    {
        compBorder = border;
        if (!(super.getBorder() instanceof CompoundBorder)) {
            super.setBorder(border);
        }
    }

    @Override
    public Border getBorder()
    {
        return compBorder != null ? compBorder : super.getBorder();
    }

    @Override
    public Insets getInsets()
    {
        return compBorder != null ? compBorder.getBorderInsets(this) : super.getInsets();
    }

    @Override
    protected void paintBorder(Graphics g)
    {
        if (isBorderPainted()) {
            if (super.getBorder() != null) {
                super.getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
            }
        }
    }

    /**
     * A simple tagging compound border to be able to differentiate between user-set compound borders and the inner set
     * ones.
     * 
     * @author Martin Pecka
     */
    protected class RolloverBorder extends CompoundBorder
    {

        /** */
        private static final long serialVersionUID = 5394741728171932908L;

        /**
         * 
         */
        public RolloverBorder()
        {
            super();
        }

        /**
         * @param outsideBorder
         * @param insideBorder
         */
        public RolloverBorder(Border outsideBorder, Border insideBorder)
        {
            super(outsideBorder, insideBorder);
        }

    }
}
