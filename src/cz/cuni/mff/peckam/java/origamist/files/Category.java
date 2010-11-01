/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A category containing some diagram metadata.
 * 
 * @author Martin Pecka
 */
public class Category extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Category
{

    /**
     * Iterator that iterates over all files in this categorie's files and subcategories.
     * 
     * @return Iterator that iterates over all files in this categorie's files and subcategories.
     */
    public Iterator<File> recursiveIterator()
    {
        return new Iterator<File>() {
            Iterator<File> fileIterator       = null;
            Iterator<File> categoriesIterator = null;

            {
                if (getFiles() != null)
                    fileIterator = getFiles().getFile().iterator();
                if (getCategories() != null)
                    categoriesIterator = ((Categories) getCategories()).recursiveIterator();
            }

            @Override
            public void remove()
            {
                System.err.println("Tried to delete a file from a categorie's recursive iterator. Not implemented.");
            }

            @Override
            public File next()
            {
                if (fileIterator != null && fileIterator.hasNext()) {
                    return fileIterator.next();
                } else if (categoriesIterator != null && categoriesIterator.hasNext()) {
                    return categoriesIterator.next();
                }

                throw new NoSuchElementException("No more elements in recursive file iterator.");
            }

            @Override
            public boolean hasNext()
            {
                if (fileIterator != null && fileIterator.hasNext()) {
                    return true;
                } else if (categoriesIterator != null && categoriesIterator.hasNext()) {
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public String toString()
    {
        return "Category [name=" + name + ", id=" + id + ", files=" + files + ", categories=" + categories + "]";
    }

}
