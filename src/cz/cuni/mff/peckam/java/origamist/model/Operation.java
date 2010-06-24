/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.Hashtable;

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
public class Operation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation
{

    /**
     * Icon of this operation.
     */
    protected ImageIcon                         icon            = null;

    /**
     * Cache for transitions from one modelstate to another one using this operation
     */
    protected Hashtable<ModelState, ModelState> modelStateCache = new Hashtable<ModelState, ModelState>();

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
     * If you change the type of this operation, the image gets updated to the default icon of the new type.
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
        // TODO check if we need to overwrite ModelState.hashCode() or not
        if (modelStateCache.get(previousState) != null)
            return modelStateCache.get(previousState);

        ModelState state;
        try {
            state = (ModelState) previousState.clone();
        } catch (CloneNotSupportedException e) {
            assert false : "ModelState does not support clone";
            return previousState;
        }

        // TODO model state transitioning stuff
        switch (this.type) {
            case ROTATE:
                state.addRotation(this.angle);
                break;
            case TURN_OVER:
                state.flipViewingAngle();
                break;
            case VALLEY_FOLD:
                // Triangulator can help here:
                // http://www.javafaq.nu/java-example-code-790.html
                // http://download.java.net/media/java3d/javadoc/1.3.2/com/sun/j3d/utils/geometry/GeometryInfo.html
                break;
            case MOUNTAIN_FOLD:
                break;
            case INSIDE_CRIMP_FOLD:
                break;
            case INSIDE_REVERSE_FOLD:
                break;
            case MOUNTAIN_VALLEY_FOLD_UNFOLD:
                break;
            case OPEN:
                break;
            case OUTSIDE_CRIMP_FOLD:
                break;
            case OUTSIDE_REVERSE_FOLD:
                break;
            case PULL:
                break;
            case RABBIT_FOLD:
                break;
            case REPEAT_ACTION:
                break;
            case SQUASH_FOLD:
                break;
            case THUNDERBOLT_FOLD:
                break;
            case VALLEY_MOUNTAIN_FOLD_UNFOLD:
                break;
            default:
                // TODO handle error - unknown operation
                break;
        }

        modelStateCache.put(previousState, state);
        return state;
    }
}
