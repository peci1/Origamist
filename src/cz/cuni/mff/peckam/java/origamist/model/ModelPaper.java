/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * The paper the model is made of.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class ModelPaper extends cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper
{

    /**
     * The hastable for more comfortable search in localized notes.
     */
    @XmlTransient
    protected Hashtable<Locale, String> notes = new Hashtable<Locale, String>();

    /**
     * Create new model paper.
     */
    public ModelPaper()
    {
        ((ObservableList<LangString>) getNote()).addObserver(new LangStringHashtableObserver(notes));
    }

    /**
     * @return Color of the background of the paper
     */
    public Color getBackgroundColor()
    {
        return colors.getBackground();
    }

    /**
     * Set the background color of the paper
     * 
     * @param c The color to be set
     */
    public void setBackgroundColor(Color c)
    {
        colors.setBackground(c);
    }

    /**
     * @return Color of the foreground of the paper
     */
    public Color getForegroundColor()
    {
        return colors.getForeground();
    }

    /**
     * Set the foreground color of the paper
     * 
     * @param c The color to be set
     */
    public void setForegroundColor(Color c)
    {
        colors.setForeground(c);
    }

    /**
     * Return the localized note to the model paper.
     * 
     * @param l The locale of the note. If null or not found, returns the
     *            content of the first &lt;note> element defined
     * @return The localized note
     */
    public String getNote(Locale l)
    {
        if (notes.size() == 0) {
            ResourceBundle b = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.model.ModelPaper", l);
            return b.getString("noteNotFound");
        }

        if (l == null || !notes.containsKey(l))
            return notes.elements().nextElement();
        return notes.get(l);
    }

    /**
     * Add a note in the given locale.
     * 
     * @param l The locale of the note
     * @param note The note to add
     */
    public void addNote(Locale l, String note)
    {
        LangString s = (LangString) new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(note);
        this.note.add(s);
    }
}
