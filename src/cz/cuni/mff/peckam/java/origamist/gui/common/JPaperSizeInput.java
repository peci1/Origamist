/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.origamist.JLocalizedButton;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;
import cz.cuni.mff.peckam.java.origamist.utils.UnitDimensionWithLabel;

/**
 * An input for {@link UnitDimension} which also provides a set of predefined sizes.
 * 
 * @author Martin Pecka
 */
public class JPaperSizeInput extends JUnitDimensionInput
{

    /** */
    private static final long              serialVersionUID = 727058655215687837L;

    /** The list of user-defined papers. */
    protected List<UnitDimensionWithLabel> userPapers;

    /** Combobox for predefined paper sizes. */
    protected JComboBox                    paperSizes;

    /** The button for saving a custom paper. */
    protected JButton                      savePaper;
    /** The button for removing a saved custom paper. */
    protected JButton                      removePaper;

    /** The item that stands for custom size. */
    protected UnitDimensionWithLabel       customSize;

    /**
     * 
     */
    public JPaperSizeInput()
    {
        ItemEvent evt = new ItemEvent(paperSizes, 0, paperSizes.getSelectedItem(), ItemEvent.SELECTED);
        for (ItemListener l : paperSizes.getItemListeners()) {
            l.itemStateChanged(evt);
        }
    }

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
                    if (newItem.getDimension() == null) {
                        removePaper.setVisible(false);
                        savePaper.setVisible(true);
                        savePaper.setEnabled(true);
                    } else {
                        savePaper.setVisible(false);
                        removePaper.setVisible(true);
                        removePaper.setEnabled(userPapers.contains(newItem));
                    }
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

        ServiceLocator.get(ConfigurationManager.class).get().getPapers()
                .addObserver(new Observer<UnitDimensionWithLabel>() {
                    @Override
                    public void changePerformed(ChangeNotification<UnitDimensionWithLabel> change)
                    {
                        Object selected = paperSizes.getSelectedItem();

                        Vector<UnitDimensionWithLabel> papers = getPapers();

                        paperSizes.setModel(new DefaultComboBoxModel(papers));
                        if (!papers.contains(selected)) {
                            selected = paperSizes.getModel().getElementAt(0);
                        }
                        paperSizes.setSelectedItem(selected);

                        ItemEvent evt = new ItemEvent(paperSizes, 0, selected, ItemEvent.SELECTED);
                        for (ItemListener l : paperSizes.getItemListeners())
                            l.itemStateChanged(evt);
                    }
                });

        Icon saveIcon = new ImageIcon(getClass().getResource("/resources/images/add-24.png"));
        savePaper = new JLocalizedButton("application", "JPaperSizeInput.savePaper", saveIcon);
        savePaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ResourceBundle messages = ResourceBundle.getBundle("application",
                        ServiceLocator.get(ConfigurationManager.class).get().getLocale());

                String name = null;
                while (true) {
                    name = (String) JOptionPane.showInputDialog(JPaperSizeInput.this,
                            messages.getString("JPaperSizeInput.savePaper.message"),
                            messages.getString("JPaperSizeInput.savePaper.title"), JOptionPane.QUESTION_MESSAGE, null,
                            null, name != null ? name : "");
                    if (name == null)
                        break;

                    boolean nameExists = false;
                    for (UnitDimensionWithLabel dim : userPapers) {
                        if (dim.getLabel().equals(name)) {
                            nameExists = true;
                            break;
                        }
                    }

                    if (nameExists) {
                        JOptionPane.showMessageDialog(JPaperSizeInput.this,
                                messages.getString("JPaperSizeInput.savePaper.nameExists.message"),
                                messages.getString("JPaperSizeInput.savePaper.nameExists.title"),
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        UnitDimension dim = getValue();
                        UnitDimensionWithLabel newDim = new UnitDimensionWithLabel(dim, name);
                        userPapers.add(newDim);

                        UnitDimensionWithLabel selected = null;
                        for (UnitDimensionWithLabel udim : getPapers()) {
                            if (udim.equals(newDim)) {
                                selected = udim;
                            }
                        }
                        if (selected != null)
                            paperSizes.setSelectedItem(selected);

                        break;
                    }
                }
            }
        });

        Icon removeIcon = new ImageIcon(getClass().getResource("/resources/images/remove-24.png"));
        removePaper = new JLocalizedButton("application", "JPaperSizeInput.removePaper", removeIcon);
        removePaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ResourceBundle messages = ResourceBundle.getBundle("application",
                        ServiceLocator.get(ConfigurationManager.class).get().getLocale());

                if (JOptionPane.showConfirmDialog(JPaperSizeInput.this,
                        messages.getString("JPaperSizeInput.removePaper.message"),
                        messages.getString("JPaperSizeInput.removePaper.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    userPapers.remove(paperSizes.getSelectedItem());
                    paperSizes.setSelectedItem(customSize);
                }
            }
        });
    }

    @Override
    protected void buildLayout()
    {
        CellConstraints cc = new CellConstraints();

        JPanel paperPanel = new JPanel(new FormLayout("pref,$ugap,pref,pref", "pref"));
        paperPanel.add(paperSizes, cc.xy(1, 1));
        paperPanel.add(savePaper, cc.xy(3, 1));
        paperPanel.add(removePaper, cc.xy(4, 1));

        JPanel widthPanel = new JPanel(new FormLayout("pref:grow,$lcgap,pref", "pref"));
        widthPanel.add(widthLabel, cc.xy(1, 1));
        widthPanel.add(width, cc.xy(3, 1));

        JPanel heightPanel = new JPanel(new FormLayout("pref:grow,$lcgap,pref", "pref"));
        heightPanel.add(heightLabel, cc.xy(1, 1));
        heightPanel.add(height, cc.xy(3, 1));

        JPanel unitPanel = new JPanel(new FormLayout("pref:grow,$lcgap,pref", "pref"));
        unitPanel.add(unitLabel, cc.xy(1, 1));
        unitPanel.add(unit, cc.xy(3, 1));

        JPanel unitDimPanel = new JPanel(new FormLayout("pref,$ugap:grow,pref", "pref,$lgap,pref"));
        unitDimPanel.add(widthPanel, cc.xy(1, 1));
        unitDimPanel.add(heightPanel, cc.xy(1, 3));
        unitDimPanel.add(unitPanel, cc.xy(3, 1));
        unitDimPanel.add(preserveRatio, cc.xy(3, 3));

        JPanel aspectPanel = new JPanel(new FormLayout("pref,$lcgap,pref,0px:grow,pref", "pref"));
        aspectPanel.add(aspectRatioLabel, cc.xy(1, 1));
        aspectPanel.add(aspectRatioDisplay, cc.xy(3, 1));
        aspectPanel.add(rotatePaper, cc.xy(5, 1));

        JPanel refDimPanel = new JPanel(new FormLayout("pref,$lcgap,pref,$ugap:grow,pref,$lcgap,pref", "pref"));
        refDimPanel.add(refLengthLabel, cc.xy(1, 1));
        refDimPanel.add(refLength, cc.xy(3, 1));
        refDimPanel.add(refUnitLabel, cc.xy(5, 1));
        refDimPanel.add(refUnit, cc.xy(7, 1));

        setLayout(new FormLayout("pref", "pref,$lgap,pref,$lgap,pref,$lgap,pref,$lgap,pref"));
        add(paperPanel, cc.xy(1, 1));
        add(unitDimPanel, cc.xy(1, 3));
        add(aspectPanel, cc.xy(1, 5));
        add(refLabel, cc.xy(1, 7));
        add(refDimPanel, cc.xy(1, 9));
    }

    @Override
    public void setValue(UnitDimension value)
    {
        super.setValue(value);
        if (!setValueWithoutSideEffects && customSize != null)
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

        if (userPapers == null)
            userPapers = ServiceLocator.get(ConfigurationManager.class).get().getPapers();
        result.addAll(userPapers);

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

}
