/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * 
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Operation extends
        cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation
{

    /**
     * Icon of this operation.
     */
    protected ImageIcon icon = null;

    /**
     * @return The icon of this operation
     */
    public ImageIcon getIcon()
    {
        return icon;
    }

    /**
     * Set the icon of this operation.
     * 
     * If you change the type of this operation, the image gets updated to the
     * default icon of the new type.
     * 
     * @param icon The icon to set
     */
    public void setIcon(ImageIcon icon)
    {
        this.icon = icon;
    }

    @Override
    public void setType(Operations value)
    {
        super.setType(value);
        setIcon(OperationsHelper.getIcon(value));
    }

    /**
     * Perform folding from the previous state to a new state by this operation.
     * 
     * @param previousState The state the model has now.
     * @return The state the model would have after performing this operation.
     */
    public ModelState getModelState(ModelState previousState)
    {
        // TODO
        // TODO caching
        return previousState;
    }

}
