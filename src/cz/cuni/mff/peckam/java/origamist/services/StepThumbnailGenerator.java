/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Screen3D;

import com.sun.j3d.utils.universe.SimpleUniverse;

import cz.cuni.mff.peckam.java.origamist.gui.common.StepViewingCanvasController;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;

/**
 * A generator of step thumbnails.
 * 
 * @author Martin Pecka
 */
public class StepThumbnailGenerator extends Service
{
    /** The last set origami. */
    protected Origami                           origami     = null;

    /** The last set width and height. */
    protected int                               width       = 0, height = 0;

    /** The canvas this class uses for rendering. */
    protected final Canvas3D                    canvas;

    /** The canvas controller controlling this.canvas. */
    protected final StepViewingCanvasController controller;

    /** The callback that calls this.notify after the step has been completely set. */
    protected final Runnable                    afterSetCallback;

    /** If true, a rendering has taken place since the creation of this class. */
    protected boolean                           hasRendered = false;

    /**
     * A generator of step thumbnails.
     */
    public StepThumbnailGenerator()
    {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration(), true);

        controller = new StepViewingCanvasController(canvas);

        afterSetCallback = new Runnable() {
            @Override
            public void run()
            {
                controller.adjustSize();
                synchronized (StepThumbnailGenerator.this) {
                    StepThumbnailGenerator.this.notify();
                }
            }
        };

        // screen dimensions need to be set before rendering
        SimpleUniverse u = new SimpleUniverse(new Canvas3D(SimpleUniverse.getPreferredConfiguration()));
        Screen3D screen = u.getViewer().getCanvas3D().getScreen3D();
        Screen3D screen2 = canvas.getScreen3D();
        screen2.setSize(screen.getSize());
        screen2.setPhysicalScreenWidth(screen.getPhysicalScreenWidth());
        screen2.setPhysicalScreenHeight(screen.getPhysicalScreenHeight());
    }

    /**
     * Get the thumbnail of the last step of the given origami. The thumbnail will have the given dimension and
     * background drawn.
     * 
     * @param origami The origami the step is part of.
     * @param width Width of the thumbnail in px.
     * @param height Height of the thumbnail in px.
     * @return The thumbnail.
     */
    public Image getThumbnail(Origami origami, int width, int height)
    {
        return getThumbnail(origami,
                origami.getModel().getSteps().getStep().get(origami.getModel().getSteps().getStep().size() - 1), width,
                height);
    }

    /**
     * Get the thumbnail of the step of the given origami. The thumbnail will have the given dimension and background
     * drawn.
     * 
     * @param origami The origami the step is part of.
     * @param step The step to render.
     * @param width Width of the thumbnail in px.
     * @param height Height of the thumbnail in px.
     * @return The thumbnail.
     */
    public Image getThumbnail(Origami origami, Step step, int width, int height)
    {
        return getThumbnail(origami, step, width, height, true);
    }

    /**
     * Get the thumbnail of the step of the given origami. The thumbnail will have the given dimension.
     * 
     * @param origami The origami the step is part of.
     * @param step The step to render.
     * @param width Width of the thumbnail in px.
     * @param height Height of the thumbnail in px.
     * @param withBackground If true, draw the background color instead of transparent pixels.
     * @return The thumbnail.
     */
    public Image getThumbnail(Origami origami, Step step, int width, int height, boolean withBackground)
    {
        if (this.origami != origami) {
            this.origami = origami;
            canvas.setBackground(origami.getPaper().getBackgroundColor());
            controller.setOrigami(origami);
        }

        if (this.width != width || this.height != height) {
            ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA8, width, height);

            canvas.setOffScreenBuffer(image);

            this.width = width;
            this.height = height;
        }

        if (!hasRendered) {
            canvas.renderOffScreenBuffer(); // needed to make some transforms up-to-date
            hasRendered = true;
        }

        controller.setStep(step, afterSetCallback);

        synchronized (this) {
            try {
                wait(60000);
            } catch (InterruptedException e1) {}
        }

        canvas.waitForOffScreenRendering();
        canvas.renderOffScreenBuffer();
        canvas.waitForOffScreenRendering();

        if (withBackground) {
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = result.createGraphics();
            g.drawImage(canvas.getOffScreenBuffer().getImage(), 0, 0, canvas.getBackground(), null);

            return result;
        } else {
            return canvas.getOffScreenBuffer().getImage();
        }
    }
}
