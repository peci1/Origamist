/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.util.Hashtable;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

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
    private ServiceLocator()
    {
    }

    /**
     * Storage for the implementations
     */
    protected final static Hashtable<Class<?>, Object>      services       = new Hashtable<Class<?>, Object>();

    /** Storage for callbacks returning the services. */
    protected final static Hashtable<Class<?>, Callable<?>> serviceGetters = new Hashtable<Class<?>, Callable<?>>();

    /**
     * Adds a new service for type T and stores its implementation.
     * 
     * @param <T> The service type.
     * @param service The Class of the service to add.
     * @param implementation Its implementation.
     */
    public static <T> void add(Class<T> service, T implementation)
    {
        services.put(service, implementation);
    }

    /**
     * Adds a new service for type T and saves the callback to instantiate the service.
     * 
     * @param <T> The service type.
     * @param service The Class of the service to add.
     * @param serviceGetter The callback returning the service.
     */
    public static <T> void add(Class<T> service, Callable<? extends T> serviceGetter)
    {
        serviceGetters.put(service, serviceGetter);
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
            Object found = services.get(service);
            if (found != null)
                return (T) found;
            Callable<?> getter = serviceGetters.get(service);
            if (getter != null) {
                try {
                    found = getter.call();
                    services.put(service, found);
                } catch (Exception e) {
                    Logger.getLogger(ServiceLocator.class).error("Service getting callback threw exception.", e);
                } finally {
                    serviceGetters.remove(getter);
                }
                return (T) found;
            }
            return null;
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
        if (services.remove(service) == null)
            serviceGetters.remove(service);
    }
}
