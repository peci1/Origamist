/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;

/**
 * A component for rendering a set of StepRenderers.
 * 
 * @author Martin Pecka
 */
public class DiagramRenderer extends JPanel
{
    private static final long serialVersionUID = -7158217935566060260L;

    /**
     * The origami which is this diagram from.
     */
    protected Origami         origami          = null;

    /**
     * The first step to be rendered.
     */
    protected Step            firstStep        = null;

    public DiagramRenderer(Origami o, Step firstStep)
    {
        this.origami = o;
        this.firstStep = firstStep;

        this.setLayout(new GridLayout(o.getPaper().getCols(), 0, 10, 10));

        int numSteps = o.getPaper().getCols() * o.getPaper().getRows();
        Step step = firstStep;
        for (int i = 0; i < numSteps; i++) {
            this.add(new StepRenderer(o, step));
            if (step.getNext() != null)
                step = step.getNext();
            else
                break;
        }
    }

}
