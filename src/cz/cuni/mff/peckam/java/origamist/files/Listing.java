/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

/**
 * Additional functionality for the JAXB generated listing element.
 * 
 * @author Martin Pecka
 */
public class Listing extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing
{

    /**
     * If this isn't the newest version of the listing, convert it to the newest one. It this is the newest version,
     * just return <code>this</code>.
     * 
     * @return The newest version of the listing.
     */
    public Listing convertToNewestVersion()
    {
        // TODO if a new listing schema version is developed, make this code convert the objects older objects to the
        // newer
        return this;
    }

}
