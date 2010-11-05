/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

/**
 * An object containing a cz.cuni.mff.peckam.java.origamist.files.jaxb.Files field.
 * 
 * @author Martin Pecka
 */
public interface FilesContainer extends HierarchicalComponent
{
    /**
     * Returns the container of the list of files.
     * 
     * If no <code>&lt;files&gt;</code> tag was set, returns <code>null</code>.
     * 
     * @return The container of the list of files.
     */
    cz.cuni.mff.peckam.java.origamist.files.jaxb.Files getFiles();

    /**
     * Sets the container of the list of files.
     * 
     * @param files The container to set.
     */
    void setFiles(cz.cuni.mff.peckam.java.origamist.files.jaxb.Files files);
}
