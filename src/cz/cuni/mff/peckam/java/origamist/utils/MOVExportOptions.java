/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.awt.Dimension;
import java.awt.Insets;

/**
 * Options for MOV export format.
 * 
 * @author Martin Pecka
 */
public class MOVExportOptions implements ExportOptions
{

    protected Dimension size         = new Dimension(320, 240);
    protected double    fps          = 30;
    protected int       stepDuration = 2;

    @Override
    public Insets getPageInsets()
    {
        return null;
    }

    /**
     * @return The size.
     */
    public Dimension getSize()
    {
        return size;
    }

    /**
     * @param size The size to set.
     */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /**
     * @return The fps.
     */
    public double getFps()
    {
        return fps;
    }

    /**
     * @param fps The fps to set.
     */
    public void setFps(double fps)
    {
        this.fps = fps;
    }

    /**
     * @return The stepDuration.
     */
    public int getStepDuration()
    {
        return stepDuration;
    }

    /**
     * @param stepDuration The stepDuration to set.
     */
    public void setStepDuration(int stepDuration)
    {
        this.stepDuration = stepDuration;
    }
}
