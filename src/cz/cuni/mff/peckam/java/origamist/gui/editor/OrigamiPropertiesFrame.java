/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Permission;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.origamist.JFileInput;
import javax.swing.origamist.JImage;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import cz.cuni.mff.peckam.java.origamist.gui.common.DefaultLangStringListTextArea;
import cz.cuni.mff.peckam.java.origamist.gui.common.JDiagramPaperInput;
import cz.cuni.mff.peckam.java.origamist.gui.common.JLangStringListTextField;
import cz.cuni.mff.peckam.java.origamist.gui.common.JModelPaperInput;
import cz.cuni.mff.peckam.java.origamist.model.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.TooltipFactory;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.PermissionConverter;
import cz.cuni.mff.peckam.java.origamist.utils.UniversalDocumentListener;

/**
 * A frame for editing metadata of an tempOrigami. It can also be used to fill metadata for a just-created tempOrigami.
 * 
 * @author Martin Pecka
 */
public class OrigamiPropertiesFrame extends JDialog
{
    /** */
    private static final long                      serialVersionUID = -8222843108896744313L;

    /**
     * If <code>null</code>, indicates that the dialog should display texts for creating a model metadata, otherwise it
     * displays texts for editing an existing model's metadata.
     */
    protected Origami                              origami;

    /**
     * The origami we will store the current settings in. These values will be copied to <code>origami</code> when this
     * dialog is closed with the {@link OKAction}.
     */
    protected Origami                              tempOrigami;

    /**
     * If <code>true</code>, show texts for creating a model, otherwise display texts for editing an existing model's
     * metadata.
     */
    protected boolean                              isCreating;

    /** Input for model names. */
    protected JLangStringListTextField<JTextField> name;

    /** Input for model introduction year. */
    protected JSpinner                             year;

    /** Input for short description. */
    protected JLangStringListTextField<JTextField> shortDesc;

    /** Input for long description. */
    protected JLangStringListTextField<JTextArea>  description;

    /** Input for author's name. */
    protected JTextField                           authorName;

    /** Input for author's homepage. */
    protected JTextField                           authorHomepage;

    /** Input for license name. */
    protected JTextField                           licenseName;
    /** Input for license homepage. */
    protected JTextField                           licenseHomepage;
    /** Input for license content. */
    protected JTextArea                            licenseContent;
    /** RadioButton that signalizes that license homepage will be filled and content not. */
    protected JRadioButton                         licenseChooseHomepage;
    /** RadioButton that signalizes that license homepage will not be filled and content will be. */
    protected JRadioButton                         licenseChooseContent;
    /** Checkbox that signalizes the Do nothing license permission. */
    protected JCheckBox                            licensePermissionDoNothing;
    /** Checkbox that signalizes the Edit license permission. */
    protected JCheckBox                            licensePermissionEdit;
    /** Checkbox that signalizes the Export license permission. */
    protected JCheckBox                            licensePermissionExport;
    /** Checkbox that signalizes the Distribute license permission. */
    protected JCheckBox                            licensePermissionDistribute;
    /** Input for the original's URL. */
    protected JTextField                           original;
    /** RadioButton that signalizes that the thumbnail should be generated from the model. */
    protected JRadioButton                         thumbnailLoadFromModel;
    /** RadioButton that signalizes that the thumbnail should be generated from a file. */
    protected JRadioButton                         thumbnailLoadFromFile;
    /** Input for selecting the thumbnail. */
    protected JFileInput                           thumbnailFileInput;
    /** Component that shows a preview of the thumbnail. */
    protected JImage                               thumbnailPreview;
    /** Input for configuring diagram paper. */
    protected JDiagramPaperInput                   diagramPaper;
    /** Input for configuring model paper. */
    protected JModelPaperInput                     modelPaper;

    /**
     * @param tempOrigami If <code>null</code>, indicates that the dialog should display texts for creating a model
     *            metadata, otherwise it displays texts for editing an existing model's metadata.
     */
    public OrigamiPropertiesFrame(Origami origami)
    {
        setModalityType(ModalityType.APPLICATION_MODAL);

        setOrigami(origami);

        createComponents();
        buildLayout();

        setTitle("title"); // TODO
    }

    /**
     * Create all GUI components.
     */
    protected void createComponents()
    {
        name = new JLangStringListTextField<JTextField>(tempOrigami.getName(), new JTextField(20));

        year = new JSpinner(new SpinnerNumberModel(tempOrigami.getYear() != null ? tempOrigami.getYear().getYear()
                : new GregorianCalendar().get(Calendar.YEAR), Integer.MIN_VALUE,
                new GregorianCalendar().get(Calendar.YEAR), 1));
        year.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                tempOrigami.setYear(new XMLGregorianCalendarImpl(new GregorianCalendar(Integer.parseInt(year.getValue()
                        .toString()), 1, 1)));
            }
        });

        shortDesc = new JLangStringListTextField<JTextField>(tempOrigami.getShortdesc(), new JTextField(20));

        description = new DefaultLangStringListTextArea(tempOrigami.getDescription());

        authorName = new JTextField(20);
        authorName.getDocument().addDocumentListener(new UniversalDocumentListener() {
            @Override
            protected void update(DocumentEvent e)
            {
                tempOrigami.getAuthor().setName(authorName.getText());
            }
        });

        authorHomepage = new JTextField(20);
        authorHomepage.setInputVerifier(new URIInputVerifier());
        authorHomepage.getDocument().addDocumentListener(new UniversalDocumentListener() {
            @Override
            protected void update(DocumentEvent e)
            {
                URI value = null;
                try {
                    value = new URL(authorHomepage.getText()).toURI();
                } catch (MalformedURLException e1) {} catch (URISyntaxException e1) {}
                tempOrigami.getAuthor().setHomepage(value);
            }
        });

        licenseName = new JTextField(20);

        licenseHomepage = new JTextField(20);
        licenseHomepage.setInputVerifier(new URIInputVerifier());
        licenseHomepage.getDocument().addDocumentListener(new UniversalDocumentListener() {
            @Override
            protected void update(DocumentEvent e)
            {
                URI value = null;
                try {
                    value = new URL(licenseHomepage.getText()).toURI();
                } catch (MalformedURLException e1) {} catch (URISyntaxException e1) {}
                tempOrigami.getLicense().setHomepage(value);
            }
        });

        licenseContent = new JTextArea(5, 20);
        licenseContent.getDocument().addDocumentListener(new UniversalDocumentListener() {
            @Override
            protected void update(DocumentEvent e)
            {
                tempOrigami.getLicense().setContent(licenseContent.getText());
            }
        });

        ButtonGroup licenseChooseGroup = new ButtonGroup();

        licenseChooseHomepage = new JRadioButton("homepage");
        licenseChooseHomepage.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    licenseHomepage.setVisible(true);
                    licenseContent.setVisible(false);
                    tempOrigami.getLicense().setContent(null);
                }
            }
        });
        licenseChooseGroup.add(licenseChooseHomepage);

        licenseChooseContent = new JRadioButton("content");
        licenseChooseContent.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    licenseHomepage.setVisible(false);
                    licenseContent.setVisible(true);
                    tempOrigami.getLicense().setHomepage(null);
                }
            }
        });
        licenseChooseGroup.add(licenseChooseContent);

        licenseChooseHomepage.setSelected(true);

        licensePermissionDoNothing = new JCheckBox("do nothing");
        licensePermissionDoNothing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Permission perm = PermissionConverter.parse("doNothing");
                List<Permission> perms = tempOrigami.getLicense().getPermission();
                if (licensePermissionDoNothing.isSelected()) {
                    if (!perms.contains(perm))
                        perms.add(perm);
                } else {
                    perms.remove(perm);
                }
            }
        });

        licensePermissionEdit = new JCheckBox("edit");
        licensePermissionEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Permission perm = PermissionConverter.parse("edit");
                List<Permission> perms = tempOrigami.getLicense().getPermission();
                if (licensePermissionDoNothing.isSelected()) {
                    if (!perms.contains(perm))
                        perms.add(perm);
                } else {
                    perms.remove(perm);
                }
            }
        });

        licensePermissionExport = new JCheckBox("export");
        licensePermissionExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Permission perm = PermissionConverter.parse("export");
                List<Permission> perms = tempOrigami.getLicense().getPermission();
                if (licensePermissionDoNothing.isSelected()) {
                    if (!perms.contains(perm))
                        perms.add(perm);
                } else {
                    perms.remove(perm);
                }
            }
        });

        licensePermissionDistribute = new JCheckBox("distribute");
        licensePermissionDistribute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Permission perm = PermissionConverter.parse("distribute");
                List<Permission> perms = tempOrigami.getLicense().getPermission();
                if (licensePermissionDoNothing.isSelected()) {
                    if (!perms.contains(perm))
                        perms.add(perm);
                } else {
                    perms.remove(perm);
                }
            }
        });

        original = new JTextField(20);
        original.setInputVerifier(new URIInputVerifier());
        original.getDocument().addDocumentListener(new UniversalDocumentListener() {
            @Override
            protected void update(DocumentEvent e)
            {
                URI value = null;
                try {
                    value = new URL(original.getText()).toURI();
                } catch (MalformedURLException e1) {} catch (URISyntaxException e1) {}
                tempOrigami.setOriginal(value);
            }
        });

        ButtonGroup thumbnailChooseGroup = new ButtonGroup();

        thumbnailLoadFromModel = new JRadioButton("loadFromModel");
        thumbnailLoadFromModel.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    thumbnailFileInput.setEnabled(false);
                    thumbnailPreview.setVisible(false); // TODO maybe some better behavior
                    tempOrigami.getThumbnail().getImage().setImageIcon(null);
                }
            }
        });
        thumbnailChooseGroup.add(thumbnailLoadFromModel);

        thumbnailLoadFromFile = new JRadioButton("loadFromFile");
        thumbnailLoadFromFile.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    thumbnailFileInput.setEnabled(true);
                    thumbnailPreview.setVisible(true);
                }
            }
        });
        thumbnailChooseGroup.add(thumbnailLoadFromFile);

        thumbnailFileInput = new JFileInput(ServiceLocator.get(ConfigurationManager.class).get().getLastOpenPath()
                .toString(), 20);
        thumbnailFileInput.getFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        thumbnailFileInput.getFileChooser().setFileFilter(
                new FileNameExtensionFilter("images", "BMP", "JPG", "PNG", "GIF"));
        thumbnailFileInput.getFileChooser().setDialogType(JFileChooser.OPEN_DIALOG);
        thumbnailFileInput.getFileChooser().setApproveButtonText("open");
        thumbnailFileInput.getFileChooser().setApproveButtonMnemonic(KeyStroke.getKeyStroke("O").getKeyCode());
        thumbnailFileInput.getFileChooser().setApproveButtonToolTipText(
                ServiceLocator.get(TooltipFactory.class).getDecorated("open", "openTitle", "open-file-32.png",
                        KeyStroke.getKeyStroke("alt O")));
        thumbnailFileInput.getTextField().getDocument().addDocumentListener(new UniversalDocumentListener() {
            @Override
            protected void update(DocumentEvent e)
            {
                File selected = thumbnailFileInput.getFileChooser().getSelectedFile();
                ServiceLocator.get(ConfigurationManager.class).get().setLastOpenPath(selected);

                tempOrigami.getThumbnail().getImage()
                        .setType(selected.toString().substring(selected.toString().lastIndexOf('.') + 1));
                tempOrigami.getThumbnail().getImage().setImageIcon(new ImageIcon(selected.toString()));

                if (tempOrigami.getThumbnail().getImage().getImageIcon() != null)
                    thumbnailPreview.setImage(tempOrigami.getThumbnail().getImage().getImageIcon().getImage());
                else
                    thumbnailPreview.setImage(null);
            }
        });

        thumbnailPreview = new JImage(null);

        thumbnailLoadFromModel.setSelected(true);

        diagramPaper = new JDiagramPaperInput(tempOrigami.getPaper());

        modelPaper = new JModelPaperInput(tempOrigami.getModel().getPaper());
    }

    /**
     * Add all components to a layout.
     */
    protected void buildLayout()
    {
        setLayout(new GridLayout(0, 1));
        // add(name);
        // add(year);
        // add(shortDesc);
        // add(description);
        // add(authorName);
        // add(authorHomepage);
        // add(licenseName);
        // add(licenseChooseHomepage);
        // add(licenseChooseContent);
        // add(licenseHomepage);
        // add(licenseContent);
        // add(licensePermissionDoNothing);
        // add(licensePermissionEdit);
        // add(licensePermissionExport);
        // add(licensePermissionDistribute);
        // add(original);
        // add(thumbnailLoadFromModel);
        // add(thumbnailLoadFromFile);
        // add(thumbnailFileInput);
        // add(thumbnailPreview);
        add(modelPaper);
        pack();
    }

    /**
     * @return the origami that was set to this frame (or a new one if <code>null</code> was specified) with the
     *         metadata copied from this dialog's inputs' values. <code>null</code> can be returned only if the user
     *         cancelled the dialog for creating a new origami.
     */
    public Origami getOrigami()
    {
        if (origami != null && tempOrigami != null) {
            origami.getMetadataFrom(tempOrigami);
            return origami;
        } else if (origami != null) {
            return origami;
        }
        return tempOrigami;
    }

    /**
     * @param origami The origami that should be edited by the dialog.
     */
    protected void setOrigami(Origami origami)
    {
        this.origami = origami;

        tempOrigami = (Origami) new ObjectFactory().createOrigami();
        tempOrigami.initStructure();

        if (origami != null) {
            tempOrigami.getMetadataFrom(origami);
        }

        isCreating = (origami == null);
    }

    /**
     * Input verifier that verifies the content is a URI.
     * 
     * @author Martin Pecka
     */
    protected class URIInputVerifier extends InputVerifier
    {
        @Override
        public boolean verify(JComponent input)
        {
            if (!(input instanceof JTextField))
                return false;
            JTextField field = (JTextField) input;
            if (field.getText().length() == 0)
                return true;
            try {
                new URL(field.getText()).toURI();
                return true;
            } catch (MalformedURLException e) {
                return false;
            } catch (URISyntaxException e) {
                return false;
            }
        }
    }

}
