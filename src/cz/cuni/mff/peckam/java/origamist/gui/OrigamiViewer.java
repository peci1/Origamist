/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.services.OrigamiLoader;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

/**
 * The viewer of the origami model.
 * 
 * @author Martin Pecka
 */
public class OrigamiViewer extends CommonGui
{

    private static final long serialVersionUID = -6853141518719373854L;

    /**
     * Create and setup all the form components.
     */
    @Override
    protected void createComponents()
    {
        String path = getClass().getPackage().getName().replaceAll("\\.", "/") + "/../diagram.xml";
        try {
            // TODO remove testing stuff and put some more meaningful code
            Origami o = ServiceLocator.get(OrigamiLoader.class).loadModel(path);
            StepRenderer r = new StepRenderer();
            r.setOrigami(o);
            r.setStep((Step) o.getModel().getSteps().getStep().get(2));
            r.setPreferredSize(new Dimension(200, 200));
            getContentPane().add(r, BorderLayout.NORTH);
        } catch (FileNotFoundException e) {
            System.err.println(e); // TODO handle file not found exception
        } catch (UnsupportedDataFormatException e) {
            System.err.println(e); // TODO handle errors in data files
        } catch (IOException e) {
            System.err.println(e); // TODO handle IO errors
        }

    }

    /**
     * Setup the form layout.
     */
    @Override
    protected void buildLayout()
    {

    }

    @Override
    public void start()
    {
        super.start();

    }

    @Override
    public void stop()
    {
        super.stop();
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }

}