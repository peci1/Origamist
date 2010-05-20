/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.io.FileNotFoundException;
import java.io.IOException;

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
     * Load the model saved in path.
     * 
     * @param path Path to the model (either local or a URL)
     * @return The model parsed from the given file.
     * 
     * @throws FileNotFoundException If no file exists in the given path.
     * @throws IOException If the file could not be read.
     * @throws UnsupportedDataFormatException If the given file does not contain
     *             a valid model.
     */
    Origami loadModel(String path) throws FileNotFoundException, IOException,
            UnsupportedDataFormatException;
}
