/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.vecmath.Matrix4d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

/**
 * A {@link MouseRotate} behavior that doesn't rotate around (0,0,0), but around the object's center.
 * 
 * @author Martin Pecka
 */
public class CenteredMouseRotate extends MouseRotate
{
    // Most of the methods and fields are just copied from MouseBehavior, because they are either default or private

    protected double                x_angle, y_angle;
    protected double                x_factor = .03;
    protected double                y_factor = .03;

    protected MouseBehaviorCallback callback = null;

    /**
     * Creates a default mouse rotate behavior.
     **/
    public CenteredMouseRotate()
    {
        super();
    }

    /**
     * Creates a rotate behavior that uses AWT listeners and behavior
     * posts rather than WakeupOnAWTEvent. The behavior is added to the
     * specified Component. A null component can be passed to specify
     * the behavior should use listeners. Components can then be added to
     * the behavior with the addListener(Component c) method.
     * Note that this behavior still needs a transform
     * group to work on (use setTransformGroup(tg)) and the transform
     * group must add this behavior.
     * 
     * @param flags interesting flags (wakeup conditions).
     */
    public CenteredMouseRotate(Component c, int flags)
    {
        super(c, flags);
    }

    /**
     * Creates a rotate behavior that uses AWT listeners and behavior
     * posts rather than WakeupOnAWTEvent. The behaviors is added to
     * the specified Component and works on the given TransformGroup.
     * A null component can be passed to specify the behavior should use
     * listeners. Components can then be added to the behavior with the
     * addListener(Component c) method.
     * 
     * @param c The Component to add the MouseListener and
     *            MouseMotionListener to.
     * @param transformGroup The TransformGroup to operate on.
     */
    public CenteredMouseRotate(Component c, TransformGroup transformGroup)
    {
        super(c, transformGroup);
    }

    /**
     * Creates a rotate behavior that uses AWT listeners and behavior
     * posts rather than WakeupOnAWTEvent. The behavior is added to the
     * specified Component. A null component can be passed to specify
     * the behavior should use listeners. Components can then be added
     * to the behavior with the addListener(Component c) method.
     * 
     * @param c The Component to add the MouseListener
     *            and MouseMotionListener to.
     */
    public CenteredMouseRotate(Component c)
    {
        super(c);
    }

    /**
     * Creates a rotate behavior.
     * Note that this behavior still needs a transform
     * group to work on (use setTransformGroup(tg)) and
     * the transform group must add this behavior.
     * 
     * @param flags interesting flags (wakeup conditions).
     */
    public CenteredMouseRotate(int flags)
    {
        super(flags);
    }

    /**
     * Creates a rotate behavior given the transform group.
     * 
     * @param transformGroup The transformGroup to operate on.
     */
    public CenteredMouseRotate(TransformGroup transformGroup)
    {
        super(transformGroup);
    }

    @Override
    public void initialize()
    {
        super.initialize();
        x_angle = 0;
        y_angle = 0;
        if ((flags & INVERT_INPUT) == INVERT_INPUT) {
            invert = true;
            x_factor *= -1;
            y_factor *= -1;
        }
    }

    @Override
    public double getXFactor()
    {
        return x_factor;
    }

    @Override
    public double getYFactor()
    {
        return y_factor;
    }

    @Override
    public void setFactor(double factor)
    {
        x_factor = y_factor = factor;
    }

    @Override
    public void setFactor(double xFactor, double yFactor)
    {
        x_factor = xFactor;
        y_factor = yFactor;
    }

    @Override
    public void processStimulus(@SuppressWarnings("rawtypes") Enumeration criteria)
    {
        WakeupCriterion wakeup;
        AWTEvent[] events;
        MouseEvent evt;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                if (events.length > 0) {
                    evt = (MouseEvent) events[events.length - 1];
                    process(evt);
                }
            }

            else if (wakeup instanceof WakeupOnBehaviorPost) {
                while (true) {
                    // access to the queue must be synchronized
                    synchronized (mouseq) {
                        if (mouseq.isEmpty())
                            break;
                        evt = (MouseEvent) mouseq.remove(0);
                        // consolidate MOUSE_DRAG events
                        while ((evt.getID() == MouseEvent.MOUSE_DRAGGED) && !mouseq.isEmpty()
                                && (((MouseEvent) mouseq.get(0)).getID() == MouseEvent.MOUSE_DRAGGED)) {
                            evt = (MouseEvent) mouseq.remove(0);
                        }
                    }
                    process(evt);
                }
            }

        }
        wakeupOn(mouseCriterion);
    }

    /**
     * Process the mouse event.
     * 
     * @param evt The event to be processed.
     */
    protected void process(MouseEvent evt)
    {
        int id;
        int dx, dy;

        processMouseEvent(evt);
        if (((buttonPress) && ((flags & MANUAL_WAKEUP) == 0)) || ((wakeUp) && ((flags & MANUAL_WAKEUP) != 0))) {
            id = evt.getID();
            if ((id == MouseEvent.MOUSE_DRAGGED) && !evt.isMetaDown() && !evt.isAltDown()) {
                x = evt.getX();
                y = evt.getY();

                dx = x - x_last;
                dy = y - y_last;

                if (!reset) {
                    x_angle = dy * y_factor;
                    y_angle = dx * x_factor;

                    transformX.rotX(x_angle);
                    transformY.rotY(y_angle);

                    transformGroup.getTransform(currXform);

                    Matrix4d mat = new Matrix4d();
                    // Remember old matrix
                    currXform.get(mat);

                    if (invert) {
                        currXform.mul(currXform, transformX);
                        currXform.mul(currXform, transformY);
                    } else {
                        currXform.mul(transformX, currXform);
                        currXform.mul(transformY, currXform);
                    }

                    // This is the only difference from MouseRotate - not setting the old transform back
                    // currXform.setTranslation(translation);

                    // Update xform
                    transformGroup.setTransform(currXform);

                    transformChanged(currXform);

                    if (callback != null)
                        callback.transformChanged(MouseBehaviorCallback.ROTATE, currXform);
                } else {
                    reset = false;
                }

                x_last = x;
                y_last = y;
            } else if (id == MouseEvent.MOUSE_PRESSED) {
                x_last = evt.getX();
                y_last = evt.getY();
            }
        }
    }

    @Override
    public void setupCallback(MouseBehaviorCallback callback)
    {
        this.callback = callback;
    }
}
