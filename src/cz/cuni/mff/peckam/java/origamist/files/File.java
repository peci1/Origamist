/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * Metadata of a model.
 * 
 * Provides the following bound properties:
 * <ul>
 * <li>isOrigamiLoaded - fired when the origami this file references to gets loaded</li>
 * <li>origami</li>
 * <li>parent</li>
 * <li>loading</li>
 * <li>invalid</li>
 * <li>author</li>
 * <li>license</li>
 * <li>year</li>
 * <li>thumbnail</li>
 * <li>src</li>
 * <li>original</li>
 * </ul>
 * 
 * @author Martin Pecka
 */
public class File extends cz.cuni.mff.peckam.java.origamist.files.jaxb.File implements HierarchicalComponent
{

    /** The origami property. */
    public static final String          ORIGAMI_PROPERTY = "origami";
    /** The parent property. */
    public static final String          PARENT_PROPERTY  = "parent";
    /** The loading property. */
    public static final String          LOADING_PROPERTY = "loading";
    /** The invalid property. */
    public static final String          INVALID_PROPERTY = "invalid";
    /** The loaded property. */
    public static final String          LOADED_PROPERTY  = "loaded";

    /**
     * The hastable for more comfortable search in localized names.
     */
    @XmlTransient
    protected Hashtable<Locale, String> names            = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized short descriptions.
     */
    @XmlTransient
    protected Hashtable<Locale, String> shortDescs       = new Hashtable<Locale, String>();

    /** The origami model corresponding to this file. */
    @XmlTransient
    protected Origami                   origami          = null;

    /** The category or listing this file is contained in. */
    protected transient FilesContainer  parent           = null;

    /** True if the origami is being loaded right now. */
    @XmlTransient
    protected volatile boolean          origamiLoading   = false;

    /** True if this file doesn't point to a valid origami. */
    @XmlTransient
    protected volatile boolean          invalid          = false;

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
     *            content of the first &lt;name&gt; element defined.
     * @return The localized name.
     */
    public String getName(Locale l)
    {
        if (names.size() == 0) {
            String[] parts = getSrc().toString().split("/");
            return parts[parts.length - 1];
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
     * @throws IOException If the source could not be read.
     * @throws UnsupportedDataFormatException If the given source does not contain a valid model.
     */
    public Origami getOrigami() throws UnsupportedDataFormatException, IOException
    {
        return getOrigami(false);
    }

    /**
     * (Potentionally load) and return the model corresponding to this listing entry.
     * 
     * If the referenced file does not contain a valid model, remove this file from its parent.
     * 
     * @param onlyMetadata If true, load only metadata of the model if it has not yet been loaded.
     * 
     * @return The model corresponding to this listing entry.
     * 
     * @throws IOException If the source could not be read.
     * @throws UnsupportedDataFormatException If the given source does not contain a valid model.
     */
    public Origami getOrigami(boolean onlyMetadata) throws UnsupportedDataFormatException, IOException
    {
        return getOrigami(onlyMetadata, true);
    }

    /**
     * (Potentionally load) and return the model corresponding to this listing entry.
     * 
     * @param onlyMetadata If true, load only metadata of the model if it has not yet been loaded.
     * @param autoRemoveBad If the referenced file does not contain a valid model and <code>autoRemoveBad</code> is
     *            <code>true</code>, remove this file from its parent.
     * 
     * @return The model corresponding to this listing entry.
     * 
     * @throws IOException If the source could not be read.
     * @throws UnsupportedDataFormatException If the given source does not contain a valid model.
     */
    public Origami getOrigami(boolean onlyMetadata, boolean autoRemoveBad) throws UnsupportedDataFormatException,
            IOException
    {
        if (origami == null) {
            setOrigamiLoading(true);
            try {
                Origami origami = ServiceLocator.get(OrigamiHandler.class).loadModel(getSrc(), onlyMetadata);
                setOrigami(origami);
                setInvalid(false);
            } catch (UnsupportedDataFormatException e) {
                if (autoRemoveBad && this.parent != null) {
                    this.parent.getFiles().getFile().remove(this);
                }
                setInvalid(true);
                setOrigamiLoading(false);
                throw e;
            } catch (IOException e) {
                if (autoRemoveBad && this.parent != null) {
                    this.parent.getFiles().getFile().remove(this);
                }
                setInvalid(true);
                setOrigamiLoading(false);
                throw e;
            }
        }
        setOrigamiLoading(false);
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
        Origami oldOrigami = this.origami;
        this.origami = origami;
        if (origami != null) {
            origami.setFile(this);
            fillFromOrigami();
        }

        if (oldOrigami == null && origami != null)
            support.firePropertyChange(LOADED_PROPERTY, null, origami);
        if ((oldOrigami != origami && (oldOrigami == null || origami == null))
                || (oldOrigami != null && !oldOrigami.equals(origami))) {
            support.firePropertyChange(ORIGAMI_PROPERTY, oldOrigami, origami);
        }
        if (origami != null)
            setInvalid(false);
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

        setAuthor(origami.getAuthor());
        setLicense(origami.getLicense());
        this.name.clear();
        this.name.addAll(origami.getName());
        setOriginal(origami.getOriginal());
        this.shortdesc.clear();
        this.shortdesc.addAll(origami.getShortdesc());
        setThumbnail(origami.getThumbnail());
        setYear(origami.getYear());
    }

    /**
     * @return The category or listing this file is contained in.
     */
    @XmlTransient
    public FilesContainer getParent()
    {
        return parent;
    }

    /**
     * @param parent The category or listing this file is contained in.
     */
    public void setParent(FilesContainer parent)
    {
        FilesContainer oldParent = this.parent;
        this.parent = parent;
        if ((oldParent != parent && (oldParent == null || parent == null))
                || (oldParent != null && !oldParent.equals(parent)))
            support.firePropertyChange(PARENT_PROPERTY, oldParent, parent);
    }

    @Override
    public String toString()
    {
        return "File [names=" + names + ", shortDescs=" + shortDescs + ", author=" + author + ", name=" + name
                + ", year=" + year + ", shortdesc=" + shortdesc + ", license=" + license + ", original=" + original
                + ", thumbnail=" + thumbnail + ", src=" + src + "]";
    }

    @Override
    public String getHierarchicalId(String separator)
    {
        return parent.getHierarchicalId(separator) + separator + this.getName(Locale.getDefault());
    }

    /**
     * @return True if the origami belonging to this file has been loaded.
     */
    public boolean isOrigamiLoaded()
    {
        return origami != null;
    }

    /**
     * @return True if the origami is being loaded right now.
     */
    public boolean isOrigamiLoading()
    {
        return origamiLoading;
    }

    /**
     * @param origamiLoading True if the origami is being loaded right now.
     */
    protected void setOrigamiLoading(boolean origamiLoading)
    {
        boolean oldLoading = this.origamiLoading;
        this.origamiLoading = origamiLoading;
        if (oldLoading != origamiLoading)
            support.firePropertyChange(LOADING_PROPERTY, oldLoading, origamiLoading);
    }

    /**
     * @return False if this file points to a valid origami or the origami isn't loaded.
     */
    public boolean isInvalid()
    {
        return invalid;
    }

    /**
     * @param invalid Set to true to indicate this file doesn't point to a valid origami.
     */
    public void setInvalid(boolean invalid)
    {
        boolean oldInvalid = this.invalid;
        this.invalid = invalid;
        if (oldInvalid != invalid)
            support.firePropertyChange(INVALID_PROPERTY, oldInvalid, invalid);
    }

    @Override
    protected String[] getNonChildProperties()
    {
        return new String[] { File.PARENT_PROPERTY, File.INVALID_PROPERTY, File.LOADED_PROPERTY, File.LOADING_PROPERTY,
                File.ORIGAMI_PROPERTY };
    }
}
