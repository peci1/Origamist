/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.IOException;
import java.net.URL;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.Listing;

/**
 * A class implementing this interface is able to load a listing.xml file into Java classes.
 * 
 * @author Martin Pecka
 */
public interface ListingLoader
{
    /**
     * Load the listing.xml from the path. Always returns the listing converted to the newest available schema version.
     * 
     * @param path Path to listing.xml
     * @return The listing parsed from the given file.
     * 
     * @throws IOException If the file could not be read.
     * @throws UnsupportedDataFormatException If the given file does not contain a valid listing.
     */
    Listing loadListing(URL path) throws IOException, UnsupportedDataFormatException;
}
