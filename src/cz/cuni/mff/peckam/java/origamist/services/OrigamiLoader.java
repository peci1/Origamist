/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.IOException;
import java.net.URL;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;

/**
 * A class implementing this interface is able to load an origami model.
 * 
 * @author Martin Pecka
 */
public interface OrigamiLoader
{
    /**
     * Load the model saved in path. Always returns the model converted to the newest available schema version.
     * 
     * @param path Path to the model (either local or a URL)
     * @param onlyMetadata If true, the contents of the <code>model</code> tag will be skipped. They will be loaded
     *            lazily the first time they will be accessed.
     * @return The model parsed from the given file.
     * 
     * @throws IOException If the file could not be read.
     * @throws UnsupportedDataFormatException If the given file does not contain
     *             a valid model.
     */
    Origami loadModel(URL path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException;
}
