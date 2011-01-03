/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import cz.cuni.mff.peckam.java.origamist.gui.common.StepRenderer;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;

/**
 * A renderer of a step that supports mouse interaction with the step.
 * 
 * @author Martin Pecka
 */
public class StepEditor extends StepRenderer
{

    /** */
    private static final long serialVersionUID = -785240462702127380L;

    /**
     * 
     */
    public StepEditor()
    {
        super();
    }

    /**
     * @param origami
     * @param step
     */
    public StepEditor(Origami origami, Step step)
    {
        super(origami, step);
    }
}
