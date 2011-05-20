/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.exceptions;

import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * An exception thrown if the paper would tear or intersect.
 * 
 * @author Martin Pecka
 */
public class PaperStructureException extends RuntimeException
{

    /** */
    private static final long serialVersionUID = -4928336638856821523L;

    @Override
    public String getMessage()
    {
        return ResourceBundle.getBundle(PaperStructureException.class.getName(),
                ServiceLocator.get(ConfigurationManager.class).get().getLocale()).getString("message");
    }
}
