/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.Author;
import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.common.License;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * Metadata of a model.
 * 
 * @author Martin Pecka
 */
public class File extends cz.cuni.mff.peckam.java.origamist.files.jaxb.File
{

    /**
     * The hastable for more comfortable search in localized names.
     */
    @XmlTransient
    protected Hashtable<Locale, String> names      = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized short descriptions.
     */
    @XmlTransient
    protected Hashtable<Locale, String> shortDescs = new Hashtable<Locale, String>();

    /** The origami model corresponding to this file. */
    @XmlTransient
    protected Origami                   origami    = null;

    /** The category or listing this file is contained in. */
    @XmlTransient
    protected FilesContainer            parent     = null;

    /**
     * Create a new origami metadata.
     */
    public File()
    {
        ((ObservableList<LangString>) getName()).addObserver(new LangStringHashtableObserver(names));
        ((ObservableList<LangString>) getShortdesc()).addObserver(new LangStringHashtableObserver(shortDescs));
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
            ResourceBundle b = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.model.Origami", l);
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
        LangString s = (LangString) new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
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
            ResourceBundle b = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.model.Origami", l);
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
        LangString s = (LangString) new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(desc);
        this.shortdesc.add(s);
    }

    /**
     * (Potentionally load) and return the model corresponding to this listing entry.
     * 
     * @return The model corresponding to this listing entry.
     * 
     * @throws MalformedURLException If the source is invalid.
     * @throws IOException If the source could not be read.
     * @throws UnsupportedDataFormatException If the given source does not contain a valid model.
     */
    public Origami getOrigami() throws UnsupportedDataFormatException, MalformedURLException, IOException
    {
        return getOrigami(false);
    }

    /**
     * (Potentionally load) and return the model corresponding to this listing entry.
     * 
     * @param onlyMetadata If true, load only metadata of the model if it has not yet been loaded.
     * 
     * @return The model corresponding to this listing entry.
     * 
     * @throws MalformedURLException If the source is invalid.
     * @throws IOException If the source could not be read.
     * @throws UnsupportedDataFormatException If the given source does not contain a valid model.
     */
    public Origami getOrigami(boolean onlyMetadata) throws UnsupportedDataFormatException, MalformedURLException,
            IOException
    {
        if (origami == null) {
            origami = ServiceLocator.get(OrigamiHandler.class).loadModel(getSrc(), onlyMetadata);
            origami.setFile(this);
        }
        return origami;

    }

    /**
     * Sets the model this file represents (particulary this doesn't update the file's metadata from the origami's
     * metadata).
     * 
     * @param origami The origami to set.
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
    }

    /**
     * Fills this file's metadata from the metadata of this file's origami.
     * 
     * @throws IllegalStateException If the origami has not yet been loaded.
     */
    public void fillFromOrigami() throws IllegalStateException
    {
        if (origami == null)
            throw new IllegalStateException(
                    "Tried to fill a File's metadata from its origami, but the origami has not been loaded yet.");

        this.author = (Author) origami.getAuthor();
        this.license = (License) origami.getLicense();
        this.name.clear();
        this.name.addAll(origami.getName());
        this.original = origami.getOriginal();
        this.shortdesc.clear();
        this.shortdesc.addAll(origami.getShortdesc());
        this.thumbnail = origami.getThumbnail();
        this.year = origami.getYear();
    }

    /**
     * @return The category or listing this file is contained in.
     */
    public FilesContainer getParent()
    {
        return parent;
    }

    /**
     * @param parent The category or listing this file is contained in.
     */
    public void setParent(FilesContainer parent)
    {
        this.parent = parent;
    }

    @Override
    public String toString()
    {
        return "File [names=" + names + ", shortDescs=" + shortDescs + ", author=" + author + ", name=" + name
                + ", year=" + year + ", shortdesc=" + shortdesc + ", license=" + license + ", original=" + original
                + ", thumbnail=" + thumbnail + ", src=" + src + "]";
    }

}
