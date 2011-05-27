/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import cz.cuni.mff.peckam.java.origamist.common.BinaryImage;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Image;
import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.ImageArgument;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

/**
 * Show an image instead of performing an operation.
 * 
 * @author Martin Pecka
 */
public class ImageOperation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ImageOperation
{
    /** The buffered version of the image. */
    protected BufferedImage buffer = null;

    {
        setType(Operations.IMAGE);
    }

    @Override
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        previousState.setOverlayImage(buffer);
        return previousState;
    }

    @Override
    protected List<OperationArgument> initArguments()
    {
        List<OperationArgument> result = new LinkedList<OperationArgument>();
        result.add(new ImageArgument(true, "operation.argument.image"));
        return result;
    }

    @Override
    public void fillFromArguments() throws IllegalStateException
    {
        if (((ImageArgument) getArguments().get(0)).getImage() != null) {
            this.image = new Image();
            this.image.setImage(new BinaryImage());
            this.image.getImage().setImageIcon(new ImageIcon(((ImageArgument) getArguments().get(0)).getImage()));
            this.buffer = ((ImageArgument) getArguments().get(0)).getImage();
        }
    }

}
