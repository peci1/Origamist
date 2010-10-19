/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.util.prefs.BackingStoreException;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;

/**
 * Manages program configuration. Saving, loading, changing...
 * 
 * @author Martin Pecka
 */
public interface ConfigurationManager
{
    /**
     * Loads the values of the configuration. May be used if you don't want the first call to <code>get()</code> to be
     * slow. If this is called when a configuration exists, this will overwrite the active configuration values by the
     * loaded ones (and will trigger appropriate <code>PropertyChangeEvent</code>s).
     */
    void load();

    /**
     * @return The active configuration of the program.
     */
    Configuration get();

    /**
     * Persist the current configuration. If the configuration has not been loaded yet, does nothing.
     * 
     * @throws BackingStoreException If the saving failed.
     */
    void persist() throws BackingStoreException;

}
