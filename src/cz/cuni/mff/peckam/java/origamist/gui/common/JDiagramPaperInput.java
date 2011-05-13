/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    /** The preview of the paper. */
    protected DiagramPaperPreview paperPreview;

    /** Label. */
    protected JLabel              backgroundLabel, colsLabel, rowsLabel, paperPreviewLabel;
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
        paperPreviewLabel = new JLocalizedLabelWithTooltip("application", "JDiagramPaperInput.paperPreviewLabel");
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
        size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paperPreview.repaint();
            }
        });

        background = new JSimpleColorChooser(paper.getBackgroundColor() != null ? paper.getBackgroundColor()
                : Color.WHITE);
        background.getColorChooser().getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paper.setBackgroundColor(background.getColorChooser().getColor());
                paperPreview.repaint();
            }
        });

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
        cols.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paper.setCols((Integer) cols.getValue());
                paperPreview.repaint();
            }
        });
        rows = new JSpinner(new SpinnerNumberModel(paper.getRows() != null ? paper.getRows() : 1, 1, null, 1));
        rows.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                paper.setRows((Integer) rows.getValue());
                paperPreview.repaint();
            }
        });

        paperPreview = new DiagramPaperPreview();
    }

    /**
     * Add components to layout.
     */
    protected void buildLayout()
    {
        CellConstraints cc = new CellConstraints();
        setLayout(new FormLayout("pref,$ugap,pref", "top:pref"));

        JPanel leftPanel = new JPanel();
        leftPanel.add(size);

        JPanel rightPanel = new JPanel(new FormLayout("pref,$lcgap,pref", "pref,$lgap,pref,$lgap,pref,$lgap,pref"));
        rightPanel.add(backgroundLabel, cc.xy(1, 1));
        rightPanel.add(background, cc.xy(3, 1));
        rightPanel.add(rowsColsHelpLabel, cc.xyw(1, 3, 3));
        rightPanel.add(colsLabel, cc.xy(1, 5));

        // adding these components to a panel is a workaround for the fact that FormLayout doesn't take components that
        // span multiple columns into account when computing the grid size
        JPanel panel = new JPanel();
        panel.setLayout(new FormLayout("pref,$lcgap,pref,$ugap,pref,$lcgap,pref", "pref"));
        panel.add(cols, cc.xy(3, 1));
        panel.add(rowsLabel, cc.xy(5, 1));
        panel.add(rows, cc.xy(7, 1));

        rightPanel.add(panel, cc.xy(3, 5));

        rightPanel.add(paperPreviewLabel, cc.xy(1, 7));
        rightPanel.add(paperPreview, cc.xy(3, 7));

        add(leftPanel, cc.xy(1, 1));
        add(rightPanel, cc.xy(3, 1));
    }

    /**
     * Disable changes to the size of the paper.
     */
    public void lockSize()
    {
        size.setEnabled(false);
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

    /**
     * Select the first non-custom item.
     */
    public void selectFirstNonCustom()
    {
        size.paperSizes.setSelectedIndex(size.customSize != null ? 1 : 0);
    }

    /**
     * A preview of the diagram paper.
     * 
     * @author Martin Pecka
     */
    protected class DiagramPaperPreview extends JPanel
    {
        /** */
        private static final long serialVersionUID = 3495666034159392170L;

        public DiagramPaperPreview()
        {
            setPreferredSize(new Dimension(48, 48));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double pWidth = (Double) size.width.getValue();
            double pHeight = (Double) size.height.getValue();

            if (pWidth == 0d || pHeight == 0d)
                return;

            double relWidth, relHeight;
            if (pWidth >= pHeight) {
                relWidth = 1d;
                relHeight = pHeight / pWidth;
            } else {
                relWidth = pWidth / pHeight;
                relHeight = 1d;
            }

            int width = (int) (48 * relWidth);
            int height = (int) (48 * relHeight) - 1; // the -1 is important

            int left = (int) Math.floor((48 - width) / 2d);
            int top = (int) Math.floor((48 - height) / 2d);

            g2.setColor(paper.getBackgroundColor() != null ? paper.getBackgroundColor() : Color.WHITE);
            g2.fillRect(left, top, width, height);

            g2.setColor(Color.BLACK);
            g2.drawRect(left, top, width, height);

            if (paper.getCols() == null || paper.getCols() == 0)
                paper.setCols(1);
            if (paper.getRows() == null || paper.getRows() == 0)
                paper.setRows(1);

            int fieldWidth = (int) Math.floor((width - (paper.getCols() + 1)) / ((double) paper.getCols()));
            int fieldHeight = (int) Math.floor((height - (paper.getRows() + 1)) / ((double) paper.getRows()));

            for (int x = left + 1, i = 0; i < paper.getCols(); x += fieldWidth + 1, i++) {
                for (int y = top + 1, j = 0; j < paper.getRows(); y += fieldHeight + 1, j++) {
                    g2.drawRect(x, y, fieldWidth, fieldHeight);
                }
            }
        }
    }
}
