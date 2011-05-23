/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.origamist.JMultilineLabel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.gui.common.JLocaleComboBox;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * Formats an origami can be exported to.
 * 
 * @author Martin Pecka
 */
public enum ExportFormat
{
    /** The native format. */
    XML
    {

        @Override
        public int getNumOfProgressChunks(Origami o)
        {
            return 1;
        }

        @Override
        public ExportOptions askForOptions(Origami o)
        {
            return null;
        }

    },
    /** PDF document. */
    PDF
    {

        @Override
        public int getNumOfProgressChunks(Origami o)
        {
            return o.getModel().getSteps().getStep().size() + 2 * o.getNumberOfPages();
        }

        @Override
        public ExportOptions askForOptions(Origami o)
        {
            final PDFExportOptions result = new PDFExportOptions();

            final OptionsDialog dialog = new OptionsDialog(o);
            dialog.setTitle(new LocalizedString(ExportFormat.class.getName(), "PDF.options.title").toString());

            dialog.dpi.setValue(result.getDpi());
            dialog.withBackground.setSelected(result.isWithBackground());
            dialog.insetsBottom.setValue(result.getPageInsets().bottom);
            dialog.insetsTop.setValue(result.getPageInsets().top);
            dialog.insetsLeft.setValue(result.getPageInsets().left);
            dialog.insetsRight.setValue(result.getPageInsets().right);

            dialog.setVisible(true);

            if (dialog.approved) {
                result.setDpi((Double) dialog.dpi.getValue());
                result.setWithBackground(dialog.withBackground.isSelected());
                result.setLocale((Locale) dialog.localeCombo.getSelectedItem());
                result.setPageInsets(new Insets((Integer) dialog.insetsTop.getValue(), (Integer) dialog.insetsLeft
                        .getValue(), (Integer) dialog.insetsBottom.getValue(), (Integer) dialog.insetsRight.getValue()));
            }

            return result;
        }

    },
    /** SVG vector graphics. */
    SVG
    {
        @Override
        public int getNumOfProgressChunks(Origami o)
        {
            return o.getModel().getSteps().getStep().size();
        }

        @Override
        public ExportOptions askForOptions(Origami o)
        {
            final SVGExportOptions result = new SVGExportOptions();

            final OptionsDialog dialog = new OptionsDialog(o);
            dialog.setTitle(new LocalizedString(ExportFormat.class.getName(), "SVG.options.title").toString());

            dialog.dpi.setValue(result.getDpi());
            dialog.withBackground.setSelected(result.isWithBackground());
            dialog.insetsBottom.setValue(result.getPageInsets().bottom);
            dialog.insetsTop.setValue(result.getPageInsets().top);
            dialog.insetsLeft.setValue(result.getPageInsets().left);
            dialog.insetsRight.setValue(result.getPageInsets().right);

            dialog.setVisible(true);

            if (dialog.approved) {
                result.setDpi((Double) dialog.dpi.getValue());
                result.setWithBackground(dialog.withBackground.isSelected());
                result.setLocale((Locale) dialog.localeCombo.getSelectedItem());
                result.setPageInsets(new Insets((Integer) dialog.insetsTop.getValue(), (Integer) dialog.insetsLeft
                        .getValue(), (Integer) dialog.insetsBottom.getValue(), (Integer) dialog.insetsRight.getValue()));
            }

            return result;
        }
    },
    /** PNG file(s). */
    PNG
    {
        @Override
        public int getNumOfProgressChunks(Origami o)
        {
            return o.getModel().getSteps().getStep().size();
        }

        @Override
        public ExportOptions askForOptions(Origami o)
        {
            final PNGExportOptions result = new PNGExportOptions();

            final OptionsDialog dialog = new OptionsDialog(o);
            dialog.setTitle(new LocalizedString(ExportFormat.class.getName(), "PNG.options.title").toString());

            dialog.dpi.setValue(result.getDpi());
            dialog.withBackground.setSelected(result.isWithBackground());
            dialog.insetsBottom.setValue(result.getPageInsets().bottom);
            dialog.insetsTop.setValue(result.getPageInsets().top);
            dialog.insetsLeft.setValue(result.getPageInsets().left);
            dialog.insetsRight.setValue(result.getPageInsets().right);

            dialog.setVisible(true);

            if (dialog.approved) {
                result.setDpi((Double) dialog.dpi.getValue());
                result.setWithBackground(dialog.withBackground.isSelected());
                result.setLocale((Locale) dialog.localeCombo.getSelectedItem());
                result.setPageInsets(new Insets((Integer) dialog.insetsTop.getValue(), (Integer) dialog.insetsLeft
                        .getValue(), (Integer) dialog.insetsBottom.getValue(), (Integer) dialog.insetsRight.getValue()));
            }

            return result;
        }
    };

    /**
     * Return the number of progress steps generated by exporting the given origami to this format.
     * 
     * @param o The exported origami.
     * @return The number of progress steps.
     */
    public abstract int getNumOfProgressChunks(Origami o);

    /**
     * This method should be called on EDT.
     * 
     * @param o The origami this dialog can use to setup itself.
     * 
     * @return Options to be used for exporting to this format. The options are gotten from the user in a dialog this
     *         method shows. <code>null</code> is returned if the format has no options.
     */
    public abstract ExportOptions askForOptions(Origami o);

    protected class OptionsDialog extends JDialog
    {
        /** */
        private static final long serialVersionUID = -451472224433073936L;

        protected boolean         approved         = false;

        protected JLocaleComboBox localeCombo;
        protected JSpinner        dpi;
        protected JCheckBox       withBackground;
        protected JSpinner        insetsTop;
        protected JSpinner        insetsRight;
        protected JSpinner        insetsBottom;
        protected JSpinner        insetsLeft;
        protected JButton         okButton;
        protected JButton         cancelButton;
        protected Origami         o;
        protected PanelBuilder    builder;

        public OptionsDialog(Origami o)
        {
            this.o = o;

            setModalityType(ModalityType.APPLICATION_MODAL);

            createComponents();

            buildLayout();

            finishLayout();

            pack();

            setLocationRelativeTo(null);
        }

        protected void createComponents()
        {
            LinkedHashSet<Locale> diagramLocales = new LinkedHashSet<Locale>();
            for (Step step : o.getModel().getSteps().getStep()) {
                for (LangString s : step.getDescription())
                    diagramLocales.add(s.getLang());
            }
            for (LangString s : o.getDescription())
                diagramLocales.add(s.getLang());
            for (LangString s : o.getShortdesc())
                diagramLocales.add(s.getLang());

            localeCombo = new JLocaleComboBox(diagramLocales);

            dpi = new JSpinner(new SpinnerNumberModel(72d, 1d, 600d, 1d));

            withBackground = new JCheckBox();

            insetsTop = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
            insetsRight = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
            insetsBottom = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
            insetsLeft = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
        }

        protected void buildLayout()
        {
            ResourceBundle bundle = ResourceBundle.getBundle(ExportFormat.class.getName(),
                    ServiceLocator.get(ConfigurationManager.class).get().getLocale());
            ResourceBundle appMessages = ResourceBundle.getBundle("application",
                    ServiceLocator.get(ConfigurationManager.class).get().getLocale());

            okButton = new JButton();
            okButton.setAction(new AbstractAction() {
                /** */
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    approved = true;
                    setVisible(false);
                }
            });
            okButton.setText(appMessages.getString("buttons.ok"));

            cancelButton = new JButton();
            cancelButton.setAction(new AbstractAction() {
                /** */
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    setVisible(false);
                }
            });
            cancelButton.setText(appMessages.getString("buttons.cancel"));

            Container pane = getContentPane();
            pane.setLayout(new FormLayout("fill:default:grow", "fill:default:grow"));
            JPanel panel = new JPanel();
            pane.add(panel, new CellConstraints(1, 1));

            builder = new PanelBuilder(new FormLayout(""), panel);
            builder.appendColumn("$dmargin");
            builder.appendColumn("left:default");
            builder.appendLabelComponentsGapColumn();
            builder.appendColumn("fill:max(default;150px):grow");
            builder.appendColumn("$dmargin");

            builder.appendRow("$dmargin");

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("steps.locale"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(localeCombo);

            builder.appendUnrelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.add(new JMultilineLabel(bundle.getString("dpi.beware")), new CellConstraints(builder.getColumn(),
                    builder.getRow(), 3, 1));

            builder.appendRelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("dpi"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(dpi);

            builder.appendUnrelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("with.background"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(withBackground);

            builder.appendUnrelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("insets.top"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(insetsTop);

            builder.appendRelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("insets.right"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(insetsRight);

            builder.appendRelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("insets.bottom"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(insetsBottom);

            builder.appendRelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("default:grow");
            builder.nextLine();
            builder.nextColumn();
            builder.addLabel(bundle.getString("insets.left"));
            builder.nextColumn();
            builder.nextColumn();
            builder.add(insetsLeft);
        }

        protected void finishLayout()
        {
            builder.appendUnrelatedComponentsGapRow();
            builder.nextLine();

            builder.appendRow("pref");
            builder.nextLine();
            builder.nextColumn();
            builder.add(cancelButton);
            builder.nextColumn();
            builder.nextColumn();
            builder.add(okButton);

            builder.appendRow("$dmargin");
        }
    }
}
