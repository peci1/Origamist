/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.Border;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An editable slider with zoom icons.
 * 
 * @author Martin Pecka
 */
public class JZoomSlider extends JEditableSlider
{
    /** */
    private static final long serialVersionUID = 7403547161994063196L;

    /** Button for zooming. */
    protected JButton         zoomIn, zoomOut;

    /** The number that represents the value change if either the zoomIn or zoomOut is clicked. */
    protected int             zoomStep         = 10;

    /**
     * 
     */
    public JZoomSlider()
    {
        super();
    }

    /**
     * @param slider
     * @param spinner
     */
    public JZoomSlider(JSlider slider, JSpinner spinner)
    {
        super(slider, spinner);
    }

    @Override
    protected void createComponents()
    {
        super.createComponents();

        zoomIn = new JButton(new ImageIcon(getClass().getResource("/resources/images/zoom-in-24.png")));
        zoomOut = new JButton(new ImageIcon(getClass().getResource("/resources/images/zoom-out-24.png")));

        Border border = BorderFactory.createEmptyBorder();
        zoomIn.setBorder(border);
        zoomOut.setBorder(border);

        zoomIn.setBackground(new Color(0, 0, 0, 0));
        zoomIn.setOpaque(false);
        zoomOut.setBackground(new Color(0, 0, 0, 0));
        zoomOut.setOpaque(false);

        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setValue(getValue() + zoomStep);
            }
        });

        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setValue(getValue() - zoomStep);
            }
        });

        ServiceLocator
                .get(ConfigurationManager.class)
                .get()
                .addAndRunResourceBundleListener(
                        new Configuration.LocaleListener("application", "JZoomSlider.zoomIn.tooltip") {
                            @Override
                            protected void updateText(String text)
                            {
                                String zoomInTooltip = text;
                                String zoomOutTooltip = bundle.getString("JZoomSlider.zoomOut.tooltip");

                                String tooltip = ServiceLocator.get(TooltipFactory.class).getPlain(zoomInTooltip);
                                zoomIn.setToolTipText(tooltip);

                                tooltip = ServiceLocator.get(TooltipFactory.class).getPlain(zoomOutTooltip);
                                zoomOut.setToolTipText(tooltip);
                            }
                        });
    }

    @Override
    protected void buildLayout()
    {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("min,0px,default,0px,min,$rgap,pref", ""),
                this);

        builder.append(zoomOut, slider, zoomIn);
        builder.append(spinner);
    }

    /**
     * @return the zoomStep
     */
    public int getZoomStep()
    {
        return zoomStep;
    }

    /**
     * @param zoomStep the zoomStep to set
     */
    public void setZoomStep(int zoomStep)
    {
        this.zoomStep = zoomStep;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        zoomIn.setEnabled(enabled);
        zoomOut.setEnabled(enabled);
    }
}
