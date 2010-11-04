/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services.interfaces;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.Listing;

/**
 * A class implementing this interface is able to load a listing.xml file into Java classes and export
 * cz.cuni.mff.peckam.java.origamist.files.Listing as listing.xml.
 * 
 * @author Martin Pecka
 */
public interface ListingHandler
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
    Listing load(URL path) throws IOException, UnsupportedDataFormatException;

    /**
     * Exports the listing to XML file.
     * 
     * @param listing The listing to export.
     * @param file The file to save the listing to.
     * @throws IOException If an IO error occured during saving.
     * @throws MarshalException If JAXB is unable to marshal an element.
     * @throws JAXBException If JAXB initialization failed.
     */
    void export(Listing listing, File file) throws IOException, MarshalException, JAXBException;
}
