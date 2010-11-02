/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.OrigamiLoader;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

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
     * Create a category with the given id. If the id contains slashes ("/"), it is concerned as a category id hierarchy
     * and all missing categories are created.
     * 
     * @param categoryString The category to create. It is written in the form "cat1id/cat2id/cat3id".
     * @return The created category.
     */
    public Category createSubCategories(String categoryString)
    {
        String[] cats = categoryString.split("/");
        Category firstCat = ((Categories) this.getCategories()).getHashtable().get(cats[0]);
        if (firstCat == null) {
            firstCat = (Category) new ObjectFactory().createCategory();
            firstCat.setId(cats[0]);
            firstCat.getName().add(new LangString(cats[0], Locale.getDefault()));
            firstCat.setParentCategory(null);
            if (this.getCategories() == null)
                this.setCategories(new ObjectFactory().createCategories());
            this.getCategories().getCategory().add(firstCat);
        }
        if (cats.length == 1) {
            return firstCat;
        }
        return firstCat.createSubCategories(categoryString.substring(categoryString.indexOf("/") + 1));
    }

    /**
     * Adds the given origami to the file listing.
     * 
     * @param origami The origami to add. It must have its src property non-<code>null</code>.
     * @param categoryString The category to add the origami to, or <code>""</code> if it has to be added directly under
     *            the listing. The category is written in the form "cat1id/cat2id/cat3id".
     * 
     * @return The File object created when adding the origami.
     */
    public File addOrigami(Origami origami, String categoryString)
    {
        Category category = createSubCategories(categoryString);
        return addOrigami(origami, category);
    }

    /**
     * Loads origami from the given path and adds it to the file listing.
     * 
     * @param path The path where the origami is to be loaded from.
     * @param category The category to add the origami to, or <code>null</code> if it has to be added directly under the
     *            listing.
     * 
     * @return The File object created when adding the origami.
     * 
     * @throws UnsupportedDataFormatException If the origami could not be loaded.
     * @throws IOException If an IO error occured while loading the origami.
     */
    public File addOrigami(URI path, Category category) throws UnsupportedDataFormatException, IOException
    {
        Origami o = ServiceLocator.get(OrigamiLoader.class).loadModel(path, true);
        return addOrigami(o, category);
    }

    /**
     * Loads origami from the given path and adds it to the file listing.
     * 
     * @param path The path where the origami is to be loaded from.
     * @param category The category to add the origami to, or <code>null</code> if it has to be added directly under the
     *            listing.
     * 
     * @return The File object created when adding the origami.
     * 
     * @throws UnsupportedDataFormatException If the origami could not be loaded.
     * @throws IOException If an IO error occured while loading the origami.
     */
    public File addOrigami(URL path, Category category) throws UnsupportedDataFormatException, IOException
    {
        Origami o = ServiceLocator.get(OrigamiLoader.class).loadModel(path, true);
        return addOrigami(o, category);
    }

    /**
     * Loads origami from the given path and adds it to the file listing.
     * 
     * @param path The path where the origami is to be loaded from.
     * @param category The category to add the origami to, or <code>""</code> if it has to be added directly under
     *            the listing. The category is written in the form "cat1id/cat2id/cat3id".
     * 
     * @return The File object created when adding the origami.
     * 
     * @throws UnsupportedDataFormatException If the origami could not be loaded.
     * @throws IOException If an IO error occured while loading the origami.
     */
    public File addOrigami(URI path, String category) throws UnsupportedDataFormatException, IOException
    {
        Origami o = ServiceLocator.get(OrigamiLoader.class).loadModel(path, true);
        return addOrigami(o, category);
    }

    /**
     * Loads origami from the given path and adds it to the file listing.
     * 
     * @param path The path where the origami is to be loaded from.
     * @param category The category to add the origami to, or <code>""</code> if it has to be added directly under
     *            the listing. The category is written in the form "cat1id/cat2id/cat3id".
     * 
     * @return The File object created when adding the origami.
     * 
     * @throws UnsupportedDataFormatException If the origami could not be loaded.
     * @throws IOException If an IO error occured while loading the origami.
     */
    public File addOrigami(URL path, String category) throws UnsupportedDataFormatException, IOException
    {
        Origami o = ServiceLocator.get(OrigamiLoader.class).loadModel(path, true);
        return addOrigami(o, category);
    }

    /**
     * Adds the given origami to the file listing.
     * 
     * @param origami The origami to add. It must have its src property non-<code>null</code>.
     * @param category The category to add the origami to, or <code>null</code> if it has to be added directly under the
     *            listing.
     * 
     * @return The File object created when adding the origami.
     */
    public File addOrigami(Origami origami, Category category)
    {
        File file = (File) new ObjectFactory().createFile();
        if (origami.getSrc() == null) {
            Logger.getLogger("application").l7dlog(Level.ERROR, "listingAddOrigamiInvalidOrigamiSource",
                    new NullPointerException());
            return null;
        }
        try {
            file.setSrc(origami.getSrc().toURI());
        } catch (URISyntaxException e) {
            Logger.getLogger("application").l7dlog(Level.ERROR, "listingAddOrigamiInvalidOrigamiSource", e);
            return null;
        }

        file.setParentCategory(category);
        file.setOrigami(origami);
        file.fillFromOrigami();

        if (category != null) {
            if (category.getFiles() == null)
                category.setFiles(new ObjectFactory().createFiles());
            category.getFiles().getFile().add(file);
        } else {
            if (this.getFiles() == null)
                this.setFiles(new ObjectFactory().createFiles());
            this.getFiles().getFile().add(file);
        }
        return file;
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
                Logger.getLogger(getClass()).warn(
                        "Tried to delete a file from a listing's recursive iterator. Not implemented.");
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
