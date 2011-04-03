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
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.EmptyIterator;

/**
 * Additional functionality for the JAXB generated listing element.
 * 
 * @author Martin Pecka
 */
public class Listing extends cz.cuni.mff.peckam.java.origamist.files.jaxb.Listing implements FilesContainer,
        CategoriesContainer
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
     * Adds the <code>java.net.URI</code>s to this listing. If recursive is non-<code>null</code> and greater than 0,
     * add files from subdirectories and create a category for each subdirectory of depth <code>recurseDepth</code> and
     * less. If recursive is <code>null</code>, recurse all subdirectories. The recursion is done only for URIs with the
     * "file" scheme.
     * 
     * @param uris The URIs to add.
     * @param recurseDepth If <code>null</code>, recurse infinitely, else recurse subdirectories of the depth
     *            <code>recurseDepth</code> and less.
     * @param category The category or listing to add the files to.
     */
    public void addFiles(List<URI> uris, final Integer recurseDepth, FilesContainer category)
    {
        if (category == null)
            throw new IllegalArgumentException("Cannot add files to null category.");

        if (recurseDepth != null && recurseDepth < 0)
            return;

        final FileFilter fileFilter = new FileFilter() {
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

        final ObjectFactory of = new ObjectFactory();

        for (URI uri : uris) {
            if ("file".equals(uri.getScheme())) {
                java.io.File ioFile = new java.io.File(uri);
                if (!ioFile.isDirectory()) {
                    if (!fileFilter.accept(ioFile))
                        continue;
                    File file = (File) of.createFile();
                    file.setSrc(uri);
                    file.setParent(category);
                    if (category.getFiles() == null)
                        category.setFiles((Files) of.createFiles());
                    category.getFiles().getFile().add(file);
                } else {
                    List<java.io.File> ioFilesToAdd = Arrays.asList(ioFile.listFiles(fileFilter));

                    Category newCategory = (Category) of.createCategory();
                    String name = ioFile.getName();
                    newCategory.setId(name);
                    newCategory.getName().add(new LangString(name, Locale.getDefault()));

                    CategoriesContainer cCategory = (CategoriesContainer) category;

                    newCategory.setParent(cCategory);

                    if (cCategory.getCategories() == null)
                        cCategory.setCategories((Categories) of.createCategories());
                    cCategory.getCategories().getCategory().add(newCategory);

                    this.addFiles(ioFilesToAdd, recurseDepth == null ? null : recurseDepth - 1, newCategory, false);
                }
            } else {
                if (!uri.toString().toLowerCase().endsWith(".xml"))
                    continue;
                File file = (File) of.createFile();
                file.setSrc(uri);
                file.setParent(category);
                if (category.getFiles() == null)
                    category.setFiles((Files) of.createFiles());
                category.getFiles().getFile().add(file);
            }
        }
    }

    /**
     * Adds the <code>java.io.File</code>s to this listing. If recursive non-null and greater than 0, add files from
     * subdirectories and create a category for each subdirectory of depth <code>recurseDepth</code> and less. If
     * recursive is <code>null</code>, recurse all subdirectories.
     * 
     * @param ioFiles The files to add.
     * @param recurseDepth If <code>null</code>, recurse infinitely, else recurse subdirectories of the depth
     *            <code>recurseDepth</code> and less.
     * @param category The category or listing to add the files to.
     */
    public void addFiles(List<java.io.File> ioFiles, final Integer recurseDepth, FilesContainer category, boolean unused)
    {
        if (category == null)
            throw new IllegalArgumentException("Cannot add files to null category.");

        if (recurseDepth != null && recurseDepth < 0)
            return;

        final FileFilter fileFilter = new FileFilter() {
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

        final ObjectFactory of = new ObjectFactory();

        for (java.io.File ioFile : ioFiles) {
            if (!ioFile.isDirectory()) {
                if (!fileFilter.accept(ioFile))
                    continue;
                File file = (File) of.createFile();
                file.setSrc(ioFile.toURI());
                file.setParent(category);
                if (category.getFiles() == null)
                    category.setFiles((Files) of.createFiles());
                category.getFiles().getFile().add(file);
            } else {
                List<java.io.File> ioFilesToAdd = Arrays.asList(ioFile.listFiles(fileFilter));

                Category newCategory = (Category) of.createCategory();
                String name = ioFile.getName();
                newCategory.setId(name);
                newCategory.getName().add(new LangString(name, Locale.getDefault()));

                CategoriesContainer cCategory = (CategoriesContainer) category;

                newCategory.setParent(cCategory);

                if (cCategory.getCategories() == null)
                    cCategory.setCategories((Categories) of.createCategories());
                cCategory.getCategories().getCategory().add(newCategory);

                this.addFiles(ioFilesToAdd, recurseDepth == null ? null : recurseDepth - 1, newCategory, false);
            }
        }
    }

    /**
     * Create a category with the given id. If the id contains slashes ("/"), it is concerned as a category id hierarchy
     * and all missing categories are created.
     * 
     * Categories "" and "/" are concerned as the listing.
     * 
     * @param categoryString The category to create. It is written in the form "cat1id/cat2id/cat3id".
     * @return The created category.
     */
    public CategoriesContainer createSubCategories(String categoryString)
    {
        if (categoryString == null || categoryString.equals("") || categoryString.equals("/"))
            return this;

        String[] cats = categoryString.split("/");
        Category firstCat = (this.getCategories()).getHashtable().get(cats[0]);
        if (firstCat == null) {
            firstCat = (Category) new ObjectFactory().createCategory();
            firstCat.setId(cats[0]);
            firstCat.getName().add(new LangString(cats[0], Locale.getDefault()));
            firstCat.setParent(this);
            if (this.getCategories() == null)
                this.setCategories((Categories) new ObjectFactory().createCategories());
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
        FilesContainer category = (FilesContainer) createSubCategories(categoryString);
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
    public File addOrigami(URI path, FilesContainer category) throws UnsupportedDataFormatException, IOException
    {
        Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(path, true);
        return addOrigami(o, category);
    }

    /**
     * Loads origami from the given path and adds it to the file listing.
     * 
     * @param path The path where the origami is to be loaded from.
     * @param category The category or listing to add the origami to.
     * 
     * @return The File object created when adding the origami.
     * 
     * @throws UnsupportedDataFormatException If the origami could not be loaded.
     * @throws IOException If an IO error occured while loading the origami.
     */
    public File addOrigami(URL path, FilesContainer category) throws UnsupportedDataFormatException, IOException
    {
        Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(path, true);
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
        Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(path, true);
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
        Origami o = ServiceLocator.get(OrigamiHandler.class).loadModel(path, true);
        return addOrigami(o, category);
    }

    /**
     * Adds the given origami to the file listing.
     * 
     * @param origami The origami to add. It must have its src property non-<code>null</code>.
     * @param category The category or listing to add the origami to.
     * 
     * @return The File object created when adding the origami.
     */
    public File addOrigami(Origami origami, FilesContainer category)
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

        file.setParent(category);
        file.setOrigami(origami);
        file.fillFromOrigami();

        if (category.getFiles() == null)
            category.setFiles((Files) new ObjectFactory().createFiles());
        category.getFiles().getFile().add(file);

        return file;
    }

    /**
     * Iterator that iterates over all files in this listing and the categories and subcategories.
     * 
     * @return Iterator that iterates over all files in this listing and the categories and subcategories.
     */
    public Iterator<File> recursiveFileIterator()
    {
        return new Iterator<File>() {
            Iterator<File> fileIterator           = null;
            Iterator<File> categoriesFileIterator = null;
            boolean        wasRemoved             = false;

            {
                if (getFiles() != null) {
                    fileIterator = getFiles().getFile().iterator();
                }
                if (getCategories() != null) {
                    categoriesFileIterator = (getCategories()).recursiveFileIterator();
                }
            }

            @Override
            public void remove()
            {
                if (wasRemoved) {
                    throw new IllegalStateException(
                            "Tried to remove a file from a listings's recursive iterator twice.");
                } else if (fileIterator != null) {
                    wasRemoved = true;
                    fileIterator.remove();
                } else if (categoriesFileIterator != null) {
                    wasRemoved = true;
                    categoriesFileIterator.remove();
                } else {
                    throw new IllegalStateException(
                            "Tried to delete a file from a listing's recursive iterator before a call to next().");
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
     * Iterator that iterates over all files in this listing and the categories and subcategories.
     * 
     * @param beginWithThis If true, the first value returned by the iterator will be <code>this</code>.
     * @return Iterator that iterates over all files in this listing and the categories and subcategories.
     */
    public Iterator<? extends CategoriesContainer> recursiveCategoryIterator(boolean beginWithThis)
    {
        if (!beginWithThis) {
            return recursiveCategoryIterator();
        } else {
            return new Iterator<CategoriesContainer>() {
                boolean            wasThis          = false;
                Iterator<Category> categoryIterator = recursiveCategoryIterator();

                @Override
                public boolean hasNext()
                {
                    if (!wasThis) {
                        return true;
                    } else if (categoryIterator != null && categoryIterator.hasNext()) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public CategoriesContainer next()
                {
                    if (!wasThis) {
                        wasThis = true;
                        return Listing.this;
                    } else if (categoryIterator != null) {
                        return categoryIterator.next();
                    }
                    throw new NoSuchElementException("There are no more categories in the listing.");
                }

                @Override
                public void remove()
                {
                    if (!wasThis) {
                        throw new IllegalStateException(
                                "Tried to remove a category from a listings's recursive iterator before a call to next().");
                    } else {
                        if (categoryIterator != null && categoryIterator.hasNext()) {
                            try {
                                categoryIterator.remove();
                            } catch (IllegalStateException e) {
                                // if wasThis == true and we get the exception here, it is the case when this was
                                // returned last time and the categoryIterator.next() has not been called yet
                                // we don't allow to remove the whole listing
                                throw new IllegalStateException(
                                        "Tried to remove listing from a listing's category iterator. Not allowed.");
                            }
                        } else if (categoryIterator != null) {
                            categoryIterator.remove();
                        } else {
                            throw new IllegalStateException(
                                    "There are no categories to remove in the listing iterator.");
                        }
                    }
                }

            };
        }
    }

    /**
     * Iterator that iterates over all subcategories in this listing.
     * 
     * @return Iterator that iterates over all subcategories in this listing.
     */
    public Iterator<Category> recursiveCategoryIterator()
    {
        if (getCategories() == null) {
            return new EmptyIterator<Category>();
        }

        return (getCategories()).recursiveCategoryIterator();
    }

    /**
     * Set this listing as the parent of its files and subcategories and recursively do the same for all subcategories.
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

    @Override
    public String toString()
    {
        return "Listing [files=" + files + ", categories=" + categories + ", version=" + version + "]";
    }

    @Override
    public String getHierarchicalId(String separator)
    {
        return "";
    }

    @Override
    public Category getParent()
    {
        return null;
    }
}
