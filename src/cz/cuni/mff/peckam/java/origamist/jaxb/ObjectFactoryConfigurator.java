package cz.cuni.mff.peckam.java.origamist.jaxb;

import java.lang.reflect.Field;

import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * Unmarshaller configurator that configures the provided object factory to be used by the unmarshaller.
 * 
 * @author Martin Pecka
 */
public class ObjectFactoryConfigurator implements UnmarshallerConfigurator
{
    /** The object factory to use. */
    protected Object        objectFactory;

    /** The cached property name. */
    protected static String propertyName = null;

    /**
     * @param objectFactory The object factory to use. If you want, you can provide an array of object factories.
     */
    public ObjectFactoryConfigurator(Object objectFactory)
    {
        this.objectFactory = objectFactory;
    }

    @Override
    public void configure(Unmarshaller unmarshaller)
    {
        if (propertyName == null) {
            propertyName = "ObjectFactory";
            if (unmarshaller.getClass().getName()
                    .equals("com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl")) {
                propertyName = "com.sun.xml.internal.bind.ObjectFactory";
            } else if (unmarshaller.getClass().getName()
                    .equals("com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl")) {
                propertyName = "com.sun.xml.bind.ObjectFactory";
            } else {
                Class<?> clazz = unmarshaller.getClass();
                Field field = null;
                try {
                    field = clazz.getField("FACTORY");
                } catch (NoSuchFieldException e) {
                    try {
                        field = clazz.getField("factory");
                    } catch (NoSuchFieldException ex) {}
                }
                if (field != null) {
                    try {
                        propertyName = (String) field.get(unmarshaller);
                    } catch (IllegalAccessException e) {} catch (ClassCastException e) {}
                }
            }
        }

        try {
            unmarshaller.setProperty(propertyName, objectFactory);
        } catch (PropertyException e) {
            Logger.getLogger(getClass()).warn(
                    "Cannot set the property " + propertyName + " for unmarshaller. The unmarshaller is of class "
                            + unmarshaller.getClass()
                            + ". Please report this as bug to the author to add your JAXB implementation support.");
        }
    }
}