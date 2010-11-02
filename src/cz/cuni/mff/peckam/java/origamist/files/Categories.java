/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.utils.HashtableElementAdapter;
import cz.cuni.mff.peckam.java.origamist.utils.HashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * A list of subcategories.
 * 
 * @author Martin Pecka
 */
public class Categories extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Categories
{

    /**
     * The hastable for more comfortable search in subcatagories.
     */
    protected Hashtable<String, Category> categories = new Hashtable<String, Category>();

    public Categories()
    {
        category = new ObservableList<Category>();
        HashtableElementAdapter<Category, String, Category> elementAdapter = new HashtableElementAdapter<Category, String, Category>() {

            @Override
            public String getKey(Category item)
            {
                return item.getId();
            }

            @Override
            public Category getValue(Category item)
            {
                return item;
            }

        };
        HashtableObserver<Category, String, Category> listener = new HashtableObserver<Category, String, Category>(
                categories, elementAdapter);
        ((ObservableList<Category>) category).addObserver(listener);
    }

    /**
     * Count the overall number of files in this category and its subcategories.
     * 
     * @return The overall number of files in this category and its subcategories.
     */
    public int sizeRecursive()
    {
        int result = 0;
        for (Category cat : category) {
            if (cat.getFiles() != null)
                result += cat.getFiles().getFile().size();
            if (cat.getCategories() != null)
                result += ((Categories) cat.getCategories()).sizeRecursive();
        }
        return result;
    }

    /**
     * Iterator that iterates over all files in this list of categories and their subcategories.
     * 
     * @return Iterator that iterates over all files in this list of categories and their subcategories.
     */
    public Iterator<File> recursiveIterator()
    {
        return new Iterator<File>() {
            Iterator<Category> categoryIterator = category.iterator();
            Iterator<File>     catFileIterator  = categoryIterator.next().recursiveIterator();

            @Override
            public void remove()
            {
                Logger.getLogger(getClass()).warn(
                        "Tried to delete a file from a categorie's recursive iterator. Not implemented.");
            }

            @Override
            public File next()
            {
                if (!hasNext())
                    throw new NoSuchElementException("No more elements in recursive file iterator.");

                // hasNext will do all the magic for us

                return catFileIterator.next();

            }

            @Override
            public boolean hasNext()
            {
                if (catFileIterator.hasNext()) {
                    return true;
                } else if (categoryIterator.hasNext()) {
                    while (categoryIterator.hasNext() && !catFileIterator.hasNext()) {
                        catFileIterator = categoryIterator.next().recursiveIterator();
                    }
                    return catFileIterator.hasNext();
                }

                return false;
            }
        };
    }

    @Override
    public String toString()
    {
        return "Categories [categories=" + categories + "]";
    }

}
