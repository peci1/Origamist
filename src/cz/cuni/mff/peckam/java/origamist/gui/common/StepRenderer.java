/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.j3d.exp.swing.JCanvas3D;

import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;

/**
 * The panel for rendering a step.
 * 
 * Provides the following properties:
 * <ul>
 * <li>pickMode</li>
 * <li>zoom</li>
 * </ul>
 * 
 * @author Martin Pecka
 */
public class StepRenderer extends JPanel
{
    /** */
    private static final long             serialVersionUID = 9198803673578003101L;

    /** The origami diagram we are rendering. */
    protected Origami                     origami          = null;

    /** The step this renderer is rendering. */
    protected Step                        step             = null;

    /** The canvas the model is rendered to. */
    protected JCanvas3D                   canvas;

    /** The controller that performs step rendering onto canvas. */
    protected StepViewingCanvasController canvasController = null;

    /**
     * 
     */
    public StepRenderer()
    {
        setLayout(new BorderLayout());

        setOpaque(false);

        canvas = createCanvas();

        canvas.setOpaque(false);
        canvas.setResizeMode(JCanvas3D.RESIZE_DELAYED);

        add(canvas, BorderLayout.CENTER);
        canvas.setSize(new Dimension(20, 20));

        if (getWidth() > 0 && getHeight() > 0)
            canvas.setSize(getWidth(), getHeight());

        canvas.getOffscreenCanvas3D().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                canvas.getOffscreenCanvas3D().repaint();
                revalidate();
                repaint();
            }
        });

        canvasController = createCanvasController(canvas);

        canvasController.addPropertyChangeListener(StepViewingCanvasController.STEP_PROPERTY,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                repaint();
                            }
                        });
                    }
                });
        canvasController.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
    }

    /**
     * @param origami
     * @param step
     */
    public StepRenderer(Origami origami, Step step)
    {
        this();
        setOrigami(origami);
        setStep(step);
    }

    /**
     * Create a new canvas controller.
     * 
     * @param canvas The canvas to be controlled.
     * @return The canvas controller.
     */
    protected StepViewingCanvasController createCanvasController(JCanvas3D canvas)
    {
        StepViewingCanvasController canvasController = new StepViewingCanvasController(canvas.getOffscreenCanvas3D());
        return canvasController;
    }

    /**
     * @return A new and set-up canvas.
     */
    protected JCanvas3D createCanvas()
    {
        // Subclassing JCanvas3D is needed to call the protected method.
        JCanvas3D canvas = new JCanvas3D(new GraphicsConfigTemplate3D()) {
            /** */
            private static final long serialVersionUID = 1159847610761430144L;
            {
                // This call is needed because it is a workaround for the offscreen canvas not being notified of AWT
                // events with no registered listeners.
                enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK
                        | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
            }
        };

        return canvas;
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
        canvasController.setOrigami(origami);

        if (origami != null) {
            setBackground(origami.getPaper().getColor().getBackground());
        } else {
            setBackground(Color.GRAY); // TODO some more convenient behavior
        }
    }

    /**
     * @return the step
     */
    public Step getStep()
    {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(final Step step)
    {
        this.setStep(step, null);
    }

    /**
     * @param step the step to set
     * @param afterSetCallback The callback to call after the step is changed. Will be run outside EDT.
     */
    public void setStep(final Step step, final Runnable afterSetCallback)
    {
        this.step = step;

        if (step != null && step.getAttachedTo() == null) {
            return;
        }

        if (getWidth() > 0 && getHeight() > 0)
            canvas.setSize(getWidth(), getHeight());

        canvasController.setStep(step, afterSetCallback);
    }

    /**
     * @return The model state of the current step.
     */
    protected ModelState getModelState()
    {
        return step != null ? step.getModelState(false) : null;
    }

    /**
     * @return The overall zoom of the displayed object (as percentage - 0 to 100).
     */
    public double getCompositeZoom()
    {
        return canvasController.getCompositeZoom();
    }

    /**
     * @return the zoom
     */
    public double getZoom()
    {
        return canvasController.getZoom();
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(double zoom)
    {
        if (!isEnabled())
            return;

        canvasController.setZoom(zoom);
    }

    /**
     * Increase zoom by 10%.
     */
    public void incZoom()
    {
        setZoom(getZoom() + 10d);
    }

    /**
     * Decrease zoom by 10%.
     */
    public void decZoom()
    {
        setZoom(getZoom() - 10d);
    }

    /**
     * @return The canvas.
     */
    public JCanvas3D getCanvas()
    {
        return canvas;
    }

    /**
     * @return The controller that performs step rendering onto canvas.
     */
    public StepViewingCanvasController getCanvasController()
    {
        return canvasController;
    }

    /**
     * @param l
     * @see java.awt.Component#addKeyListener(java.awt.event.KeyListener)
     */
    public synchronized void addKeyListener(KeyListener l)
    {
        canvas.addKeyListener(l);
        canvasController.addKeyListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
     */
    public synchronized void addMouseListener(java.awt.event.MouseListener l)
    {
        canvas.addMouseListener(l);
        canvasController.addMouseListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addMouseMotionListener(java.awt.event.MouseMotionListener)
     */
    public synchronized void addMouseMotionListener(MouseMotionListener l)
    {
        canvas.addMouseMotionListener(l);
        canvasController.addMouseMotionListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addMouseWheelListener(java.awt.event.MouseWheelListener)
     */
    public synchronized void addMouseWheelListener(MouseWheelListener l)
    {
        canvas.addMouseWheelListener(l);
        canvasController.addMouseWheelListener(l);
    }

    /**
     * @param l
     * @see java.awt.Component#addInputMethodListener(java.awt.event.InputMethodListener)
     */
    public synchronized void addInputMethodListener(InputMethodListener l)
    {
        canvas.addInputMethodListener(l);
        canvasController.addInputMethodListener(l);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        super.addPropertyChangeListener(listener);
        canvasController.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        super.addPropertyChangeListener(propertyName, listener);
        canvasController.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        super.removePropertyChangeListener(listener);
        canvasController.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        super.removePropertyChangeListener(propertyName, listener);
        canvasController.removePropertyChangeListener(propertyName, listener);
    }
}