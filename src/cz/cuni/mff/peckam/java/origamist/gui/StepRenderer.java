/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import cz.cuni.mff.peckam.java.origamist.model.DoubleDimension;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
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
     * The label that shows the descripton of the shown step.
     */
    protected JTextArea       descLabel        = new JTextArea();

    public StepRenderer()
    {
        this.setLayout(new BorderLayout());
        this.add(descLabel, BorderLayout.SOUTH);
        descLabel.setEditable(false);
        descLabel.setOpaque(false);
    }

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

    @Override
    public void setPreferredSize(Dimension preferredSize)
    {
        super.setPreferredSize(preferredSize);
        this.descLabel.setMaximumSize(preferredSize);
    }

    @Override
    public void setSize(Dimension size)
    {
        super.setSize(size);
        this.descLabel.setMaximumSize(size);
    }

    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        this.descLabel.setMaximumSize(new Dimension(width, height));
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
        Locale l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();

        this.step = step;
        this.descLabel.setText(step.getDescription(l));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (step == null)
            return;

        Dimension renderAreaSize = new Dimension(getWidth(), getHeight() - descLabel.getHeight());

        // TODO step rendering code

        ModelState state = step.getModelState();

        try {
            // g.setColor(origami.getModel().getPaper().getColors().getForeground());
            // g.fillRect(30, 30, 100, 100);

            DoubleDimension relativePaperDimensions = origami.getModel().getPaper().getRelativeDimensions();

            g.setColor(Color.BLACK);

            Point2D point1 = new Point2D.Double(0, 0);
            Point2D point2 = new Point2D.Double(relativePaperDimensions.getWidth(), 0);
            point1 = state.getPointProjection(point1, renderAreaSize);
            point2 = state.getPointProjection(point2, renderAreaSize);
            g.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());

            point1 = new Point2D.Double(relativePaperDimensions.getWidth(), 0);
            point2 = new Point2D.Double(relativePaperDimensions.getWidth(), relativePaperDimensions.getHeight());
            point1 = state.getPointProjection(point1, renderAreaSize);
            point2 = state.getPointProjection(point2, renderAreaSize);
            g.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());

            point1 = new Point2D.Double(relativePaperDimensions.getWidth(), relativePaperDimensions.getHeight());
            point2 = new Point2D.Double(0, relativePaperDimensions.getHeight());
            point1 = state.getPointProjection(point1, renderAreaSize);
            point2 = state.getPointProjection(point2, renderAreaSize);
            g.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());

            point1 = new Point2D.Double(0, relativePaperDimensions.getHeight());
            point2 = new Point2D.Double(0, 0);
            point1 = state.getPointProjection(point1, renderAreaSize);
            point2 = state.getPointProjection(point2, renderAreaSize);
            g.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());

            // g.setColor(Color.BLACK);
            // g.drawRect(30, 30, 100, 100);
        } catch (NullPointerException e) {
            System.err.println(e);
        }
    }
}
