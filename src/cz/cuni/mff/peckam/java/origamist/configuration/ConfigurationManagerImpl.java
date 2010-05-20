/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.configuration;

import cz.cuni.mff.peckam.java.origamist.services.ConfigurationManager;

/**
 * A default implementation of a configuration manager that stores the
 * configuration in Preferences.
 * 
 * @author Martin Pecka
 */
public class ConfigurationManagerImpl implements ConfigurationManager
{

    /**
     * The active configuration.
     */
    protected Configuration configuration = new Configuration();

    @Override
    public Configuration get()
    {
        return configuration;
    }

    @Override
    public void persist()
    {
        // TODO Auto-generated method stub
    }

}
