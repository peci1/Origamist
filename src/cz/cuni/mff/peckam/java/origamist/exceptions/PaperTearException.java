/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.exceptions;

import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.modelstate.ModelTriangle;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An exception thrown if the paper would tear.
 * 
 * @author Martin Pecka
 */
public class PaperTearException extends PaperStructureException
{

    /** The triangles that caused this exception. */
    protected ModelTriangle   triangle, neighbor;

    /** */
    private static final long serialVersionUID = -3889575976327252483L;

    /**
     * @param triangle The triangles that caused this exception.
     * @param neighbor The triangles that caused this exception.
     */
    public PaperTearException(ModelTriangle triangle, ModelTriangle neighbor)
    {
        this.triangle = triangle;
        this.neighbor = neighbor;
    }

    @Override
    public String getMessage()
    {
        return ResourceBundle.getBundle(PaperStructureException.class.getName(),
                ServiceLocator.get(ConfigurationManager.class).get().getLocale()).getString("tear");
    }

    @Override
    public String toString()
    {
        return "PaperTearException [triangle=" + triangle + ", neighbor=" + neighbor + "], " + super.toString();
    }

}
