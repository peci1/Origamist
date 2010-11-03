/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;

/**
 * A class implementing this interface is able to load an origami model from XML and save
 * cz.cuni.mff.peckam.java.origamist.model.Origami as XML.
 * 
 * @author Martin Pecka
 */
public interface OrigamiHandler
{
    /**
     * Saves the given origami in the given file in XML format according to the latest version of schema.
     * 
     * @param origami The origami to save.
     * @param file The file to save to.
     * 
     * @throws IOException If an IO error occured during saving.
     * @throws MarshalException If JAXB is unable to marshal an element.
     * @throws JAXBException If JAXB initialization failed.
     */
    public void save(Origami origami, File file) throws IOException, MarshalException, JAXBException;

    /**
     * Load the model saved in path. Always returns the model converted to the newest available schema version.
     * 
     * @param path Path to the model.
     * @param onlyMetadata If true, the contents of the <code>model</code> tag will be skipped. They will be loaded
     *            lazily the first time they will be accessed.
     * @return The model parsed from the given file.
     * 
     * @throws IOException If the file could not be read.
     * @throws UnsupportedDataFormatException If the given file does not contain
     *             a valid model.
     */
    Origami loadModel(URL path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException;

    /**
     * Load the model saved in path. Always returns the model converted to the newest available schema version.
     * 
     * @param path Path to the model.
     * @param onlyMetadata If true, the contents of the <code>model</code> tag will be skipped. They will be loaded
     *            lazily the first time they will be accessed.
     * @return The model parsed from the given file.
     * 
     * @throws IOException If the file could not be read.
     * @throws UnsupportedDataFormatException If the given file does not contain
     *             a valid model.
     */
    Origami loadModel(URI path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException;

    /**
     * @return the documentBase
     */
    URL getDocumentBase();

    /**
     * @param documentBase the documentBase to set
     */
    void setDocumentBase(URL documentBase);
}
