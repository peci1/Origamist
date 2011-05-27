/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An argument asking for an image file.
 * 
 * @author Martin Pecka
 */
public class ImageArgument extends OperationArgument implements UserInputDataReceiver
{

    /** The selected image. */
    protected BufferedImage image = null;

    /**
     * @param required
     * @param resourceBundleKey
     */
    public ImageArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public boolean isComplete()
    {
        return image != null;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return null;
    }

    @Override
    public void askForData()
    {
        Configuration conf = ServiceLocator.get(ConfigurationManager.class).get();
        ResourceBundle bundle = ResourceBundle.getBundle(OperationArgument.class.getName(), conf.getLocale());

        JFileChooser chooser = new JFileChooser();
        File defaultFile = conf.getLastOpenPath().getParentFile();
        chooser.setCurrentDirectory(defaultFile);
        chooser.setFileFilter(new FileNameExtensionFilter(bundle.getString("imageDialog.fileFilter"), "jpg", "png",
                "bmp", "gif"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        if (chooser.showDialog(null, bundle.getString("imageDialog.approve")) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                image = null;
                JOptionPane.showMessageDialog(null, bundle.getString("imageDialog.error.message"),
                        bundle.getString("imageDialog.error.title"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @return The image.
     */
    public BufferedImage getImage()
    {
        return image;
    }

}
