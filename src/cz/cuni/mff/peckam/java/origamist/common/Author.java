/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

/**
 * Represents the author of the model.
 * 
 * @author Martin Pecka
 */
public class Author extends cz.cuni.mff.peckam.java.origamist.common.jaxb.Author
{

    @Override
    public String toString()
    {
        return "Author [name=" + name + ", homepage=" + homepage + "]";
    }

}
