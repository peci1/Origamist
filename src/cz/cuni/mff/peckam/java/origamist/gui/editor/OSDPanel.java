/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * An OSD panel to be displayed over Canvas3D.
 * 
 * @author Martin Pecka
 */
public class OSDPanel
{

    protected Canvas3D         canvas;
    protected Coordinates      coordinates;
    protected Rectangle        bounds;
    protected BufferedImage    textureBuffer;
    protected BufferedImage    paintArea;
    protected ImageComponent2D textureImage;
    protected Texture          texture;
    protected Appearance       appearance;
    protected Shape3D          shape;
    protected QuadArray        plate;
    protected Transform3D      transform;
    protected TransformGroup   tGroup;
    protected BranchGroup      root;

    /**
     * Construct a new OSD panel on the given canvas and with the given bounds.
     * 
     * @param canvas The canvas this panel is attached to.
     * @param x Top-left corner's x coordinate in px.
     * @param y Top-left corner's y coordinate in px.
     * @param width Width of the panel in px.
     * @param height Height of the panel in px.
     */
    public OSDPanel(Canvas3D canvas, int x, int y, int width, int height, final boolean fromRight,
            final boolean fromBottom)
    {
        this.canvas = canvas;
        this.coordinates = new Coordinates(x, y, width, height, fromRight, fromBottom);
        updateBounds();

        canvas.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e)
            {
                repaint();
                updateTransform();
            }
        });

        Dimension textureSize = getTextureSize(width, height);

        textureBuffer = new BufferedImage(textureSize.width, textureSize.height, BufferedImage.TYPE_INT_ARGB);
        paintArea = textureBuffer.getSubimage(0, 0, width, height);

        float relWidth = (float) width / textureBuffer.getWidth(), relHeight = (float) height
                / textureBuffer.getHeight();

        plate = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        if (fromRight || fromBottom)
            plate.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
        plate.setTextureCoordinates(0, 0, new float[] { 0, 1 - relHeight, relWidth, 1 - relHeight, relWidth, 1, 0, 1 });
        updatePlateCoords();

        textureImage = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA8, textureSize.width, textureSize.height, true,
                false);
        textureImage.setCapability(ImageComponent2D.ALLOW_IMAGE_WRITE);
        textureImage.set(textureBuffer);

        texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, textureSize.width, textureSize.height);
        texture.setImage(0, textureImage);

        appearance = new Appearance();
        appearance.setTexture(texture);
        appearance.setTextureAttributes(new TextureAttributes());
        appearance.getTextureAttributes().setTextureMode(TextureAttributes.REPLACE);
        appearance.setTransparencyAttributes(new TransparencyAttributes());
        appearance.getTransparencyAttributes().setTransparencyMode(TransparencyAttributes.BLENDED);
        appearance.setPolygonAttributes(new PolygonAttributes());
        appearance.getPolygonAttributes().setCullFace(PolygonAttributes.CULL_NONE);
        appearance.setRenderingAttributes(new RenderingAttributes());
        appearance.getRenderingAttributes().setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
        appearance.getRenderingAttributes().setDepthBufferEnable(false);
        appearance.getRenderingAttributes().setDepthBufferWriteEnable(false);

        shape = new Shape3D(plate, appearance);

        root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_DETACH);

        tGroup = new TransformGroup();
        tGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tGroup.addChild(shape);
        updateTransform();

        root.addChild(tGroup);
    }

    protected void updateBounds()
    {
        this.bounds = coordinates.getBoundsInComponent(canvas);
    }

    protected void updateTransform()
    {
        // code inspired by j3d.org Overlay library, which is LGPL

        // get the field of view and then calculate the width in meters of the
        // screen
        double c_width = 2 * 2.1 * Math.tan(canvas.getView().getFieldOfView() * 0.5);

        // calculate the ratio between the canvas in pixels and the screen in
        // meters and use that to find the height of the screen in meters
        double scale = c_width / canvas.getWidth();
        double c_height = canvas.getHeight() * scale;

        // The texture is upside down relative to the canvas so this has to
        // be flipped to be in the right place. bounds needs to have the correct
        // value to be used in Overlays that relay on it to know their position
        // like mouseovers
        // float flipped_x = x; //TODO these values don't look to be correct, but in fact, they are (I think there
        // cumulate some errors that cancel mutually).
        float flipped_y = canvas.getHeight() - bounds.height - 2 * bounds.y;

        // build the plane offset
        transform = new Transform3D();
        Vector3d loc = new Vector3d(-c_width / 2, -c_height / 2 + flipped_y * scale, -2.1);

        transform.setTranslation(loc);
        transform.setScale(scale);

        tGroup.setTransform(transform);
    }

    /**
     * Update coordinates of plate.
     */
    protected void updatePlateCoords()
    {
        plate.setCoordinates(0, new float[] { bounds.x, bounds.y + bounds.height, 0, bounds.x + bounds.width,
                bounds.y + bounds.height, 0, bounds.x + bounds.width, bounds.y, 0, bounds.x, bounds.y, 0 });
    }

    /**
     * Attach this panel to the given universe.
     * 
     * @param universe The universe to attach to.
     */
    public void attachToUniverse(SimpleUniverse universe)
    {
        universe.getViewingPlatform().getViewPlatformTransform().addChild(root);
        repaint();
    }

    /**
     * Detach this panel from the given universe.
     * 
     * @param universe The universe to detach from.
     */
    public void detachFromUniverse(SimpleUniverse universe)
    {
        universe.getViewingPlatform().getViewPlatformTransform().removeChild(root);
    }

    /**
     * Manually force a repaint of this panel.
     */
    public void repaint()
    {
        if (coordinates.fromRight || coordinates.fromBottom) {
            updateBounds();
            updatePlateCoords();
        }
        paint(paintArea.createGraphics());
        textureImage.set(textureBuffer);
        canvas.repaint();
    }

    /**
     * If true, show this panel, otherwise hide it.
     * 
     * @param visible If the panel should be visible.
     */
    public void setVisible(boolean visible)
    {
        appearance.getRenderingAttributes().setVisible(visible);
    }

    /**
     * Paint graphics on the panel. Add all your painting code here.
     * 
     * @param graphics The graphics used to paint.
     */
    protected void paint(Graphics2D graphics)
    {

    }

    /**
     * Return the smallest possible texture size so that the whole rectangle of width and height fits into it.
     * 
     * @param width The minimum width of the texture.
     * @param height The minimum height of the texture.
     * @return Size of texture (both dimensions are powers of 2).
     */
    protected Dimension getTextureSize(int width, int height)
    {
        return new Dimension(getSmallestPower(width), getSmallestPower(height));
    }

    /**
     * Return the smallest power of 2 greater than value.
     * 
     * @param value The value to find the smallest non-less power of.
     * @return The smallest power of 2 greater than value.
     */
    protected int getSmallestPower(int value)
    {
        int n = 1;
        while (n < value)
            n <<= 1;

        return n;
    }

    /**
     * Utility class that makes it easy to get bounds that "stick" to a component's bottom or right edge.
     * 
     * @author Martin Pecka
     */
    protected class Coordinates
    {
        protected int x, y, width, height;
        boolean       fromRight, fromBottom;

        public Coordinates(int x, int y, int width, int height, boolean fromRight, boolean fromBottom)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.fromRight = fromRight;
            this.fromBottom = fromBottom;
        }

        /**
         * Get the bounds relative to the given component.
         * 
         * @param component The component to use as container.
         * @return The bounds of this corrdinates in the component.
         */
        public Rectangle getBoundsInComponent(Component component)
        {
            return new Rectangle(!fromRight ? x : component.getWidth() - x - width, !fromBottom ? y
                    : component.getHeight() - y - height, width, height);
        }
    }
}
