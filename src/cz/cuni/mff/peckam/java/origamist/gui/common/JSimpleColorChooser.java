/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A panel with a button for displaying color chooser and a preview area.
 * 
 * @author Martin Pecka
 */
public class JSimpleColorChooser extends JPanel
{
    /** */
    private static final long serialVersionUID = 7123841380690366089L;

    /** The area for displaying the selected color. */
    protected JComponent      previewArea;
    /** The color chooser that is a part of the dialog. */
    protected JColorChooser   colorChooser;
    /** The button for showing the color chooser dialog. */
    protected AbstractButton  showChooserButton;
    /** The dialog for showing the color chooser. */
    protected JDialog         chooserDialog;

    /** <code>true</code> if the layout is completed. */
    protected boolean         isLayoutSet      = false;

    /**
     * @param initialColor
     */
    public JSimpleColorChooser(Color initialColor)
    {
        this(initialColor, "Select color", "Select color");
    }

    /**
     * @param initialColor
     * @param dialogTitle
     * @param buttonText
     */
    public JSimpleColorChooser(Color initialColor, String dialogTitle, String buttonText)
    {
        setPreviewArea(new JPanel());
        previewArea.setPreferredSize(new Dimension(32, 32));
        previewArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel noPreview = new JPanel();
        noPreview.setSize(100, 32); // workaround for Swing bug
        noPreview.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0)); // workaround for Swing bug

        setColorChooser(new JColorChooser(initialColor != null ? initialColor : Color.WHITE));
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]); // removes HSV panel
        colorChooser.setPreviewPanel(noPreview);

        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                previewArea.setBackground(colorChooser.getColor());
            }
        };
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            }
        };

        chooserDialog = JColorChooser.createDialog(this, dialogTitle, true, colorChooser, okListener, cancelListener);

        setShowChooserButton(new JButton(buttonText));

        setLayout(new FormLayout("pref,$lcgap,pref", "pref"));
        CellConstraints cc = new CellConstraints();
        add(previewArea, cc.xy(1, 1));
        add(showChooserButton, cc.xy(3, 1));

        isLayoutSet = true;
    }

    /**
     * @return the previewArea
     */
    public JComponent getPreviewArea()
    {
        return previewArea;
    }

    /**
     * @param previewArea the previewArea to set
     */
    public void setPreviewArea(JComponent previewArea)
    {
        if (previewArea == null)
            throw new NullPointerException(
                    "JSimpleColorChooser#setPreviewArea(JComponent): previewArea cannot be null.");

        if (isLayoutSet)
            remove(this.previewArea);
        this.previewArea = previewArea;
        if (isLayoutSet)
            add(previewArea, new CellConstraints(1, 1));
    }

    /**
     * @return the colorChooser
     */
    public JColorChooser getColorChooser()
    {
        return colorChooser;
    }

    /**
     * @param colorChooser the colorChooser to set
     */
    public void setColorChooser(JColorChooser colorChooser)
    {
        this.colorChooser = colorChooser;

        this.colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                previewArea.setBackground(JSimpleColorChooser.this.colorChooser.getColor());
            }
        });
    }

    /**
     * @return the chooserDialog
     */
    public JDialog getChooserDialog()
    {
        return chooserDialog;
    }

    /**
     * @return the showChooserButton
     */
    public AbstractButton getShowChooserButton()
    {
        return showChooserButton;
    }

    /**
     * @param showChooserButton the showChooserButton to set
     */
    public void setShowChooserButton(AbstractButton showChooserButton)
    {
        if (isLayoutSet)
            remove(this.showChooserButton);
        this.showChooserButton = showChooserButton;
        if (isLayoutSet)
            add(showChooserButton, new CellConstraints(3, 1));

        this.showChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                chooserDialog.setVisible(true);
            }
        });
    }

    /**
     * Set the color of this chooser.
     * 
     * @param color The color to set.
     */
    public void setColor(Color color)
    {
        previewArea.setBackground(color);
    }

    /**
     * @return the chosen color.
     */
    public Color getColor()
    {
        return previewArea.getBackground();
    }
}
