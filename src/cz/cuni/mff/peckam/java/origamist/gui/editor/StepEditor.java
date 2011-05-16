/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.sun.j3d.exp.swing.JCanvas3D;

import cz.cuni.mff.peckam.java.origamist.gui.common.StepRenderer;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelPoint;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelSegment;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;

/**
 * A renderer of a step that supports mouse interaction with the step.
 * 
 * @author Martin Pecka
 */
public class StepEditor extends StepRenderer
{

    /** */
    private static final long serialVersionUID = -785240462702127380L;

    {
        addPropertyChangeListener("pickMode", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                switch ((PickMode) evt.getNewValue()) {
                    case POINT:
                        StepEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        break;
                    case LINE:
                        StepEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        break;
                    case LAYER:
                        StepEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        break;
                }
            }
        });
    }

    /**
     * 
     */
    public StepEditor()
    {
        super();
        setOpaque(true);
    }

    /**
     * @param origami
     * @param step
     */
    public StepEditor(Origami origami, Step step)
    {
        super(origami, step);
        setOpaque(true);
    }

    @Override
    protected StepEditingCanvasController createCanvasController(JCanvas3D canvas)
    {
        return new StepEditingCanvasController(canvas.getOffscreenCanvas3D());
    }

    @Override
    public StepEditingCanvasController getCanvasController()
    {
        return (StepEditingCanvasController) super.getCanvasController();
    }

    @Override
    protected ModelState getModelState()
    {
        return step != null ? step.getModelState(true) : null;
    }

    /**
     * @return The type of primitives the user can pick.
     */
    public PickMode getPickMode()
    {
        return getCanvasController().getPickMode();
    }

    /**
     * @param pickMode The type of primitives the user can pick.
     */
    public void setPickMode(PickMode pickMode)
    {
        getCanvasController().setPickMode(pickMode);
    }

    /**
     * @param currentOperationArgument The operation argument the editor fetches data for.
     */
    public void setCurrentOperationArgument(OperationArgument currentOperationArgument)
    {
        getCanvasController().setCurrentOperationArgument(currentOperationArgument);
    }

    /**
     * @return If a line is chosen, return it, otherwise return <code>null</code>.
     */
    public ModelSegment getChosenLine()
    {
        return getCanvasController().getChosenLine();
    }

    /**
     * @return If an existing line is chosen, return it, otherwise return <code>null</code>.
     */
    public ModelSegment getChosenExistingLine()
    {
        return getCanvasController().getChosenExistingLine();
    }

    /**
     * @return If a point is chosen, return it, otherwise return <code>null</code>.
     */
    public ModelPoint getChosenPoint()
    {
        return getCanvasController().getChosenPoint();
    }

    /**
     * @return If some layers are chosen, return them, otherwise return <code>null</code>.
     */
    public List<Integer> getChosenLayers()
    {
        return getCanvasController().getChosenLayers();
    }

    /**
     * Clear all chosen items.
     */
    public void clearChosenItems()
    {
        getCanvasController().clearChosenItems();
    }

    /**
     * @return True if the user is currently choosing the second point of a line.
     */
    public boolean isChoosingSecondPoint()
    {
        return getCanvasController().isChoosingSecondPoint();
    }
}