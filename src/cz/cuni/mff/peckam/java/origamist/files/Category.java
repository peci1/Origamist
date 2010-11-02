/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

/**
 * A category containing some diagram metadata.
 * 
 * @author Martin Pecka
 */
public class Category extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Category
{

    /** The category this category is a subcategory of. <code>null</code> means that this is the top-level category. */
    protected Category parentCategory = null;

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
                Logger.getLogger(getClass()).warn(
                        "Tried to delete a file from a categorie's recursive iterator. Not implemented.");
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

    /**
     * @return the parentCategory
     */
    public Category getParentCategory()
    {
        return parentCategory;
    }

    /**
     * @param parentCategory the parentCategory to set
     */
    public void setParentCategory(Category parentCategory)
    {
        this.parentCategory = parentCategory;
    }

    /**
     * Set this category as the parent of its files and subcategories and recursively do the same for all subcategories.
     */
    public void updateChildCategories()
    {
        if (getFiles() != null) {
            for (File f : getFiles().getFile()) {
                f.setParentCategory(this);
            }
        }
        if (getCategories() != null) {
            for (Category c : getCategories().getCategory()) {
                c.setParentCategory(this);
                c.updateChildCategories();
            }
        }
    }

    @Override
    public String toString()
    {
        return "Category [name=" + name + ", id=" + id + ", files=" + files + ", categories=" + categories + "]";
    }

}
