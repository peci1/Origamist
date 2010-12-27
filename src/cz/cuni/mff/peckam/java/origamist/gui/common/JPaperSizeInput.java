/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An input for {@link UnitDimension} which also provides a set of predefined sizes.
 * 
 * @author Martin Pecka
 */
public class JPaperSizeInput extends JUnitDimensionInput
{

    /** */
    private static final long        serialVersionUID = 727058655215687837L;

    /** Combobox for predefined paper sizes. */
    protected JComboBox              paperSizes;

    /** The item that stands for custom size. */
    protected UnitDimensionWithLabel customSize       = null;

    @Override
    protected void createComponents()
    {
        super.createComponents();

        ServiceLocator
                .get(ConfigurationManager.class)
                .get()
                .addAndRunResourceBundleListener(
                        new Configuration.LocaleListener("application", "JPaperSizeInput.paperSizesLabel") {

                            @Override
                            protected void updateText(String text)
                            {
                                setBorder(BorderFactory.createTitledBorder(text));
                            }
                        });

        paperSizes = new JComboBox(getPapers());
        ServiceLocator.get(ConfigurationManager.class).get()
                .addPropertyChangeListener("locale", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        UnitDimensionWithLabel selected = (UnitDimensionWithLabel) paperSizes.getSelectedItem();
                        Vector<UnitDimensionWithLabel> newItems = getPapers();
                        UnitDimensionWithLabel newSelected = null;
                        paperSizes.setModel(new DefaultComboBoxModel(newItems));
                        if (selected != null) {
                            for (UnitDimensionWithLabel d : newItems) {
                                if (d.getDimension() != null && d.getDimension().equals(selected.getDimension())) {
                                    newSelected = d;
                                    break;
                                }
                            }
                        }
                        if (newSelected != null) {
                            paperSizes.setSelectedItem(newSelected);
                        }
                    }
                });
        paperSizes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    UnitDimensionWithLabel newItem = (UnitDimensionWithLabel) e.getItem();
                    if (newItem.getDimension() != null)
                        JPaperSizeInput.super.setValue(newItem.getDimension());
                    JPaperSizeInput.super.setEnabled(newItem.getDimension() == null);
                }
            }
        });
        paperSizes.setRenderer(new DefaultListCellRenderer() {
            /** */
            private static final long serialVersionUID = 445838703043695244L;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus)
            {
                JLabel cell = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                UnitDimensionWithLabel val = (UnitDimensionWithLabel) value;
                if (val.getDimension() != null) {
                    cell.setText(val.getLabel() + " (" + val.getDimension().toString() + ")");
                } else {
                    cell.setText(val.getLabel());
                }
                return cell;

            }
        });
    }

    @Override
    protected void buildLayout()
    {
        setLayout(new FormLayout("pref,$lcgap,min(60dlu;pref),$ugap,pref,$lcgap,pref",
                "pref,$lgap,pref,$lgap,pref,$lgap,pref,$lgap,pref"));
        CellConstraints cc = new CellConstraints();

        add(paperSizes, cc.xyw(1, 1, 7));

        add(widthLabel, cc.xy(1, 3));
        add(width, cc.xy(3, 3));

        add(heightLabel, cc.xy(1, 5));
        add(height, cc.xy(3, 5));

        add(unitLabel, cc.xy(5, 3));
        add(unit, cc.xy(7, 3));

        add(preserveRatio, cc.xyw(5, 5, 3));

        add(refLabel, cc.xyw(1, 7, 5));

        add(refLengthLabel, cc.xy(1, 9));
        add(refLength, cc.xy(3, 9));

        add(refUnitLabel, cc.xy(5, 9));
        add(refUnit, cc.xy(7, 9));
    }

    @Override
    public void setValue(UnitDimension value)
    {
        super.setValue(value);
        if (customSize != null)
            paperSizes.setSelectedItem(customSize);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        paperSizes.setEnabled(enabled);
    }

    /**
     * @return all defined paper sizes (taken from a .properties file).
     */
    protected Vector<UnitDimensionWithLabel> getPapers()
    {
        Vector<UnitDimensionWithLabel> result = new Vector<UnitDimensionWithLabel>();

        ResourceBundle bundle = ResourceBundle.getBundle("application", ServiceLocator.get(ConfigurationManager.class)
                .get().getLocale());
        Enumeration<String> keys = bundle.getKeys();

        customSize = null;

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("paper.") && key.endsWith(".label")) {
                String prefix = key.substring(0, key.lastIndexOf(".label"));

                String label = bundle.getString(key);

                UnitDimension dim = new UnitDimension();
                try {
                    try {
                        double width = Double.parseDouble(bundle.getString(prefix + ".width"));
                        double height = Double.parseDouble(bundle.getString(prefix + ".height"));
                        Unit unit = Unit.fromValue(bundle.getString(prefix + ".unit"));

                        Double refLength = null;
                        try {
                            refLength = Double.parseDouble(bundle.getString(prefix + ".reference.length"));
                        } catch (MissingResourceException e) {}

                        Unit refUnit = null;
                        try {
                            refUnit = Unit.fromValue(bundle.getString(prefix + ".reference.unit"));
                        } catch (MissingResourceException e) {}

                        dim.setWidth(width);
                        dim.setHeight(height);
                        dim.setUnit(unit);
                        dim.setReference(refUnit, refLength);
                    } catch (MissingResourceException e) {
                        // this will happen for the custom paper size
                        dim = null;
                    }

                    UnitDimensionWithLabel newItem = new UnitDimensionWithLabel(dim, label);
                    // we only want one custom item in the list
                    if (!(newItem.getDimension() == null && customSize != null)) {
                        result.add(newItem);
                        if (newItem.getDimension() == null)
                            customSize = newItem;
                    }
                } catch (IllegalArgumentException e) {/* For wrongly spelled units. */}
            }
        }

        UnitDimensionWithLabel[] items = result.toArray(new UnitDimensionWithLabel[] {});
        Arrays.sort(items, new Comparator<UnitDimensionWithLabel>() {
            @Override
            public int compare(UnitDimensionWithLabel o1, UnitDimensionWithLabel o2)
            {
                // let the custom dimension be the first
                if (o1.getDimension() == null)
                    return -1;
                if (o2.getDimension() == null)
                    return 1;
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        result = new Vector<UnitDimensionWithLabel>(Arrays.asList(items));
        return result;
    }

    /**
     * A {@link UnitDimension} with a textual label.
     * 
     * @author Martin Pecka
     */
    protected class UnitDimensionWithLabel
    {
        protected UnitDimension dimension;
        protected String        label;

        /**
         * @param dimension
         * @param label
         */
        public UnitDimensionWithLabel(UnitDimension dimension, String label)
        {
            this.dimension = dimension;
            this.label = label;
        }

        /**
         * @return the dimension
         */
        public UnitDimension getDimension()
        {
            return dimension;
        }

        /**
         * @param dimension the dimension to set
         */
        public void setDimension(UnitDimension dimension)
        {
            this.dimension = dimension;
        }

        /**
         * @return the label
         */
        public String getLabel()
        {
            return label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label)
        {
            this.label = label;
        }
    }

}
