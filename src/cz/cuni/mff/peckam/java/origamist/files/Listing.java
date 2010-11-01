/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import cz.cuni.mff.peckam.java.origamist.common.LangString;

/**
 * Additional functionality for the JAXB generated listing element.
 * 
 * @author Martin Pecka
 */
public class Listing extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing
{

    /**
     * If this isn't the newest version of the listing, convert it to the newest one. It this is the newest version,
     * just return <code>this</code>.
     * 
     * @return The newest version of the listing.
     */
    public Listing convertToNewestVersion()
    {
        // TODO if a new listing schema version is developed, make this code convert the objects older objects to the
        // newer
        return this;
    }

    /**
     * Adds the <code>java.io.File</code>s to this listing. If recursive non-null and greater than 0, add files from
     * subdirectories and create a category for each subdirectory of depth <code>recurseDepth</code> and less. If
     * recursive is <code>null</code>, recurse all subdirectories.
     * 
     * @param ioFiles The files to add.
     * @param recurseDepth If <code>null</code>, recurse infinitely, else recurse subdirectories of the depth
     *            <code>recurseDepth</code> and less.
     * @param category If <code>null</code>, add the files in the listing itself, otherwise add them in the category.
     */
    public void addFiles(List<java.io.File> ioFiles, final Integer recurseDepth, Category category)
    {
        if (recurseDepth != null && recurseDepth < 0)
            return;

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(java.io.File pathname)
            {
                if (recurseDepth != null && recurseDepth == 0 && pathname.isDirectory())
                    return false;
                if (pathname.isDirectory())
                    return true;
                return pathname.getName().toLowerCase().endsWith("xml");
            }
        };

        ObjectFactory of = new ObjectFactory();

        for (java.io.File ioFile : ioFiles) {
            if (!ioFile.isDirectory()) {
                if (!fileFilter.accept(ioFile))
                    continue;
                File file = (File) of.createFile();
                file.setSrc(ioFile.toURI());
                if (category == null) {
                    if (getFiles() == null)
                        setFiles(of.createFiles());
                    getFiles().getFile().add(file);
                } else {
                    if (category.getFiles() == null)
                        category.setFiles(of.createFiles());
                    category.getFiles().getFile().add(file);
                }
            } else {
                List<java.io.File> ioFilesToAdd = Arrays.asList(ioFile.listFiles(fileFilter));

                Category newCategory = (Category) of.createCategory();
                String name = ioFile.getName();
                newCategory.setId(name);
                newCategory.getName().add(new LangString(name, Locale.getDefault()));

                if (category == null) {
                    if (getCategories() == null)
                        setCategories(of.createCategories());
                    getCategories().getCategory().add(newCategory);
                } else {
                    if (category.getCategories() == null)
                        category.setCategories(of.createCategories());
                    category.getCategories().getCategory().add(newCategory);
                }

                this.addFiles(ioFilesToAdd, recurseDepth == null ? null : recurseDepth - 1, newCategory);
            }
        }
    }

    /**
     * Iterator that iterates over all files in this listing and the categories and subcategories.
     * 
     * @return Iterator that iterates over all files in thi listing and the categories and subcategories.
     */
    public Iterator<File> recursiveIterator()
    {
        return new Iterator<File>() {
            Iterator<File> fileIterator       = null;
            Iterator<File> categoriesIterator = null;
            {
                if (getFiles() != null) {
                    fileIterator = getFiles().getFile().iterator();
                }
                if (getCategories() != null) {
                    categoriesIterator = ((Categories) getCategories()).recursiveIterator();
                }
            }

            @Override
            public void remove()
            {
                System.err.println("Tried to delete a file from a listing's recursive iterator. Not implemented.");
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
     * Set this category as the parent of its files and subcategories and recursively do the same for all subcategories.
     */
    public void updateChildCategories()
    {
        if (getFiles() != null) {
            for (File f : getFiles().getFile()) {
                f.setParentCategory(null);
            }
        }
        if (getCategories() != null) {
            for (Category c : getCategories().getCategory()) {
                c.setParentCategory(null);
                c.updateChildCategories();
            }
        }
    }

    @Override
    public String toString()
    {
        return "Listing [files=" + files + ", categories=" + categories + ", version=" + version + "]";
    }
}
