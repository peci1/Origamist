/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.origamist.JHideablePanel;
import javax.swing.origamist.JLocalizedLabel;
import javax.swing.origamist.JMultilineLabel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.gui.JLabelWithTooltip;
import cz.cuni.mff.peckam.java.origamist.gui.JLocalizedLabelWithTooltip;
import cz.cuni.mff.peckam.java.origamist.gui.JMultilineLabelWithTooltip;
import cz.cuni.mff.peckam.java.origamist.model.ModelPaper;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * Panel with info about a model.
 * 
 * @author Martin Pecka
 */
public class ModelInfoPanel extends JHideablePanel
{
    /** */
    private static final long        serialVersionUID    = 9041458561866387156L;
    protected Origami                origami;

    /** The listener for locale changes. */
    protected PropertyChangeListener localeListener;

    protected JLocalizedLabel        nameDesc            = new JLocalizedLabelWithTooltip("viewer", "nameLabel", "%1s:");
    protected JLabel                 name                = new JLabelWithTooltip();
    protected JLocalizedLabel        authorDesc          = new JLocalizedLabelWithTooltip("viewer", "authorLabel",
                                                                 "%1s:");
    protected JMultilineLabel        author              = new JMultilineLabelWithTooltip("");
    protected JLocalizedLabel        licenseDesc         = new JLocalizedLabelWithTooltip("viewer", "licenseLabel",
                                                                 "%1s:");
    protected JLabel                 license             = new JLabelWithTooltip();
    protected JLocalizedLabel        yearDesc            = new JLocalizedLabelWithTooltip("viewer", "yearLabel", "%1s:");
    protected JLabel                 year                = new JLabelWithTooltip();
    protected JLocalizedLabel        originalDesc        = new JLocalizedLabelWithTooltip("viewer", "originalLabel",
                                                                 "%1s:");
    protected JLabel                 original            = new JLabelWithTooltip();
    protected JLocalizedLabel        paperDesc           = new JLocalizedLabelWithTooltip("viewer", "paperLabel");
    protected JLocalizedLabel        paperDimensionDesc  = new JLocalizedLabelWithTooltip("viewer",
                                                                 "paperDimensionLabel", "   %1s:");
    protected JLabel                 paperDimension      = new JLabelWithTooltip();
    protected JLocalizedLabel        paperNoteDesc       = new JLocalizedLabelWithTooltip("viewer", "paperNoteLabel",
                                                                 "   %1s:");
    protected JLabel                 paperNote           = new JLabelWithTooltip();
    protected JLocalizedLabel        paperForegroundDesc = new JLocalizedLabelWithTooltip("viewer",
                                                                 "paperForegroundLabel", "   %1s:");
    protected JTextField             paperForeground     = new JTextField();
    protected JLocalizedLabel        paperBackgroundDesc = new JLocalizedLabelWithTooltip("viewer",
                                                                 "paperBackgroundLabel", "   %1s:");
    protected JTextField             paperBackground     = new JTextField();
    protected JLocalizedLabel        paperWeightDesc     = new JLocalizedLabelWithTooltip("viewer", "paperWeightLabel",
                                                                 "   %1s:");
    protected JLabel                 paperWeight         = new JLabelWithTooltip();
    protected JLocalizedLabel        descDesc            = new JLocalizedLabelWithTooltip("viewer", "longDescLabel",
                                                                 "%1s:");
    protected JMultilineLabel        desc                = new JMultilineLabelWithTooltip("");

    public ModelInfoPanel(Origami origami)
    {
        super(BorderLayout.NORTH);

        paperBackground.setOpaque(true);
        paperForeground.setOpaque(true);
        paperBackground.setFont(name.getFont());
        paperForeground.setFont(name.getFont());
        paperBackground.setEditable(false);
        paperForeground.setEditable(false);
        paperBackground.setBorder(BorderFactory.createEmptyBorder());
        paperForeground.setBorder(BorderFactory.createEmptyBorder());

        author.setDisableLastLineHack(true);
        author.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e)
            {
                if (e.getEventType().equals(EventType.ACTIVATED) && Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException e1) {} catch (URISyntaxException e1) {}
                }
            }
        });

        localeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                Locale locale = (Locale) evt.getNewValue();

                name.setText(ModelInfoPanel.this.origami.getName(locale));
                String homePage = ResourceBundle.getBundle("viewer", locale).getString("author.homepage");
                author.setText("<html>"
                        + ModelInfoPanel.this.origami.getAuthor().getName()
                        + (ModelInfoPanel.this.origami.getAuthor().getHomepage() == null ? "" : " (<a href=\""
                                + ModelInfoPanel.this.origami.getAuthor().getHomepage() + "\">" + homePage + "</a>)")
                        + "</html>");
                paperDimension.setText(((ModelPaper) ModelInfoPanel.this.origami.getModel().getPaper()).getSize()
                        .toString());
                paperNote.setText(((ModelPaper) ModelInfoPanel.this.origami.getModel().getPaper()).getNote(locale));
                desc.setText(ModelInfoPanel.this.origami.getDescription(locale));

            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("locale", localeListener);

        setOrigami(origami);

        buildLayout();
    }

    /**
     * Add components to the layout.
     */
    protected void buildLayout()
    {
        DefaultFormBuilder b = new DefaultFormBuilder(new FormLayout("min(default;60dlu),$lcgap,min(default;85dlu)"),
                content);
        b.append(nameDesc);
        b.append(name);
        b.nextLine();
        b.append(authorDesc);
        b.append(author);
        b.nextLine();
        b.append(licenseDesc);
        b.append(license);
        b.nextLine();
        b.append(yearDesc);
        b.append(year);
        b.nextLine();
        b.append(originalDesc);
        b.append(original);
        b.nextLine();
        b.append(paperDesc);
        b.nextLine();
        b.append(paperDimensionDesc);
        b.append(paperDimension);
        b.nextLine();
        b.append(paperNoteDesc);
        b.append(paperNote);
        b.nextLine();
        b.append(paperForegroundDesc);
        b.append(paperForeground);
        b.nextLine();
        b.append(paperBackgroundDesc);
        b.append(paperBackground);
        b.nextLine();
        b.append(paperWeightDesc);
        b.append(paperWeight);
        b.nextLine();
        b.append(descDesc);
        b.append(desc);
        b.nextLine();
    }

    /**
     * @return the origami
     */
    public Origami getOrigami()
    {
        return origami;
    }

    /**
     * @param origami the origami to set
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;

        if (origami == null)
            return;

        license.setText(origami.getLicense().getName());

        year.setText(new Integer(origami.getYear().getYear()).toString());

        original.setText(origami.getOriginal() == null ? "" : origami.getOriginal().toString());

        Color c = origami.getModel().getPaper().getColors().getForeground();
        paperForeground.setText("R: " + c.getRed() + ", G: " + c.getGreen() + ", B: " + c.getBlue());
        paperForeground.setBackground(c);

        c = origami.getModel().getPaper().getColors().getBackground();
        paperBackground.setText("R: " + c.getRed() + ", G: " + c.getGreen() + ", B: " + c.getBlue());
        paperBackground.setBackground(c);

        paperWeight.setText(origami.getModel().getPaper().getWeight() + " g/m²");

        localeListener.propertyChange(new PropertyChangeEvent(this, "locale", null, ServiceLocator
                .get(ConfigurationManager.class).get().getLocale()));

        show();

        revalidate();
    }
}
