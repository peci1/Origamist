/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.common.jaxb.Image;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Model;
import cz.cuni.mff.peckam.java.origamist.modelstate.DefaultModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * The origami diagram.
 * 
 * Provides the following bound properties:
 * author
 * license
 * year
 * thumbnail
 * src
 * original
 * model
 * paper
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Origami extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami
{
    /**
     * The hastable for more comfortable search in localized names.
     */
    protected Hashtable<Locale, String> names             = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized short descriptions.
     */
    protected Hashtable<Locale, String> shortDescs        = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized descriptions.
     */
    protected Hashtable<Locale, String> descriptions      = new Hashtable<Locale, String>();

    /** If the origami is loaded without the model, then this task will be run the first time the model is read. */
    protected Callable<Model>           loadModelCallable = null;

    /** The file in the listing containing this origami. */
    protected File                      file              = null;

    /**
     * The URL this origami was created from. Obviously this will be <code>null</code> for the just-being-created model.
     */
    protected URL                       src               = null;

    /** Property change listeners. */
    @XmlTransient
    protected PropertyChangeSupport     propertyListeners = new PropertyChangeSupport(this);

    /**
     * Create a new origami diagram.
     */
    public Origami()
    {
        ((ObservableList<LangString>) getName()).addObserver(new LangStringHashtableObserver(names));
        ((ObservableList<LangString>) getShortdesc()).addObserver(new LangStringHashtableObserver(shortDescs));
        ((ObservableList<LangString>) getDescription()).addObserver(new LangStringHashtableObserver(descriptions));
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
            ResourceBundle b = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.model.Origami", l);
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
        LangString s = (LangString) new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory()
                .createLangString();
        s.setLang(l);
        s.setValue(desc);
        this.description.add(s);
    }

    @Override
    public Model getModel()
    {
        if (loadModelCallable != null) {
            try {
                // TODO notify the user about loading
                Callable<Model> callable = loadModelCallable;
                loadModelCallable = null;
                this.model = callable.call();
                initSteps();
            } catch (Exception e) {
                this.model = new ObjectFactory().createModel();
                Logger.getLogger("application").l7dlog(Level.ERROR, "modelLazyLoadException", e);
            }
        }
        return super.getModel();
    }

    /**
     * @param loadModelCallable the loadModelCallable to set
     */
    public void setLoadModelCallable(Callable<Model> loadModelCallable)
    {
        this.loadModelCallable = loadModelCallable;
    }

    /**
     * @return the file
     */
    public File getFile()
    {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    /**
     * @return the src
     */
    public URL getSrc()
    {
        return src;
    }

    /**
     * @param value the new src
     */
    public void setSrc(URL value)
    {
        URL oldValue = getSrc();
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("src", oldValue, value);
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
    public void setPaper(DiagramPaper value)
    {
        DiagramPaper oldValue = getPaper();
        super.setPaper(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("paper", oldValue, value);
    }

    @Override
    public void setModel(Model value)
    {
        Model oldValue = getModel();
        super.setModel(value);
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            propertyListeners.firePropertyChange("model", oldValue, value);
    }

    /**
     * If this isn't the newest version of the diagram, convert it to the newest one. It this is the newest version,
     * just
     * return <code>this</code>.
     * 
     * @return The newest version of the diagram.
     */
    public Origami convertToNewestVersion()
    {
        // TODO if a new diagram schema version is developed, make this code convert the objects older objects to the
        // newer
        return this;
    }

    public void initSteps()
    {
        List<cz.cuni.mff.peckam.java.origamist.model.Step> list = this.getModel().getSteps().getStep();

        ModelState defaultModelState = new DefaultModelState(this);
        if (list.size() == 0)
            return;
        else if (list.size() == 1) {
            ((Step) list.get(0)).setDefaultModelState(defaultModelState);
            return;
        } else {
            Iterator<cz.cuni.mff.peckam.java.origamist.model.Step> i = list.iterator();
            Step prev = null, curr = null, next = (Step) i.next();
            next.setDefaultModelState(defaultModelState);
            while (i.hasNext()) {
                prev = curr;
                curr = next;
                next = (Step) i.next();
                curr.setPrevious(prev);
                curr.setNext(next);
            }
            prev = curr;
            curr = next;
            next = null;
            curr.setPrevious(prev);
            curr.setNext(next);

            return;
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((src == null) ? 0 : src.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Origami other = (Origami) obj;
        if (src == null) {
            if (other.src != null)
                return false;
        } else if (!src.equals(other.src))
            return false;
        return true;
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
