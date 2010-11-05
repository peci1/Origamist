/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * A category containing some diagram metadata.
 * 
 * @author Martin Pecka
 */
public class Category extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Category implements FilesContainer,
        CategoriesContainer
{

    /** The category or listing this category is a subcategory of. */
    @XmlTransient
    protected CategoriesContainer       parent = null;

    /** The hastable for more comfortable search in localized names. */
    @XmlTransient
    protected Hashtable<Locale, String> names  = new Hashtable<Locale, String>();

    /**
     * Create a new listing category.
     */
    public Category()
    {
        ((ObservableList<LangString>) getName()).addObserver(new LangStringHashtableObserver(names));
    }

    /**
     * Iterator that iterates over all files in this categorie's files and subcategories.
     * 
     * @return Iterator that iterates over all files in this categorie's files and subcategories.
     */
    public Iterator<File> recursiveFileIterator()
    {
        return new Iterator<File>() {
            Iterator<File> fileIterator           = null;
            Iterator<File> categoriesFileIterator = null;
            boolean        wasRemoved             = false;

            {
                if (getFiles() != null)
                    fileIterator = getFiles().getFile().iterator();
                if (getCategories() != null)
                    categoriesFileIterator = ((Categories) getCategories()).recursiveFileIterator();
            }

            @Override
            public void remove()
            {
                if (wasRemoved) {
                    Logger.getLogger(getClass()).warn(
                            "Tried to remove a file from a categorie's recursive iterator twice.");
                } else if (fileIterator != null) {
                    wasRemoved = true;
                    fileIterator.remove();
                } else if (categoriesFileIterator != null) {
                    wasRemoved = true;
                    categoriesFileIterator.remove();
                } else {
                    Logger.getLogger(getClass()).warn(
                            "Tried to delete a file from a categorie's recursive iterator before a call to next().");
                }
            }

            @Override
            public File next()
            {
                wasRemoved = false;
                if (fileIterator != null && fileIterator.hasNext()) {
                    return fileIterator.next();
                } else if (categoriesFileIterator != null && categoriesFileIterator.hasNext()) {
                    fileIterator = null;
                    return categoriesFileIterator.next();
                }

                throw new NoSuchElementException("No more elements in recursive file iterator.");
            }

            @Override
            public boolean hasNext()
            {
                if (fileIterator != null && fileIterator.hasNext()) {
                    return true;
                } else if (categoriesFileIterator != null && categoriesFileIterator.hasNext()) {
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
     * @return The category or listing this category is a subcategory of.
     */
    public CategoriesContainer getParent()
    {
        return parent;
    }

    /**
     * @param parent The category or listing this category is a subcategory of.
     */
    public void setParent(CategoriesContainer parent)
    {
        this.parent = parent;
    }

    /**
     * Set this category as the parent of its files and subcategories and recursively does the same for all
     * subcategories.
     */
    public void updateChildParents()
    {
        if (getFiles() != null) {
            for (File f : getFiles().getFile()) {
                f.setParent(this);
            }
        }
        if (getCategories() != null) {
            for (Category c : getCategories().getCategory()) {
                c.setParent(this);
                c.updateChildParents();
            }
        }
    }

    /**
     * Create a category with the given id. If the id contains slashes ("/"), it is concerned as a category id hierarchy
     * and all missing categories are created.
     * 
     * Category "" is treated as <code>this</code>. Categories starting with a slash are not allowed here.
     * 
     * @param categoryString The category to create. It is written in the form "cat1id/cat2id/cat3id".
     * @return The created category.
     * 
     * @throws IllegalArgumentException If the categoryString is <code>null</code> or if it starts with a slash.
     */
    public CategoriesContainer createSubCategories(String categoryString)
    {
        if (categoryString == null || categoryString.startsWith("/"))
            throw new IllegalArgumentException("Cannot create absolute-path-like subcategories in a raw category.");

        String[] cats = categoryString.split("/");
        Category oldCat = this;
        for (String cat : cats) {
            Category newCat = (Category) new ObjectFactory().createCategory();
            newCat.setId(cat);
            newCat.getName().add(new LangString(cat, Locale.getDefault()));
            newCat.setParent(oldCat);
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
        return parent.getHierarchicalId(separator) + separator + this.id;
    }

    @Override
    public String toString()
    {
        return "Category [name=" + name + ", id=" + id + ", files=" + files + ", categories=" + categories + "]";
    }

    /**
     * Return the localized name of the category.
     * 
     * @param l The locale of the name. If null or not found, returns the content of the first &lt;name&gt; element
     *            defined.
     * @return The localized name.
     */
    public String getName(Locale l)
    {
        if (names.size() == 0) {
            ResourceBundle b = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.model.Origami", l);
            return b.getString("nameNotFound");
        }

        if (l == null || !names.containsKey(l))
            return name.get(0).getValue();
        return names.get(l);
    }

    /**
     * Add a name in the given locale.
     * 
     * @param l The locale of the name
     * @param name The name to add
     */
    public void addName(Locale l, String name)
    {
        LangString s = (LangString) new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(name);
        this.name.add(s);
    }

}
