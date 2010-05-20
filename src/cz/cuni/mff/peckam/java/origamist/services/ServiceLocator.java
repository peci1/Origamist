/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.util.Hashtable;

/**
 * This class serves as a pool of implementations of available services
 * 
 * This is a "static" class - no instance can be created
 * 
 * @author Martin Pecka
 *
 */
public class ServiceLocator
{

    /**
     * Disables instance creation
     */
    private ServiceLocator() {}
    
    /**
     * Storage for the implementations
     */
    protected static Hashtable<Class<?>, Object> services = 
        new Hashtable<Class<?>, Object>();
    
    /**
     * Adds a new service for type T and stores its implementation
     * 
     * @param <T> The service type 
     * @param service The Class of the service to add
     * @param implementation Its implementation
     */
    public static <T> void add(Class<T> service, T implementation) 
    {
        services.put(service, implementation);
    }
    
    /**
     * Returns the implementation of the service.
     * 
     * @param <T> The service type
     * @param service The Class of the service to get
     * @return Implementation of the service
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> service)
    { 
        try {
            return (T)services.get(service);
        } catch (ClassCastException e) {
            return null;
        }
    }
    
    /**
     * Removes implementation of the given service
     * 
     * @param <T> The service type
     * @param service The service to remove
     */
    public static <T> void remove(Class<T> service)
    {
        services.remove(service);
    }
    
}
