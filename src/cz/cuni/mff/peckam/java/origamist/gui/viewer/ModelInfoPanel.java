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
import java.security.Permission;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
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

import cz.cuni.mff.peckam.java.origamist.common.License;
import cz.cuni.mff.peckam.java.origamist.gui.common.JLabelWithTooltip;
import cz.cuni.mff.peckam.java.origamist.gui.common.JLocalizedLabelWithTooltip;
import cz.cuni.mff.peckam.java.origamist.gui.common.JMultilineLabelWithTooltip;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
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

    /** The origami this panel displays info of. */
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
    protected JLabel                 license             = new JLabel();
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
        this(origami, BorderLayout.NORTH);
    }

    public ModelInfoPanel(Origami origami, String orientation)
    {
        super(orientation);

        setOpaque(true);
        getContent().setOpaque(true);

        hideButton.setBackground(new Color(231, 231, 189));
        getContent().setBackground(new Color(250, 250, 242));

        hideButton.setBorder(BorderFactory.createEmptyBorder());

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
                Locale locale = ServiceLocator.get(ConfigurationManager.class).get().getLocale();

                name.setText(ModelInfoPanel.this.origami.getName(locale));
                String homePage = ResourceBundle.getBundle("viewer", locale).getString("author.homepage");
                author.setText("<html>"
                        + ModelInfoPanel.this.origami.getAuthor().getName()
                        + (ModelInfoPanel.this.origami.getAuthor().getHomepage() == null ? "" : " (<a href=\""
                                + ModelInfoPanel.this.origami.getAuthor().getHomepage() + "\">" + homePage + "</a>)")
                        + "</html>");

                license.setToolTipText(createLicenseTooltip(ModelInfoPanel.this.origami.getLicense()));

                if (ModelInfoPanel.this.origami.getYear() != null) {
                    Calendar cal = new GregorianCalendar(locale);
                    cal.set(ModelInfoPanel.this.origami.getYear().getYear(), 1, 1);
                    year.setText(new SimpleDateFormat("yyyy", locale).format(cal.getTime()));
                } else {
                    year.setText("");
                }

                paperDimension.setText((ModelInfoPanel.this.origami.getModel().getPaper()).getSize().toString(true));

                paperNote.setText((ModelInfoPanel.this.origami.getModel().getPaper()).getNote(locale));
                paperNote.setVisible(!(paperNote.getText() == null || paperNote.getText().equals("")));
                paperNoteDesc.setVisible(paperNote.isVisible());
                paperWeight.setText(MessageFormat.format(
                        ResourceBundle.getBundle("application", locale).getString("units.gramm_per_meter2"),
                        ModelInfoPanel.this.origami.getModel().getPaper().getWeight()));

                desc.setText(ModelInfoPanel.this.origami.getDescription(locale));
                desc.setVisible(!(desc.getText() == null || desc.getText().equals("")));
                descDesc.setVisible(desc.isVisible());

            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("locale", localeListener);
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("preferredUnit", localeListener);

        setOrigami(origami);

        buildLayout();
    }

    /**
     * Add components to the layout.
     */
    protected void buildLayout()
    {
        DefaultFormBuilder b = new DefaultFormBuilder(new FormLayout("min(default;60dlu),$lcgap,min(default;100dlu)"),
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

        original.setText(origami.getOriginal() == null ? "" : origami.getOriginal().toString());
        original.setVisible(!(original.getText() == null || original.getText().equals("")));
        originalDesc.setVisible(original.isVisible());

        Color c = origami.getModel().getPaper().getColors().getForeground();
        paperForeground.setText("R: " + c.getRed() + ", G: " + c.getGreen() + ", B: " + c.getBlue());
        paperForeground.setBackground(c);

        c = origami.getModel().getPaper().getColors().getBackground();
        paperBackground.setText("R: " + c.getRed() + ", G: " + c.getGreen() + ", B: " + c.getBlue());
        paperBackground.setBackground(c);

        localeListener.propertyChange(new PropertyChangeEvent(this, "locale", null, ServiceLocator
                .get(ConfigurationManager.class).get().getLocale()));

        show();

        revalidate();
    }

    /**
     * Return the tooltip displaying information about the given license.
     * 
     * @param license The license the tooltip will be generated from.
     * @return The tooltip displaying information about the given license.
     */
    protected String createLicenseTooltip(License license)
    {
        StringBuilder tooltip = new StringBuilder();

        Locale l = ServiceLocator.get(ConfigurationManager.class).get().getLocale();
        ResourceBundle viewerMessages = ResourceBundle.getBundle("viewer", l);
        ResourceBundle messages = ResourceBundle.getBundle("application", l);

        tooltip.append("<html>").append(
                "<head><style>body {width: 500px;} ul {padding: 0px; margin: 0px; margin-left: 20px;}</style></head>");
        tooltip.append("<body>");
        tooltip.append("<h1>" + license.getName() + "</h1>").append(
                viewerMessages.getString("license.tooltip.youArePermittedTo"));
        if (license.getPermission().size() == 0) {
            tooltip.append(messages.getString("permission.doNothing"));
        } else {
            tooltip.append("<ul>");
            for (Permission p : license.getPermission()) {
                String name = p.getName();
                try {
                    name = messages.getString("permission." + name);
                } catch (MissingResourceException e) {}
                tooltip.append("<li>" + name + "</li>");
            }
            tooltip.append("</ul>");
        }

        if (license.getHomepage() != null) {
            tooltip.append("<b>" + viewerMessages.getString("license.tooltip.homepage") + "</b>: "
                    + license.getHomepage());
        }

        if (license.getContent() != null) {
            tooltip.append("<h2>" + viewerMessages.getString("license.tooltip.licenseText") + "</h2>");
            tooltip.append("<p>" + license.getContent() + "</p>");
        }
        tooltip.append("</body>");
        tooltip.append("</html>");

        return ServiceLocator.get(TooltipFactory.class).getPlain(tooltip.toString());
    }
}
