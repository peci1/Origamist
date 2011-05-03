/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.List;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.model.Line2D;

/**
 * Argument for selecting layers.
 * 
 * @author Martin Pecka
 */
public class LayersArgument extends OperationArgument
{
    /** The line defining the layer set. */
    protected LineArgument  defLine = null;

    /** Selected layers. */
    protected List<Integer> layers  = null;

    /**
     * @param defLine The line defining the layer set.
     * @param required If true, this argument is required.
     * 
     * @throws NullPointerException If defLine is <code>null</code>.
     */
    public LayersArgument(LineArgument defLine, boolean required) throws NullPointerException
    {
        super(required);

        if (defLine == null)
            throw new NullPointerException("defLine cannot be null");

        this.defLine = defLine;
    }

    @Override
    public boolean isComplete()
    {
        return layers != null && layers.size() > 0;
    }

    /**
     * @return The layers.
     * 
     * @throws IllegalStateException If {@link #isComplete()} is false.
     */
    public List<Integer> getLayers() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return layers;
    }

    /**
     * @param layers The layers to set.
     */
    public void setLayers(List<Integer> layers)
    {
        this.layers = layers;
    }

    /**
     * @return A copy of the line defining the layer set.
     * 
     * @throws IllegalStateException If the defLine hasn't been completed yet.
     */
    public Line2D getDefSegment() throws IllegalStateException
    {
        return defLine.getLine().clone();
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.LAYER;
    }
}
