/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;
import cz.cuni.mff.peckam.java.origamist.utils.HashtableChangeNotificationListener;

/**
 * The origami diagram.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Origami extends
        cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami
{
    /**
     * The hastable for more comfortable search in localized names.
     */
    protected Hashtable<Locale, String> names        = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized short descriptions.
     */
    protected Hashtable<Locale, String> shortDescs   = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized descriptions.
     */
    protected Hashtable<Locale, String> descriptions = new Hashtable<Locale, String>();

    /**
     * Create a new origami diagram.
     */
    public Origami()
    {
        ((ChangeNotifyingList<LangString>) name)
                .addChangeListener(new HashtableChangeNotificationListener(
                        names));
        ((ChangeNotifyingList<LangString>) shortdesc)
                .addChangeListener(new HashtableChangeNotificationListener(
                        shortDescs));
        ((ChangeNotifyingList<LangString>) description)
                .addChangeListener(new HashtableChangeNotificationListener(
                        descriptions));
    }

    /**
     * Return the localized name of the model.
     * 
     * @param l The locale of the name. If null or not found, returns the
     *            content of the first &lt;name> element defined
     * @return The localized note
     */
    public String getName(Locale l)
    {
        if (names.size() == 0) {
            ResourceBundle b = ResourceBundle.getBundle(
                    "cz.cuni.mff.peckam.java.origamist.model.Origami", l);
            return b.getString("nameNotFound");
        }

        if (l == null || !names.containsKey(l))
            return names.elements().nextElement();
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
        LangString s = new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(name);
        this.name.add(s);
    }

    /**
     * Return the localized short description of the model.
     * 
     * @param l The locale of the short descripton. If null or not found,
     *            returns the content of the first &lt;shortdesc> element
     *            defined
     * @return The localized note
     */
    public String getShortDesc(Locale l)
    {
        if (shortDescs.size() == 0) {
            ResourceBundle b = ResourceBundle.getBundle(
                    "cz.cuni.mff.peckam.java.origamist.model.Origami", l);
            return b.getString("shortDescNotFound");
        }

        if (l == null || !shortDescs.containsKey(l))
            return shortDescs.elements().nextElement();
        return shortDescs.get(l);
    }

    /**
     * Add a short description in the given locale.
     * 
     * @param l The locale of the short description
     * @param desc The short description to add to add
     */
    public void addShortDesc(Locale l, String desc)
    {
        LangString s = new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(desc);
        this.shortdesc.add(s);
    }

    /**
     * Return the localized description of the model.
     * 
     * @param l The locale of the description. If null or not found, returns the
     *            content of the first &lt;description> element defined
     * @return The localized description
     */
    public String getDescription(Locale l)
    {
        if (descriptions.size() == 0) {
            // TODO possible fallback to short description
            ResourceBundle b = ResourceBundle.getBundle(
                    "cz.cuni.mff.peckam.java.origamist.model.Origami", l);
            return b.getString("descriptionNotFound");
        }

        if (l == null || !descriptions.containsKey(l))
            return descriptions.elements().nextElement();
        return descriptions.get(l);
    }

    /**
     * Add a description in the given locale.
     * 
     * @param l The locale of the description
     * @param name The description to add
     */
    public void addDescription(Locale l, String desc)
    {
        LangString s = new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(desc);
        this.description.add(s);
    }

}