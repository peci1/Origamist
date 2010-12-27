/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private static final long serialVersionUID   = 3259320166895060569L;

    /** Aspect ratio of the entered dimension. If this is 0, the ratio is undefined. */
    protected double          aspectRatio        = 0d;

    /** The value of this input. */
    protected UnitDimension   value              = new UnitDimension();

    /**
     * If <code>true</code>, setting values to width or height won't affect the other even if the preserve ratio
     * checkbox is checked.
     */
    protected boolean         ignoreAscpectRatio = false;

    /**
     * If <code>true</code>, setting the value fro width and height components only sets its value without performing
     * actions in the custom change listners.
     */
    protected boolean         setRawValue        = false;

    /** Input for width. */
    protected JSpinner        width;
    /** Input for height. */
    protected JSpinner        height;
    /** Input for unit. */
    protected JComboBox       unit;
    /** Checkbox for selecting, whether the aspect ratio should be preserved. */
    protected JCheckBox       preserveRatio;
    /** Input for reference length. */
    protected JSpinner        refLength;
    /** Input for reference unit. */
    protected JComboBox       refUnit;

    /** Label. */
    protected JLabel          widthLabel, heightLabel, unitLabel, refLabel, refLengthLabel, refUnitLabel;

    /**
     * 
     */
    public JUnitDimensionInput()
    {
        createComponents();
        buildLayout();
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

        width = new JSpinner(new SpinnerNumberModel(0.0d, 0.0d, null, 0.1d));
        JSpinner.NumberEditor widthEditor = new JSpinner.NumberEditor(width, "0.0######");
        widthEditor.getTextField().setColumns(9);
        width.setEditor(widthEditor);
        width.setValue(0.0d);
        value.setWidth(0.0d);
        width.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (setRawValue)
                    return;

                Double newVal = (Double) width.getValue();
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

                    if (!ignoreAscpectRatio && preserveRatio.isSelected() && aspectRatio > 0.0d && !refRescaled) {
                        double newHeight = (value.getUnit() != Unit.REL ? newVal : (newVal * 100d)) / aspectRatio;
                        ignoreAscpectRatio = true;
                        height.setValue(newHeight);
                        ignoreAscpectRatio = false;
                    } else {
                        if (value.getWidth() == 0.0d || value.getHeight() == 0.0d) {
                            aspectRatio = 0.0d;
                        } else {
                            aspectRatio = value.getWidth() / value.getHeight();
                        }
                    }
                }
            }
        });

        height = new JSpinner(new SpinnerNumberModel(0.0d, 0.0d, null, 0.1d));
        JSpinner.NumberEditor heightEditor = new JSpinner.NumberEditor(height, "0.0######");
        heightEditor.getTextField().setColumns(9);
        height.setEditor(heightEditor);
        height.setValue(0.0d);
        value.setHeight(0.0d);
        height.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (setRawValue)
                    return;

                Double newVal = (Double) height.getValue();
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

                    if (!ignoreAscpectRatio && preserveRatio.isSelected() && aspectRatio > 0.0d && !refRescaled) {
                        double newWidth = (value.getUnit() != Unit.REL ? newVal : (newVal * 100d)) * aspectRatio;
                        ignoreAscpectRatio = true;
                        width.setValue(newWidth);
                        ignoreAscpectRatio = false;
                    } else {
                        if (value.getWidth() == 0.0d || value.getHeight() == 0.0d) {
                            aspectRatio = 0.0d;
                        } else {
                            aspectRatio = value.getWidth() / value.getHeight();
                        }
                    }
                }
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

                    setValue(newValue);

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
                }
            }
        };
        refUnit.addItemListener(refUnitItemListener);

        value.setReference((Unit) refUnit.getSelectedItem(), (Double) refLength.getValue());

        unitItemListener.itemStateChanged(new ItemEvent(unit, 0, unit.getSelectedItem(), ItemEvent.SELECTED));
        refUnitItemListener.itemStateChanged(new ItemEvent(refUnit, 0, refUnit.getSelectedItem(), ItemEvent.SELECTED));
    }

    /**
     * Add all components to layout.
     */
    protected void buildLayout()
    {
        setLayout(new FormLayout("pref,$lcgap,min(60dlu;pref),$ugap,pref,$lcgap,pref",
                "pref,$lgap,pref,$lgap,pref,$lgap,pref"));
        CellConstraints cc = new CellConstraints();

        add(widthLabel, cc.xy(1, 1));
        add(width, cc.xy(3, 1));

        add(heightLabel, cc.xy(1, 3));
        add(height, cc.xy(3, 3));

        add(unitLabel, cc.xy(5, 1));
        add(unit, cc.xy(7, 1));

        add(preserveRatio, cc.xyw(5, 3, 3));

        add(refLabel, cc.xyw(1, 5, 5));

        add(refLengthLabel, cc.xy(1, 7));
        add(refLength, cc.xy(3, 7));

        add(refUnitLabel, cc.xy(5, 7));
        add(refUnit, cc.xy(7, 7));
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

        this.value.setWidth(value.getWidth());
        this.value.setHeight(value.getHeight());
        this.value.setUnit(value.getUnit());

        if (value.getReferenceLength() != null && value.getReferenceUnit() != null) {
            this.value.setReference(value.getReferenceUnit(), value.getReferenceLength());
            refLength.setValue(value.getReferenceLength());
            refUnit.setSelectedItem(value.getReferenceUnit());
        }

        unit.setSelectedItem(value.getUnit());

        ignoreAscpectRatio = true;
        if (value.getUnit() != Unit.REL) {
            width.setValue(value.getWidth());
            height.setValue(value.getHeight());
        } else {
            double newWidth = value.getWidth();
            double newHeight = value.getHeight();

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

}
