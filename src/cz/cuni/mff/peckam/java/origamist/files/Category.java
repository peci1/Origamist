/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.common.LangString;

/**
 * A category containing some diagram metadata.
 * 
 * @author Martin Pecka
 */
public class Category extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Category
{

    /** The category this category is a subcategory of. <code>null</code> means that this is the top-level category. */
    @XmlTransient
    protected Category parentCategory = null;

    /**
     * Iterator that iterates over all files in this categorie's files and subcategories.
     * 
     * @return Iterator that iterates over all files in this categorie's files and subcategories.
     */
    public Iterator<File> recursiveFileIterator()
    {
        return new Iterator<File>() {
            Iterator<File> fileIterator       = null;
            Iterator<File> categoriesIterator = null;

            {
                if (getFiles() != null)
                    fileIterator = getFiles().getFile().iterator();
                if (getCategories() != null)
                    categoriesIterator = ((Categories) getCategories()).recursiveFileIterator();
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
     * Iterator that iterates over all subcategories in this category.
     * 
     * @return Iterator that iterates over all subcategories in this category.
     */
    public Iterator<Category> recursiveCategoryIterator()
    {
        if (getCategories() == null)
            return null;

        return ((Categories) getCategories()).recursiveCategoryIterator();
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

    /**
     * Create a category with the given id. If the id contains slashes ("/"), it is concerned as a category id hierarchy
     * and all missing categories are created.
     * 
     * @param categoryString The category to create. It is written in the form "cat1id/cat2id/cat3id".
     * @return The created category.
     */
    public Category createSubCategories(String categoryString)
    {
        String[] cats = categoryString.split("/");
        Category oldCat = this;
        for (String cat : cats) {
            Category newCat = (Category) new ObjectFactory().createCategory();
            newCat.setId(cat);
            newCat.getName().add(new LangString(cat, Locale.getDefault()));
            newCat.setParentCategory(oldCat);
            if (oldCat.getCategories() == null)
                oldCat.setCategories(new ObjectFactory().createCategories());
            oldCat.getCategories().getCategory().add(newCat);
            oldCat = newCat;
        }
        return oldCat;
    }

    /**
     * Returns the id of this category composed of names of it and all of its parent categories connected with
     * <code>separator</code>, starting with the highest category.
     * 
     * @param separator The string to connect the categories with.
     * 
     * @return The hierarchical id of this category.
     */
    public String getHierarchicalId(String separator)
    {
        if (parentCategory == null)
            return this.id;
        else
            return parentCategory.getHierarchicalId(separator) + separator + this.id;
    }

    @Override
    public String toString()
    {
        return "Category [name=" + name + ", id=" + id + ", files=" + files + ", categories=" + categories + "]";
    }

}
