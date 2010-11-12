/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JImage;
import javax.swing.JLabel;
import javax.swing.JMultilineLabel;
import javax.swing.JPanel;
import javax.swing.RoundedBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.common.Author;
import cz.cuni.mff.peckam.java.origamist.common.BinaryImage;
import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Image;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;

/**
 * A cell displaying information about a {@link cz.cuni.mff.peckam.java.origamist.files.File File} object.
 * 
 * @author Martin Pecka
 */
public class FileCell extends JPanel
{

    /** */
    private static final long        serialVersionUID         = -6710327410498511670L;

    /** The file being displayed. */
    protected File                   file;

    /** The thumbnail that belongs to the model. */
    final protected JImage           thumbnail                = new JImage(null);

    /** Name of the model. */
    final JLabel                     name                     = new JLabel("");

    /** Label for the name of the author of the model. */
    final protected JLabel           authorLabel              = new JLabel("");

    /** Name of the author of the model. */
    final protected JLabel           author                   = new JLabel("");

    /** The component containing <code>author</code> and <code>authorLabel</code> components. */
    final protected JPanel           authorPanel              = new JPanel();

    /** Short description of the model. */
    final protected JMultilineLabel  desc                     = new JMultilineLabel("");

    /** The currently used locale. */
    protected Locale                 l                        = null;

    /** The currently used resource bundle. */
    protected ResourceBundle         messages                 = null;

    /** The listener to be invoked when the diagram locale changes or if the file changes. */
    protected PropertyChangeListener localeListener           = null;

    /**
     * If no file thumbnail image was available at loading time and the origami wasn't loaded, we run this listener when
     * the origami gets loaded and get the thumbnail out of it.
     */
    protected PropertyChangeListener origamiLoadedListener    = null;

    /** The listener to fire when the origamis's thumbnail gets changed. */
    protected PropertyChangeListener origamiThumbnailListener = null;

    /** The listener to fire when the file's thumbnail gets changed. */
    protected PropertyChangeListener thumbnailListener        = null;

    /** The listener to fire when the file's origami gets changed. */
    protected PropertyChangeListener origamiListener          = null;

    /** The listener to fire when the file's author's name gets changed. */
    protected PropertyChangeListener authorNameListener       = null;

    /** The listener to fire when the file's author gets changed. */
    protected PropertyChangeListener authorListener           = null;

    /** The listener to fire when the file's name gets changed. */
    protected Observer<LangString>   nameListener             = null;

    /** The listener to fire when the file's description gets changed. */
    protected Observer<LangString>   descListener             = null;

    public FileCell(File file)
    {
        super(true);

        if (file == null)
            throw new IllegalArgumentException("Cannot set a file to display in a FileCell to null.");

        FormLayout layout = new FormLayout("$dmargin,30dlu,$lcgap,[30dlu,pref,100dlu],$dmargin",
                "$dmargin,pref,$lgap,pref,$lgap,pref,$dmargin");
        layout.setRowGroups(new int[][] { { 2, 4 } });
        this.setLayout(layout);

        this.setBackground(new Color(0, 0, 0, 0)); // fully transparent color
        this.setOpaque(false);
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 2, 0),
                new RoundedBorder(20, 1, Color.LIGHT_GRAY)));

        this.file = file;

        setupListeners();

        // loads the contents of the labels
        setFile(file);

        name.setFont(name.getFont().deriveFont(Font.BOLD));
        author.setFont(author.getFont().deriveFont(Font.PLAIN));
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.BOLD));
        desc.setFont(author.getFont());
        thumbnail.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        thumbnail.setAlignmentY(JComponent.TOP_ALIGNMENT);

        CellConstraints cc = new CellConstraints();

        authorPanel.setLayout(new FormLayout("left:pref,pref", "pref"));
        authorPanel.setOpaque(false);
        authorPanel.add(authorLabel, cc.xy(1, 1));
        authorPanel.add(author, cc.xy(2, 1));

        this.add(thumbnail, cc.xywh(2, 2, 1, 5));
        this.add(name, cc.xy(4, 2));
        this.add(authorPanel, cc.xy(4, 4));
        this.add(desc, cc.xy(4, 6));
    }

    /**
     * Sets up all member event listeners and observers.
     */
    protected void setupListeners()
    {
        localeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("diagramLocale") || evt.getPropertyName().equals("file")) {
                    l = (Locale) evt.getNewValue();
                    messages = ResourceBundle.getBundle("viewer", l);

                    setNameText(FileCell.this.file.getName(l));

                    setAuthorLabelText(messages.getString("authorLabel"));
                    setAuthorText(FileCell.this.file.getAuthor().getName());

                    if (FileCell.this.file.getShortdesc().size() > 0) {
                        setDescText(FileCell.this.file.getShortDesc(l));
                    } else {
                        setDescText("");
                    }
                }
            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("diagramLocale", localeListener);
        l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
        localeListener.propertyChange(new PropertyChangeEvent(this, "locale", null, l));

        origamiThumbnailListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("thumbnail") && evt.getNewValue() != null) {
                    if (FileCell.this.thumbnail.getImage() != null && FileCell.this.file.getThumbnail() != null)
                        return;
                    if (evt.getNewValue() != null) {
                        ImageIcon thumbnail = ((BinaryImage) ((Image) evt.getNewValue()).getImage()).getImageIcon();
                        FileCell.this.thumbnail.setImage(thumbnail.getImage());
                    }
                }
            }
        };

        origamiLoadedListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("isOrigamiLoaded") || evt.getPropertyName().equals("origami")) {
                    FileCell.this.file.fillFromOrigami();
                }
            }
        };

        origamiListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("origami")) {
                    origamiLoadedListener.propertyChange(evt);
                    ((Origami) evt.getNewValue()).addPropertyChangeListener("thumbnail", origamiThumbnailListener);
                }
            }
        };

        thumbnailListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("thumbnail") && evt.getNewValue() != null) {
                    thumbnail.setImage(((BinaryImage) FileCell.this.file.getThumbnail().getImage()).getImageIcon()
                            .getImage());
                }
            }
        };

        authorNameListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("name")) {
                    author.setText(((Author) evt.getSource()).getName());
                }
            }
        };

        authorListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("author")) {
                    if (evt.getSource() != null)
                        FileCell.this.author.addPropertyChangeListener("name", authorNameListener);
                }
            }
        };
        authorListener.propertyChange(new PropertyChangeEvent(this, "author", null, file.getAuthor()));

        nameListener = new Observer<LangString>() {
            @Override
            public void changePerformed(ChangeNotification<LangString> change)
            {
                if (change.getChangeType() == ChangeTypes.REMOVE || change.getChangeType() == ChangeTypes.CHANGE) {
                    if (change.getOldItem() != null && change.getOldItem().getLang().equals(l))
                        setNameText(FileCell.this.file.getName(l));
                }
                if (change.getChangeType() == ChangeTypes.ADD || change.getChangeType() == ChangeTypes.CHANGE) {
                    if (change.getItem() != null && change.getItem().getLang().equals(l))
                        setNameText(change.getItem().getValue());
                }
            }
        };

        descListener = new Observer<LangString>() {
            @Override
            public void changePerformed(ChangeNotification<LangString> change)
            {
                if (change.getChangeType() == ChangeTypes.REMOVE || change.getChangeType() == ChangeTypes.CHANGE) {
                    if (change.getOldItem() != null && change.getOldItem().getLang().equals(l))
                        setDescText(FileCell.this.file.getShortDesc(l));
                }
                if (change.getChangeType() == ChangeTypes.ADD || change.getChangeType() == ChangeTypes.CHANGE) {
                    if (change.getItem() != null && change.getItem().getLang().equals(l))
                        setDescText(change.getItem().getValue());
                }
            }
        };
    }

    /**
     * @param name The new text displayed in the <code>name</code> label.
     */
    protected void setNameText(String name)
    {
        this.name.setText(name);
        this.name.setToolTipText(name);
    }

    /**
     * @param name The new text displayed in the <code>author</code> label.
     */
    protected void setAuthorText(String author)
    {
        this.author.setText(author);
        this.authorPanel.setToolTipText(author);
    }

    /**
     * @param name The new text displayed in the <code>author</code> description label.
     */
    protected void setAuthorLabelText(String label)
    {
        this.authorLabel.setText(label + ": ");
    }

    /**
     * @param name The new text displayed in the <code>desc</code> label.
     */
    protected void setDescText(String desc)
    {
        if (desc == null || desc.length() == 0)
            this.desc.setText("");
        else
            this.desc.setText("<html><b>" + messages.getString("shortDescLabel") + ": </b>" + desc + "</html>");
    }

    public void setFile(File file)
    {
        if (file == null)
            throw new NullPointerException("Cannot set null file to FileCell!");

        this.file = file;
        file.addPropertyChangeListener("thumbnail", thumbnailListener);
        file.addPropertyChangeListener("isOrigamiLoaded", origamiLoadedListener);
        file.addPropertyChangeListener("origami", origamiListener);
        file.addPropertyChangeListener("thumbnail", thumbnailListener);
        file.addPropertyChangeListener("author", authorListener);
        ((ObservableList<LangString>) file.getName()).addObserver(nameListener);
        ((ObservableList<LangString>) file.getShortdesc()).addObserver(descListener);

        // change the values of the locale-dependent fields
        localeListener.propertyChange(new PropertyChangeEvent(this, "file", null, l));

        if (file.getThumbnail() != null) {
            thumbnail.setImage(((BinaryImage) file.getThumbnail().getImage()).getImageIcon().getImage());
        } else if (file.isOrigamiLoaded()) {
            try {
                if (file.getOrigami().getThumbnail() != null) {
                    thumbnail.setImage(((BinaryImage) file.getOrigami().getThumbnail().getImage()).getImageIcon()
                            .getImage());
                }
            } catch (UnsupportedDataFormatException e) {
                assert false;
            } catch (IOException e) {
                assert false;
            }
        }

    }

    @Override
    public String getToolTipText(MouseEvent event)
    {
        for (Component c : this.getComponents()) {
            int x = event.getX() - c.getX();
            int y = event.getY() - c.getY();
            if (c.contains(x, y)) {
                return ((JComponent) c).getToolTipText();
            }
        }
        return super.getToolTipText(event);
    }

    @Override
    public Point getToolTipLocation(MouseEvent event)
    {
        for (Component c : this.getComponents()) {
            int x = event.getX() - c.getX();
            int y = event.getY() - c.getY();
            if (c.contains(x, y)) {
                Point loc = c.getLocation();
                if (c == authorPanel)
                    loc.x += author.getX();
                return loc;
            }
        }
        return super.getToolTipLocation(event);
    }
}
