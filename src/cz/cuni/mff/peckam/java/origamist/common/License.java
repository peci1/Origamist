/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.security.Permission;

/**
 * The license of the model.
 * 
 * @author Martin Pecka
 */

public class License extends
        cz.cuni.mff.peckam.java.origamist.common.jaxb.License
{
    /**
     * @param action The action you ask to.
     * @return True if a permission to the given action is granted.
     */
    public boolean isPermitted(Permission action)
    {
        return permission.contains(action);
    }
}
