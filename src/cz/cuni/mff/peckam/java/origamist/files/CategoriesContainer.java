/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

/**
 * An object containing a cz.cuni.mff.peckam.java.origamist.files.jaxb.Categories field.
 * 
 * @author Martin Pecka
 */
public interface CategoriesContainer extends HierarchicalComponent
{
    /**
     * Returns the container of the list of subcategories.
     * 
     * If no <code>&lt;categories&gt;</code> tag was set, returns <code>null</code>.
     * 
     * @return The container of the list of subcategories.
     */
    cz.cuni.mff.peckam.java.origamist.files.Categories getCategories();

    /**
     * Sets the container of the list of subcategories.
     * 
     * @param files The container to set.
     */
    void setCategories(cz.cuni.mff.peckam.java.origamist.files.Categories categories);

    /**
     * Create a category with the given id. If the id contains slashes ("/"), it is concerned as a category id hierarchy
     * and all missing categories are created.
     * 
     * Categories "" and "/" are concerned as the listing.
     * 
     * @param categoryString The category to create. It is written in the form "cat1id/cat2id/cat3id".
     * @return The created category.
     */
    CategoriesContainer createSubCategories(String categoryString);
}
