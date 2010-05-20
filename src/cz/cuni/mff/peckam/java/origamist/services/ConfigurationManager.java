/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;

/**
 * Manages program configuration. Saving, loading, changing...
 * 
 * @author Martin Pecka
 */
public interface ConfigurationManager
{
    /**
     * @return The active configuration of the program.
     */
    Configuration get();

    /**
     * Persist the current configuration.
     */
    void persist();

}
