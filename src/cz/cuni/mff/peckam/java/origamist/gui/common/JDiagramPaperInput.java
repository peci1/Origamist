/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.origamist.JMultilineLabel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.model.DiagramPaper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramColors;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An input for diagram paper properties.
 * 
 * @author Martin Pecka
 */
public class JDiagramPaperInput extends JPanel
{
    /** */
    private static final long     serialVersionUID = 8558334697448830375L;

    /** The paper that is the value of this input. */
    protected DiagramPaper        paper;

    /** Input for paper size. */
    protected JPaperSizeInput     size;
    /** Input for paper background color. */
    protected JSimpleColorChooser background;
    /** Input for the number of columns of the paper. */
    protected JSpinner            cols;
    /** Input for the number of rows of the paper. */
    protected JSpinner            rows;

    /** Label. */
    protected JLabel              backgroundLabel, colsLabel, rowsLabel;
    /** Label. */
    protected JMultilineLabel     rowsColsHelpLabel;

    /**
     * @param paper The paper that is the value of this input.
     */
    public JDiagramPaperInput(DiagramPaper paper)
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

        backgroundLabel = new JLocalizedLabelWithTooltip("application", "JDiagramPaperInput.backgroundLabel");
        colsLabel = new JLocalizedLabelWithTooltip("application", "JDiagramPaperInput.colsLabel");
        rowsLabel = new JLocalizedLabelWithTooltip("application", "JDiagramPaperInput.rowsLabel");
        rowsColsHelpLabel = new JMultilineLabelWithTooltip("");
        rowsColsHelpLabel.setDisableLastLineHack(true);
        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JDiagramPaperInput.rowsColsHelpLabel") {
            @Override
            protected void updateText(String text)
            {
                rowsColsHelpLabel.setText(text);
            }
        });

        size = new JPaperSizeInput();
        size.setValue(paper.getSize());

        background = new JSimpleColorChooser(paper.getBackgroundColor());

        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JDiagramPaperInput.colorChooserDialogTitle") {
            @Override
            protected void updateText(String text)
            {
                background.getChooserDialog().setTitle(text);
            }
        });
        conf.addAndRunResourceBundleListener(new Configuration.LocaleListener("application",
                "JDiagramPaperInput.showColorChooserButtonText") {
            @Override
            protected void updateText(String text)
            {
                background.getShowChooserButton().setText(text);
            }
        });

        cols = new JSpinner(new SpinnerNumberModel(paper.getCols() != null ? paper.getCols() : 1, 1, null, 1));
        rows = new JSpinner(new SpinnerNumberModel(paper.getRows() != null ? paper.getRows() : 1, 1, null, 1));
    }

    /**
     * Add components to layout.
     */
    protected void buildLayout()
    {
        CellConstraints cc = new CellConstraints();
        setLayout(new FormLayout("pref,$ugap,pref,$lcgap,pref", "pref,$lgap,pref,$lgap,pref,$lgap,pref"));

        add(size, cc.xywh(1, 1, 1, 7));
        add(backgroundLabel, cc.xy(3, 3));
        add(background, cc.xy(5, 3));
        add(rowsColsHelpLabel, cc.xyw(3, 5, 3));
        add(colsLabel, cc.xy(3, 7));

        // adding these components to a panel is a workaround for the fact that FormLayout doesn't take components that
        // span multiple columns into account when computing the grid size
        JPanel panel = new JPanel();
        panel.setLayout(new FormLayout("pref,$lcgap,pref,$ugap,pref,$lcgap,pref", "pref"));
        panel.add(cols, cc.xy(3, 1));
        panel.add(rowsLabel, cc.xy(5, 1));
        panel.add(rows, cc.xy(7, 1));

        add(panel, cc.xy(5, 7));
    }

    /**
     * @return The paper that is the value of this input.
     */
    public DiagramPaper getPaper()
    {
        DiagramPaper result = new DiagramPaper();
        result.setSize(size.getValue());
        result.setColor(new DiagramColors());
        result.setBackgroundColor(background.getColor());
        result.setCols(paper.getCols());
        result.setRows(paper.getRows());
        return result;
    }

    /**
     * @param paper The paper that is the value of this input.
     */
    public void setPaper(DiagramPaper paper)
    {
        this.paper = paper;
    }
}
