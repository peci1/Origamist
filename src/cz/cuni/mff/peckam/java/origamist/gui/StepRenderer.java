/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Locale;

import javax.swing.JPanel;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.services.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

/**
 * This panel renders the given state of the model.
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanel
{
    private static final long serialVersionUID = 6989958008007575800L;

    /**
     * The origami diagram we are rendering.
     */
    protected Origami         origami          = null;

    /**
     * The step this renderer is rendering.
     */
    protected Step            step             = null;

    /**
     * The background color.
     */
    protected Color           backgroundColor  = null;

    /**
     * @return the origami
     */
    public Origami getOrigami()
    {
        return origami;
    }

    /**
     * @param origami the origami to set
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
        this.backgroundColor = origami.getPaper().getColor().getBackground();
    }

    /**
     * @return the step
     */
    public Step getStep()
    {
        return step;
    }

    /**
     * @param stepId the step to set
     */
    public void setStep(Step step)
    {
        this.step = step;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Locale l = ServiceLocator.get(ConfigurationManager.class).get()
                .getDiagramLocale();

        super.paintComponent(g);

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (step == null)
            return;

        // TODO

        try {
            g.setColor(Color.BLACK);
            g.drawString(((Step) step).getDescription(l), 10, 30);

            g.setColor(origami.getModel().getPaper().getColors()
                    .getForeground());
            g.fillRect(30, 30, 100, 100);
            g.setColor(Color.BLACK);
            g.drawRect(30, 30, 100, 100);
        } catch (NullPointerException e) {
            System.err.println(e);
        }
    }

}
