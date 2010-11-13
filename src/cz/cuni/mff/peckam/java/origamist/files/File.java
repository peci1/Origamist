/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URI;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.datatype.XMLGregorianCalendar;

import cz.cuni.mff.peckam.java.origamist.common.Author;
import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.common.License;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Image;
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
 * isOrigamiLoaded - fired when the origami this file references to gets loaded
 * origami
 * author
 * license
 * year
 * thumbnail
 * src
 * original
 * 
 * @author Martin Pecka
 */
public class File extends cz.cuni.mff.peckam.java.origamist.files.jaxb.File implements HierarchicalComponent
{

    /**
     * The hastable for more comfortable search in localized names.
     */
    @XmlTransient
    protected Hashtable<Locale, String> names             = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized short descriptions.
     */
    @XmlTransient
    protected Hashtable<Locale, String> shortDescs        = new Hashtable<Locale, String>();

    /** The origami model corresponding to this file. */
    @XmlTransient
    protected Origami                   origami           = null;

    /** The category or listing this file is contained in. */
    @XmlTransient
    protected FilesContainer            parent            = null;

    /** Property change listeners. */
    @XmlTransient
    protected PropertyChangeSupport     propertyListeners = new PropertyChangeSupport(this);

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
            try {
                origami = ServiceLocator.get(OrigamiHandler.class).loadModel(getSrc(), onlyMetadata);
                origami.setFile(this);
                propertyListeners.firePropertyChange("isOrigamiLoaded", null, origami);
                propertyListeners.firePropertyChange("origami", null, origami);
            } catch (UnsupportedDataFormatException e) {
                if (autoRemoveBad && this.parent != null) {
                    this.parent.getFiles().getFile().remove(this);
                    this.parent = null;
                }
                throw e;
            } catch (IOException e) {
                if (autoRemoveBad && this.parent != null) {
                    this.parent.getFiles().getFile().remove(this);
                    this.parent = null;
                }
                throw e;
            }
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
        Origami oldOrigami = this.origami;
        this.origami = origami;

        if (oldOrigami == null && origami != null)
            propertyListeners.firePropertyChange("isOrigamiLoaded", null, origami);
        if ((oldOrigami == null && origami != null) || (oldOrigami != null && origami == null)
                || (oldOrigami != null && origami != null && !oldOrigami.equals(origami))) {
            propertyListeners.firePropertyChange("origami", oldOrigami, origami);
        }
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

        setAuthor((Author) origami.getAuthor());
        setLicense((License) origami.getLicense());
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

    @Override
    public void setAuthor(cz.cuni.mff.peckam.java.origamist.common.jaxb.Author value)
    {
        cz.cuni.mff.peckam.java.origamist.common.jaxb.Author oldValue = getAuthor();
        super.setAuthor(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("author", oldValue, value);
    }

    @Override
    public void setYear(XMLGregorianCalendar value)
    {
        XMLGregorianCalendar oldValue = getYear();
        super.setYear(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("year", oldValue, value);
    }

    @Override
    public void setLicense(cz.cuni.mff.peckam.java.origamist.common.jaxb.License value)
    {
        cz.cuni.mff.peckam.java.origamist.common.jaxb.License oldValue = getLicense();
        super.setLicense(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("license", oldValue, value);
    }

    @Override
    public void setOriginal(URI value)
    {
        URI oldValue = getOriginal();
        super.setOriginal(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("original", oldValue, value);
    }

    @Override
    public void setThumbnail(Image value)
    {
        Image oldValue = getThumbnail();
        super.setThumbnail(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("thumbnail", oldValue, value);
    }

    @Override
    public void setSrc(URI value)
    {
        URI oldValue = getSrc();
        super.setSrc(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("src", oldValue, value);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyListeners.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyListeners.removePropertyChangeListener(listener);
    }

    /**
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        return propertyListeners.getPropertyChangeListeners();
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        propertyListeners.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        propertyListeners.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @return
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
    {
        return propertyListeners.getPropertyChangeListeners(propertyName);
    }

}
