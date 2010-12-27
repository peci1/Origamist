/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.model.ModelPaper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelColors;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An input for model paper properties.
 * 
 * @author Martin Pecka
 */
public class JModelPaperInput extends JPanel
{
    /** */
    private static final long                     serialVersionUID = -4445558165146173602L;

    /** The paper that is the value of this input. */
    protected ModelPaper                          paper;

    /** Input for paper size. */
    protected JPaperSizeInput                     size;
    /** Input for paper background color. */
    protected JSimpleColorChooser                 background;
    /** Input for paper foreground color. */
    protected JSimpleColorChooser                 foreground;
    /** The weight of the paper. */
    protected JSpinner                            weight;
    /** The note on the paper. */
    protected JLangStringListTextField<JTextArea> note;

    /** Label. */
    protected JLabel                              backgroundLabel, foregroundLabel, weightLabel;

    /**
     * @param paper The paper that is the value of this input.
     */
    public JModelPaperInput(ModelPaper paper)
    {
        this.paper = paper;

        createComponents();
        buildLayout();
    }

    /**
     * Create and setup components.
     */
    protected void createComponents()
    {
        Configuration conf = ServiceLocator.get(ConfigurationManager.class).get();

        backgroundLabel = new JLocalizedLabelWithTooltip("application", "JModelPaperInput.backgroundLabel");
        foregroundLabel = new JLocalizedLabelWithTooltip("application", "JModelPaperInput.foregroundLabel");
        weightLabel = new JLocalizedLabelWithTooltip("application", "JModelPaperInput.weightLabel");

        size = new JPaperSizeInput();
        size.setValue(paper.getSize());

        background = new JSimpleColorChooser(paper.getBackgroundColor());
        background.getColorChooser().getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paper.setBackgroundColor(background.getColor());
            }
        });
        foreground = new JSimpleColorChooser(paper.getForegroundColor());
        foreground.getColorChooser().getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paper.setForegroundColor(foreground.getColor());
            }
        });

        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JModelPaperInput.background.colorChooserDialogTitle") {
            @Override
            protected void updateText(String text)
            {
                background.getChooserDialog().setTitle(text);
            }
        });
        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JModelPaperInput.background.showColorChooserButtonText") {
            @Override
            protected void updateText(String text)
            {
                background.getShowChooserButton().setText(text);
            }
        });
        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JModelPaperInput.foreground.colorChooserDialogTitle") {
            @Override
            protected void updateText(String text)
            {
                foreground.getChooserDialog().setTitle(text);
            }
        });
        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JModelPaperInput.foreground.showColorChooserButtonText") {
            @Override
            protected void updateText(String text)
            {
                foreground.getShowChooserButton().setText(text);
            }
        });

        weight = new JSpinner(new SpinnerNumberModel(80d, 0d, null, 1d));
        weight.setEditor(new JSpinner.NumberEditor(weight, "0.0"));
        weight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paper.setWeight((Double) weight.getValue());
            }
        });

        note = new DefaultLangStringListTextArea(paper.getNote());
        final TitledBorder noteBorder = BorderFactory.createTitledBorder(note.getBorder(), "");
        note.setBorder(noteBorder);
        ServiceLocator
                .get(ConfigurationManager.class)
                .get()
                .addAndRunResourceBundleListener(
                        new Configuration.LocaleListener("application", "JModelPaperInput.noteLabel") {

                            @Override
                            protected void updateText(String text)
                            {
                                noteBorder.setTitle(text);
                            }
                        });

    }

    /**
     * Add components to layout.
     */
    protected void buildLayout()
    {
        CellConstraints cc = new CellConstraints();
        setLayout(new FormLayout("pref", "pref,$lgap,pref"));

        JPanel panel1 = new JPanel(new FormLayout("pref,$ugap,pref", "top:pref"));
        JPanel panel2 = new JPanel(new FormLayout("pref,$lcgap,pref", "pref,$lgap,pref,$lgap,pref"));

        add(panel1, cc.xy(1, 1));
        panel1.add(size, cc.xy(1, 1));
        panel1.add(panel2, cc.xy(3, 1));
        panel2.add(backgroundLabel, cc.xy(1, 1));
        panel2.add(background, cc.xy(3, 1));
        panel2.add(foregroundLabel, cc.xy(1, 3));
        panel2.add(foreground, cc.xy(3, 3));
        panel2.add(weightLabel, cc.xy(1, 5));
        panel2.add(weight, cc.xy(3, 5));
        add(note, cc.xy(1, 3));

    }

    /**
     * @return The paper that is the value of this input.
     */
    public ModelPaper getPaper()
    {
        ModelPaper result = new ModelPaper();
        result.setColors(new ModelColors());
        result.setBackgroundColor(background.getColor());
        result.setForegroundColor(foreground.getColor());
        result.setSize(size.getValue());
        result.setWeight((Double) weight.getValue());
        for (LangString s : note.getStrings())
            result.addNote(s.getLang(), s.getValue());
        return result;
    }

    /**
     * @param paper The paper that is the value of this input.
     */
    public void setPaper(ModelPaper paper)
    {
        this.paper = paper;
    }
}
