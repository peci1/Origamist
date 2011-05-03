/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.bind.Unmarshaller;

/**
 * A callback for configuring a {@link javax.xml.bind.Unmarshaller}.
 * 
 * @author Martin Pecka
 */
public interface UnmarshallerConfigurator
{
    /**
     * Configure the given unmarshaller.
     * 
     * @param unmarshaller The unmarshaller to be configured.
     */
    void configure(Unmarshaller unmarshaller);
}
