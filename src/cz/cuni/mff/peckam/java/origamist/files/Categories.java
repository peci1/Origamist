/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.util.Hashtable;

import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;
import cz.cuni.mff.peckam.java.origamist.utils.HashtableChangeNotificationListener;
import cz.cuni.mff.peckam.java.origamist.utils.HashtableElementAdapter;

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
        ((ChangeNotifyingList<Category>) category)
                .addChangeListener(new HashtableChangeNotificationListener<Category, String, Category>(categories,
                        new HashtableElementAdapter<Category, String, Category>() {

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

                        }));
    }

}
