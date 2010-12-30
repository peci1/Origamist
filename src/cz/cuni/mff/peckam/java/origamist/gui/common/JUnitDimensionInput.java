/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.origamist.JLocalizedButton;
import javax.swing.origamist.JLocalizedLabel;
import javax.swing.origamist.UnitListCellRenderer;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A input element for {@link cz.cuni.mff.peckam.java.origamist.model.UnitDimension}.
 * 
 * @author Martin Pecka
 */
/**
 * 
 * 
 * @author Martin Pecka
 */
public class JUnitDimensionInput extends JPanel
{
    /** */
    private static final long      serialVersionUID           = 3259320166895060569L;

    /** Aspect ratio of the entered dimension. If this is 0, the ratio is undefined. */
    protected double               aspectRatio                = 0d;

    /** The value of this input. */
    protected UnitDimension        value                      = new UnitDimension();

    /**
     * If <code>true</code>, setting values to width or height won't affect the other even if the preserve ratio
     * checkbox is checked.
     */
    protected boolean              ignoreAscpectRatio         = false;

    /**
     * If <code>true</code>, setting the value fro width and height components only sets its value without performing
     * actions in the custom change listners.
     */
    protected boolean              setRawValue                = false;

    /** The list of change listeners. */
    protected List<ChangeListener> changeListeners            = new LinkedList<ChangeListener>();

    /** It <code>true</code>, the change listeners are not notified of changes. */
    protected boolean              disableChangeListeners     = false;

    /**
     * If <code>true</code>, setValue() will just set the value and will do no other side-effects. Not used in this
     * class, but can be handy in subclasses.
     */
    protected boolean              setValueWithoutSideEffects = false;

    /** Input for width. */
    protected JSpinner             width;
    /** Input for height. */
    protected JSpinner             height;
    /** Input for unit. */
    protected JComboBox            unit;
    /** Checkbox for selecting, whether the aspect ratio should be preserved. */
    protected JCheckBox            preserveRatio;
    /** Input for reference length. */
    protected JSpinner             refLength;
    /** Input for reference unit. */
    protected JComboBox            refUnit;
    /** The label displaying the current aspect ratio. */
    protected JLabel               aspectRatioDisplay;
    /** The button for rotating the paper 90 degrees. */
    protected JLocalizedButton     rotatePaper;

    /** Label. */
    protected JLabel               widthLabel, heightLabel, unitLabel, refLabel, refLengthLabel, refUnitLabel,
            aspectRatioLabel;

    /** Listener for aspect ratio changes. */
    protected ChangeListener aspectRatioListener;

    /**
     * 
     */
    public JUnitDimensionInput()
    {
        createComponents();
        buildLayout();
        
        ItemEvent unitEvent = new ItemEvent(unit, 0, unit.getSelectedItem(), ItemEvent.SELECTED);
        for (ItemListener l : unit.getItemListeners()) {
            l.itemStateChanged(unitEvent);
        }
        
        ItemEvent refUnitEvent = new ItemEvent(refUnit, 0, refUnit.getSelectedItem(), ItemEvent.SELECTED);
        for (ItemListener l : refUnit.getItemListeners()) {
            l.itemStateChanged(refUnitEvent);
        }
        
        aspectRatioListener.stateChanged(new ChangeEvent(this));
    }

    /**
     * Create and setup all the components.
     */
    protected void createComponents()
    {
        Unit prefUnit = ServiceLocator.get(ConfigurationManager.class).get().getPreferredUnit();

        widthLabel = new JLocalizedLabel("application", "JUnitDimensionInput.width");
        heightLabel = new JLocalizedLabel("application", "JUnitDimensionInput.height");
        unitLabel = new JLocalizedLabel("application", "JUnitDimensionInput.unit");
        refLabel = new JLocalizedLabel("application", "JUnitDimensionInput.refLabel");
        refLengthLabel = new JLocalizedLabel("application", "JUnitDimensionInput.refLength");
        refUnitLabel = new JLocalizedLabel("application", "JUnitDimensionInput.refUnit");
        aspectRatioLabel = new JLocalizedLabel("application", "JUnitDimensionInput.aspectRatioLabel");

        aspectRatioDisplay = new JLabel();
        aspectRatioListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                NumberFormat format = NumberFormat.getInstance(ServiceLocator.get(ConfigurationManager.class).get()
                        .getLocale());
                format.setMaximumFractionDigits(5);
                format.setMinimumFractionDigits(1);
                aspectRatioDisplay.setText(format.format(aspectRatio));
                aspectRatioDisplay.repaint();
            }
        };

        width = new JSpinner(new SpinnerNumberModel(0.0d, 0.0d, null, 0.1d));
        final JSpinner.NumberEditor widthEditor = new JSpinner.NumberEditor(width, "0.0######");
        widthEditor.getTextField().setColumns(9);
        width.setEditor(widthEditor);
        width.setValue(0.0d);
        value.setWidth(0.0d);
        width.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                Double newVal = (Double) width.getValue();

                if (setRawValue) {
                    value.setWidth(newVal);
                    return;
                }

                boolean refRescaled = false;
                if (value.getUnit() == Unit.REL) {
                    newVal /= 100d;
                    if (newVal > 1.0d) {
                        refRescaled = true;
                        double ratio = 1.0d / newVal;
                        newVal = 1.0d;
                        if (!ignoreAscpectRatio && preserveRatio.isSelected() && aspectRatio > 0.0d) {
                            refLength.setValue(value.getReferenceLength() / ratio);
                            setRawValue = true;
                            width.setValue(100d);
                            setRawValue = false;
                        } else {
                            double newHeight = value.getHeight() * ratio;
                            double newRefLength = value.getReferenceLength() / ratio;
                            ignoreAscpectRatio = true;
                            height.setValue(newHeight * 100d);
                            ignoreAscpectRatio = false;
                            refLength.setValue(newRefLength);
                            setRawValue = true;
                            width.setValue(100d);
                            setRawValue = false;
                        }
                    }
                }

                if (newVal != value.getWidth() || refRescaled) {
                    value.setWidth(newVal);

                    boolean oldDisable = disableChangeListeners;
                    disableChangeListeners = true;

                    if (!ignoreAscpectRatio && preserveRatio.isSelected() && aspectRatio > 0.0d && !refRescaled) {
                        double newHeight = (value.getUnit() != Unit.REL ? newVal : (newVal * 100d)) / aspectRatio;
                        ignoreAscpectRatio = true;
                        height.setValue(newHeight);
                        ignoreAscpectRatio = false;
                    }
                    disableChangeListeners = oldDisable;
                    notifyChangeListeners();
                }

                if (value.getWidth() == 0.0d || value.getHeight() == 0.0d) {
                    aspectRatio = 0.0d;
                } else {
                    aspectRatio = value.getWidth() / value.getHeight();
                }

                aspectRatioListener.stateChanged(e);
            }
        });

        height = new JSpinner(new SpinnerNumberModel(0.0d, 0.0d, null, 0.1d));
        final JSpinner.NumberEditor heightEditor = new JSpinner.NumberEditor(height, "0.0######");
        heightEditor.getTextField().setColumns(9);
        height.setEditor(heightEditor);
        height.setValue(0.0d);
        value.setHeight(0.0d);
        height.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                Double newVal = (Double) height.getValue();

                if (setRawValue) {
                    value.setHeight(newVal);
                    return;
                }

                boolean refRescaled = false;
                if (value.getUnit() == Unit.REL) {
                    newVal /= 100d;
                    if (newVal > 1.0d) {
                        refRescaled = true;
                        double ratio = 1.0d / newVal;
                        newVal = 1.0d;
                        if (!ignoreAscpectRatio && preserveRatio.isSelected() && aspectRatio > 0.0d) {
                            refLength.setValue(value.getReferenceLength() / ratio);
                            setRawValue = true;
                            height.setValue(100d);
                            setRawValue = false;
                        } else {
                            double newWidth = value.getWidth() * ratio;
                            double newRefLength = value.getReferenceLength() / ratio;
                            ignoreAscpectRatio = true;
                            width.setValue(newWidth * 100d);
                            ignoreAscpectRatio = false;
                            refLength.setValue(newRefLength);
                            setRawValue = true;
                            height.setValue(100d);
                            setRawValue = false;
                        }
                    }
                }

                if (newVal != value.getHeight() || refRescaled) {
                    value.setHeight(newVal);

                    boolean oldDisable = disableChangeListeners;
                    disableChangeListeners = true;

                    if (!ignoreAscpectRatio && preserveRatio.isSelected() && aspectRatio > 0.0d && !refRescaled) {
                        double newWidth = (value.getUnit() != Unit.REL ? newVal : (newVal * 100d)) * aspectRatio;
                        ignoreAscpectRatio = true;
                        width.setValue(newWidth);
                        ignoreAscpectRatio = false;
                    }

                    disableChangeListeners = oldDisable;
                    notifyChangeListeners();
                }

                if (value.getWidth() == 0.0d || value.getHeight() == 0.0d) {
                    aspectRatio = 0.0d;
                } else {
                    aspectRatio = value.getWidth() / value.getHeight();
                }

                aspectRatioListener.stateChanged(e);
            }
        });

        unit = new JComboBox(getUnits(true));
        unit.setRenderer(new UnitListCellRenderer());
        unit.setSelectedItem(prefUnit != null ? prefUnit : unit.getItemAt(0));
        value.setUnit((Unit) unit.getSelectedItem());
        ItemListener unitItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Unit newUnit = (Unit) e.getItem();
                    boolean isRel = newUnit == Unit.REL;

                    UnitDimension newValue = value.convertTo(newUnit);

                    setValueWithoutSideEffects = true;
                    setValue(newValue);
                    setValueWithoutSideEffects = false;

                    refLength.setVisible(isRel);
                    refLengthLabel.setVisible(isRel);
                    refUnit.setVisible(isRel);
                    refUnitLabel.setVisible(isRel);
                    refLabel.setVisible(isRel);
                    invalidate();
                }
            }
        };
        unit.addItemListener(unitItemListener);

        preserveRatio = new JCheckBox();
        ServiceLocator
                .get(ConfigurationManager.class)
                .get()
                .addAndRunResourceBundleListener(
                        new Configuration.LocaleListener("application", "JUnitDimensionInput.preserveRatio") {
                            @Override
                            protected void updateText(String text)
                            {
                                preserveRatio.setText(text);
                                preserveRatio.setToolTipText(ServiceLocator.get(TooltipFactory.class).getPlain(text));
                            }
                        });

        rotatePaper = new JLocalizedButton("application", "JUnitDimensionInput.rotatePaperLabel");
        rotatePaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                UnitDimension value = getValue();
                double height = value.getHeight();
                value.setHeight(value.getWidth());
                value.setWidth(height);
                setValue(value);
            }
        });

        refLength = new JSpinner(new SpinnerNumberModel(0.0d, 0.0d, null, 0.1d));
        JSpinner.NumberEditor refLengthEditor = new JSpinner.NumberEditor(refLength, "0.0######");
        refLengthEditor.getTextField().setColumns(9);
        refLength.setEditor(refLengthEditor);
        refLength.setValue(1.0d);
        refLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                value.setReference(value.getReferenceUnit(), (Double) refLength.getValue());
                notifyChangeListeners();
            }
        });

        refUnit = new JComboBox(getUnits(false));
        refUnit.setSelectedItem(prefUnit != null && !prefUnit.equals(Unit.REL) ? prefUnit : refUnit.getItemAt(0));
        refUnit.setRenderer(new UnitListCellRenderer());
        ItemListener refUnitItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    value.setReference((Unit) refUnit.getSelectedItem(), value.getReferenceLength());
                    notifyChangeListeners();
                }
            }
        };
        refUnit.addItemListener(refUnitItemListener);

        value.setReference((Unit) refUnit.getSelectedItem(), (Double) refLength.getValue());
        
        PropertyChangeListener localeListener = new PropertyChangeListener() {;
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                //TODO this should localize the spinners, but it doesn't work...
                Locale l = (Locale) evt.getNewValue();
                widthEditor.getFormat().setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(l));
                heightEditor.getFormat().setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(l));
            }
        };
        Configuration conf = ServiceLocator.get(ConfigurationManager.class).get();
        conf.addPropertyChangeListener("locale", localeListener);
        localeListener.propertyChange(new PropertyChangeEvent(this, "locale", null, conf.getLocale()));
    }

    /**
     * Add all components to layout.
     */
    protected void buildLayout()
    {
        setLayout(new FormLayout("pref,$lcgap,min(60dlu;pref),$ugap,pref,$lcgap,pref",
                "pref,$lgap,pref,$lgap,pref,$lgap,pref,$lgap,pref"));
        CellConstraints cc = new CellConstraints();

        add(widthLabel, cc.xy(1, 1));
        add(width, cc.xy(3, 1));

        add(heightLabel, cc.xy(1, 3));
        add(height, cc.xy(3, 3));

        add(unitLabel, cc.xy(5, 1));
        add(unit, cc.xy(7, 1));

        add(preserveRatio, cc.xyw(5, 3, 3));

        add(aspectRatioLabel, cc.xy(1, 5));
        add(aspectRatioDisplay, cc.xy(3, 5));
        add(rotatePaper, cc.xyw(5, 5, 3));

        add(refLabel, cc.xyw(1, 7, 5));

        add(refLengthLabel, cc.xy(1, 9));
        add(refLength, cc.xy(3, 9));

        add(refUnitLabel, cc.xy(5, 9));
        add(refUnit, cc.xy(7, 9));
    }

    /**
     * Returns a vector of all units.
     * 
     * @param includeRel If <code>false</code>, the <code>Unit.REL</code> won't appear in the returned list.
     * @return A vector of all units.
     */
    protected Vector<Unit> getUnits(boolean includeRel)
    {
        Vector<Unit> units = new Vector<Unit>(Arrays.asList(Unit.values()));
        if (!includeRel)
            units.remove(Unit.REL);
        return units;
    }

    /**
     * @return The copy of the value.
     */
    public UnitDimension getValue()
    {
        UnitDimension result = new UnitDimension();
        result.setWidth(value.getWidth());
        result.setHeight(value.getHeight());
        result.setUnit(value.getUnit());
        if (value.getUnit().equals(Unit.REL)) {
            result.setReference(value.getReferenceUnit(), value.getReferenceLength());
        }
        return result;
    }

    /**
     * @param value The value which is used to set this input's value.
     */
    public void setValue(UnitDimension value)
    {
        if (value.getUnit() == null)
            value.setUnit(Unit.values()[0]);

        disableChangeListeners = true;

        Unit oldUnit = this.value.getUnit();

        this.value = value;

        if (value.getReferenceLength() != null && value.getReferenceUnit() != null) {
            refLength.setValue(value.getReferenceLength());
            refUnit.setSelectedItem(value.getReferenceUnit());
        }
        value.setReference((Unit) refUnit.getSelectedItem(), (Double) refLength.getValue());

        unit.setSelectedItem(value.getUnit());

        ignoreAscpectRatio = true;
        if (value.getUnit() != Unit.REL) {
            width.setValue(value.getWidth());
            height.setValue(value.getHeight());
        } else {
            double newWidth = value.getWidth();
            double newHeight = value.getHeight();
            // if we convert between a relative and relative value, we must normalize the value
            if (oldUnit == Unit.REL) {
                newWidth /= 100d;
                newHeight /= 100d;
            }

            if (newWidth > 1.0d || newHeight > 1.0d) {
                double ratio = 1.0d / Math.max(newWidth, newHeight);
                newWidth *= ratio;
                newHeight *= ratio;
                refLength.setValue(value.getReferenceLength() / ratio);
            }
            this.value.setWidth(newWidth);
            this.value.setHeight(newHeight);
            setRawValue = true;
            width.setValue(newWidth * 100d);
            height.setValue(newHeight * 100d);
            setRawValue = false;
        }
        ignoreAscpectRatio = false;

        disableChangeListeners = false;
        notifyChangeListeners();
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        width.setEnabled(enabled);
        height.setEnabled(enabled);
        unit.setEnabled(enabled);
        preserveRatio.setEnabled(enabled);
        refLength.setEnabled(enabled);
        refUnit.setEnabled(enabled);
    }

    /**
     * Add a change listener.
     * 
     * @param listener The listener to add.
     */
    public void addChangeListener(ChangeListener listener)
    {
        changeListeners.add(listener);
    }

    /**
     * Remove the given change listener.
     * 
     * @param listener The listener to remove.
     */
    public void removeChangeListener(ChangeListener listener)
    {
        changeListeners.remove(listener);
    }

    /**
     * Notify change listeners that a value has changed.
     */
    protected void notifyChangeListeners()
    {
        if (disableChangeListeners)
            return;

        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener l : changeListeners) {
            l.stateChanged(e);
        }
    }

}
