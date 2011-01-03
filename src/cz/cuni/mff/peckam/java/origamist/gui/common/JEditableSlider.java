/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Font;
import java.util.Hashtable;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A slider with a textfield for entering a value.
 * 
 * @author Martin Pecka
 */
public class JEditableSlider extends JPanel
{

    /** */
    private static final long serialVersionUID = -3461220059990284317L;

    /** The slider this component uses. */
    protected JSlider         slider           = null;
    /** The spinner for entering the value. */
    protected JSpinner        spinner          = null;

    /** If <code>true</code>, the listeners know that they should just set the value and do no other actions. */
    protected boolean         setRawValue      = false;

    /** The value the slider takes as the initial maximum. */
    protected int             defaultSliderMax = 400;

    /**
     * 
     */
    public JEditableSlider()
    {
        this(null, null);
    }

    /**
     * @param slider
     * @param spinner
     */
    public JEditableSlider(JSlider slider, JSpinner spinner)
    {
        setSlider(slider);
        setSpinner(spinner);

        createComponents();
        buildLayout();
    }

    /**
     * Create all components this component uses.
     */
    protected void createComponents()
    {
        if (slider == null) {
            JSlider sl = new JSlider(new DefaultBoundedRangeModel(100, 5, 25, defaultSliderMax));

            Hashtable<Object, Object> labels = new Hashtable<Object, Object>();

            Font labelFont = new JLabel().getFont().deriveFont(Font.PLAIN, 9f);
            for (Integer i : new Integer[] { 25, 100, 200, defaultSliderMax }) {
                JLabel label = new JLabel(i.toString());
                label.setFont(labelFont);
                labels.put(i, label);
            }
            sl.setLabelTable(labels);

            sl.setPaintTicks(true);
            sl.setMajorTickSpacing(75);
            sl.setMinorTickSpacing(15);
            sl.setPaintLabels(true);

            setSlider(sl);
        }

        if (spinner == null) {
            JSpinner sp = new JSpinner(new SpinnerNumberModel());
            JSpinner.NumberEditor editor = (NumberEditor) sp.getEditor();
            editor.getTextField().setColumns(3);
            sp.setEditor(editor);

            setSpinner(sp);
        }

        spinner.setValue(100);
        slider.setValue(100);

        updateSpinnerModel();
    }

    /**
     * Add components to layout.
     */
    protected void buildLayout()
    {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("default,$rgap,pref", ""), this);

        builder.append(slider, spinner);
    }

    /**
     * @return the slider
     */
    public JSlider getSlider()
    {
        return slider;
    }

    /**
     * @param slider the slider to set
     */
    protected void setSlider(JSlider slider)
    {
        this.slider = slider;
        if (slider == null)
            return;

        slider.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (JEditableSlider.this.slider.getModel().getValueIsAdjusting())
                    return;

                updateSpinnerModel();
            }
        });
    }

    /**
     * @return the spinner
     */
    public JSpinner getSpinner()
    {
        return spinner;
    }

    /**
     * @param spinner the spinner to set
     */
    protected void setSpinner(JSpinner spinner)
    {
        this.spinner = spinner;
        if (spinner == null)
            return;

        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (setRawValue)
                    return;

                setRawValue = true;
                Integer value = (Integer) JEditableSlider.this.spinner.getValue();
                if (value > slider.getModel().getMaximum() || slider.getModel().getMaximum() > defaultSliderMax) {
                    slider.getModel().setMaximum(Math.max(value + 10, defaultSliderMax));
                    JTextField field = ((NumberEditor) JEditableSlider.this.spinner.getEditor()).getTextField();
                    field.setColumns((int) Math.ceil(Math.log10(value))); // TODO doesn't relayout the component
                }
                slider.setValue(value);
                setRawValue = false;
            }
        });
    }

    /**
     * Update the spinner's model to honor the min and max from the slider.
     */
    protected void updateSpinnerModel()
    {
        if (slider != null) {
            int min = slider.getMinimum();
            spinner.setModel(new SpinnerNumberModel(slider.getValue(), min, null, 1));
        }
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        slider.setEnabled(enabled);
        spinner.setEnabled(enabled);
    }

    /**
     * @return The value of the slider.
     */
    public int getValue()
    {
        return slider.getValue();
    }

    /**
     * Set the value of this slider.
     * 
     * @param value The new value.
     */
    public void setValue(int value)
    {
        slider.setValue(value);
        spinner.setValue(value);
    }

    /**
     * @return The value the slider takes as the initial maximum.
     */
    public int getDefaultSliderMax()
    {
        return defaultSliderMax;
    }

    /**
     * @param defaultSliderMax The value the slider takes as the initial maximum.
     */
    public void setDefaultSliderMax(int defaultSliderMax)
    {
        this.defaultSliderMax = defaultSliderMax;
    }

    /**
     * @param l
     * @see javax.swing.JSlider#addChangeListener(javax.swing.event.ChangeListener)
     */
    public void addChangeListener(ChangeListener l)
    {
        slider.addChangeListener(l);
    }

    /**
     * @param l
     * @see javax.swing.JSlider#removeChangeListener(javax.swing.event.ChangeListener)
     */
    public void removeChangeListener(ChangeListener l)
    {
        slider.removeChangeListener(l);
    }

}
