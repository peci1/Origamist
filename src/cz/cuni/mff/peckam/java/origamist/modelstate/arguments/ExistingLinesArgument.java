/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate.arguments;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.peckam.java.origamist.gui.editor.PickMode;
import cz.cuni.mff.peckam.java.origamist.gui.editor.StepEditor;
import cz.cuni.mff.peckam.java.origamist.model.Line2D;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

/**
 * Argument for selecting a set of existing lines.
 * 
 * @author Martin Pecka
 */
public class ExistingLinesArgument extends ExistingLineArgument
{
    /** The selected lines. */
    protected List<ModelSegment> lines = null;

    /**
     * @param required If true, this argument is required.
     * @param resourceBundleKey The key in "editor" resource bundle describing this operation argument.
     */
    public ExistingLinesArgument(boolean required, String resourceBundleKey)
    {
        super(required, resourceBundleKey);
    }

    @Override
    public boolean isComplete()
    {
        return lines != null;
    }

    /**
     * Return the first selected line.
     */
    @Override
    public ModelSegment getLine() throws IllegalStateException
    {
        if (!isComplete())
            throw new IllegalStateException("Cannot query properties of a non-completed argument.");

        return lines.get(0);
    }

    /**
     * Set the first line.
     */
    public void setLine(ModelSegment line)
    {
        if (this.lines == null)
            this.lines = new LinkedList<ModelSegment>();
        if (lines.size() > 0)
            this.lines.set(0, line);
        else
            this.lines.add(line);
    }

    /**
     * @return The lines.
     */
    public List<Line2D> getLines()
    {
        if (lines == null)
            return null;

        List<Line2D> result = new LinkedList<Line2D>();
        for (ModelSegment s : lines)
            result.add(new Line2D(s.getOriginal()));

        return result;
    }

    @Override
    public PickMode preferredPickMode()
    {
        return PickMode.LINE;
    }

    @Override
    public void readDataFromObject(StepEditor editor)
    {
        if (editor.getChosenExistingLine() != null)
            this.lines = editor.getChosenExistingLines();
        if (lines != null)
            support.firePropertyChange(COMPLETE_PROPERTY, false, true);
    }

    @Override
    protected String getUserTipPart()
    {
        return new LocalizedString(OperationArgument.class.getName(), "existing.lines.user.tip").toString();
    }
}
