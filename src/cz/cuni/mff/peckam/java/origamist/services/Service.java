/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

/**
 * The base for all services.
 * 
 * The main task of this class is to check and define dependencies. Dependency cycles are not allowed.
 * 
 * @author Martin Pecka
 */
public abstract class Service
{

    /**
     * Creates the service and checks all its dependencies.
     * 
     * @throws IllegalStateException If a dependency is not satisfied.
     */
    public Service() throws IllegalStateException
    {
        for (Class<?> c : getDependecies()) {
            if (ServiceLocator.get(c) == null)
                throw new IllegalStateException("Service " + getClass()
                        + " is dependent on the non-registered service " + c
                        + "! Try to change the order of registering the services.");
        }
    }

    /**
     * @return The list of service classes this service depends on.
     */
    protected Class<?>[] getDependecies()
    {
        return new Class<?>[0];
    }

}
