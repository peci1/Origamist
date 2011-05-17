/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.List;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;

/**
 * Argument for selecting layers.
 * 
 * @author Martin Pecka
 */
public class LayersArgument extends OperationArgument implements EditorDataReceiver
{
    /** The line defining the layer set. */
    protected LineArgument  defLine = null;

    /** Selected layers. */
    protected List<Integer> layers  = null;

    /**
     * @param defLine The line defining the layer set.
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     * 
     * @throws NullPointerException If defLine is <code>null</code>.
     */
    public LayersArgument(LineArgument defLine, boolean required, String resourceBundleKey) throws NullPointerException
    {
        super(required, resourceBundleKey);

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
     * @return True if the line defining the layer set is complete.
     */
    public boolean isDefLineComplete()
    {
        return defLine.isComplete();
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
    public ModelSegment getDefSegment() throws IllegalStateException
    {
        return defLine.getLine().clone();
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.LAYER;
    }

    @Override
    public void readDataFromObject(StepEditor editor)
    {
        if (editor.getChosenLayers() != null && editor.getChosenLayers().size() > 0)
            this.layers = editor.getChosenLayers();
    }
}
