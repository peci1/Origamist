/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.common.Author;
import cz.cuni.mff.peckam.java.origamist.common.BinaryImage;
import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.common.License;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.modelstate.DefaultModelState;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyEvent;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyListener;

/**
 * The origami diagram.
 * <p>
 * Provided property: src
 * <p>
 * See {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami} for other bound properties.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Origami extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami
{
    /**
     * The hastable for more comfortable search in localized names.
     */
    protected final Hashtable<Locale, String> names             = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized short descriptions.
     */
    protected final Hashtable<Locale, String> shortDescs        = new Hashtable<Locale, String>();
    /**
     * The hastable for more comfortable search in localized descriptions.
     */
    protected final Hashtable<Locale, String> descriptions      = new Hashtable<Locale, String>();

    /** If the origami is loaded without the model, then this task will be run the first time the model is read. */
    protected Callable<Model>                 loadModelCallable = null;

    /** The file in the listing containing this origami. */
    protected File                            file              = null;

    /**
     * The URL this origami was created from. Obviously this will be <code>null</code> for the just-being-created model.
     */
    protected URL                             src               = null;

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
                init();
            } catch (Exception e) {
                this.model = (Model) new ObjectFactory().createModel();
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
        URL oldValue = this.src;
        this.src = value;
        if ((oldValue == null && value != null) || (oldValue != null && value == null)
                || (oldValue != null && value != null && !oldValue.equals(value)))
            support.firePropertyChange("src", oldValue, value);
    }

    /**
     * Initializes the substructures, so that no structure that may contain other structures will be <code>null</code>.
     */
    public void initStructure(boolean preserveExisting)
    {
        ObjectFactory of = new ObjectFactory();
        cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory cof = new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory();

        if (!preserveExisting || getAuthor() == null)
            this.setAuthor((Author) cof.createAuthor());
        if (!preserveExisting || getLicense() == null)
            this.setLicense((License) cof.createLicense());
        if (!preserveExisting || getThumbnail() == null)
            this.setThumbnail(cof.createImage());
        if (!preserveExisting || getThumbnail().getImage() == null)
            this.getThumbnail().setImage((BinaryImage) cof.createBinaryImage());
        if (!preserveExisting || getModel() == null)
            this.setModel((Model) of.createModel());
        if (!preserveExisting || getModel().getPaper() == null)
            this.getModel().setPaper((ModelPaper) of.createModelPaper());
        if (!preserveExisting || getModel().getPaper().getSize() == null)
            this.getModel().getPaper().setSize((UnitDimension) of.createUnitDimension());
        if (!preserveExisting || getModel().getPaper().getColors() == null)
            this.getModel().getPaper().setColors(of.createModelColors());
        if (!preserveExisting || getModel().getSteps() == null)
            this.getModel().setSteps((cz.cuni.mff.peckam.java.origamist.model.Steps) of.createSteps());
        if (!preserveExisting || getPaper() == null)
            this.setPaper((DiagramPaper) of.createDiagramPaper());
        if (!preserveExisting || getPaper().getColor() == null)
            this.getPaper().setColor(of.createDiagramColors());
        if (!preserveExisting || getPaper().getSize() == null)
            this.getPaper().setSize((UnitDimension) of.createUnitDimension());
    }

    /**
     * Set the given origami's metadata to be this origami's metadata. The new metadata are another instance of the
     * <code>from</code>'s metadata, so it doesn't reflect any further changes to the <code>from</code>'s metadata.
     * 
     * @param from The origami the metadata should be loaded from.
     */
    public void getMetadataFrom(Origami from)
    {
        // reset the metadata to the empty ones
        initStructure(true);

        if (!getName().equals(from.getName())) {
            getName().clear();
            for (LangString name : from.getName())
                getName().add(name.clone());
        }
        setYear((XMLGregorianCalendar) from.getYear().clone());
        if (!getShortdesc().equals(from.getShortdesc())) {
            getShortdesc().clear();
            for (LangString shortDesc : from.getShortdesc())
                getShortdesc().add(shortDesc.clone());
        }
        if (!getDescription().equals(from.getDescription())) {
            getDescription().clear();
            for (LangString desc : from.getDescription())
                getDescription().add(desc.clone());
        }
        try {
            setOriginal(new URI(from.getOriginal().toString()));
        } catch (URISyntaxException e) {} catch (NullPointerException e) {}

        getAuthor().setName(from.getAuthor().getName());
        try {
            getAuthor().setHomepage(new URI(from.getAuthor().getHomepage().toString()));
        } catch (URISyntaxException e) {} catch (NullPointerException e) {}

        getLicense().setContent(from.getLicense().getContent());
        try {
            getLicense().setHomepage(new URI(from.getLicense().getHomepage().toString()));
        } catch (URISyntaxException e) {} catch (NullPointerException e) {}
        getLicense().setName(from.getLicense().getName());
        if (!getLicense().getPermission().equals(from.getLicense().getPermission())) {
            getLicense().getPermission().clear();
            getLicense().getPermission().addAll(from.getLicense().getPermission());
        }

        byte[] newImage = Arrays.copyOf(from.getThumbnail().getImage().getValue(), from.getThumbnail().getImage()
                .getValue().length);
        getThumbnail().getImage().setType(from.getThumbnail().getImage().getType());
        getThumbnail().getImage().setValue(newImage);

        getModel().getPaper().setWeight(from.getModel().getPaper().getWeight());
        getModel().getPaper().getColors().setBackground(from.getModel().getPaper().getColors().getBackground());
        getModel().getPaper().getColors().setForeground(from.getModel().getPaper().getColors().getForeground());
        getModel().getPaper().getSize().setWidth(from.getModel().getPaper().getSize().getWidth());
        getModel().getPaper().getSize().setHeight(from.getModel().getPaper().getSize().getHeight());
        getModel().getPaper().getSize().setUnit(from.getModel().getPaper().getSize().getUnit());
        getModel()
                .getPaper()
                .getSize()
                .setReference(from.getModel().getPaper().getSize().getReferenceUnit(),
                        from.getModel().getPaper().getSize().getReferenceLength());
        if (!getModel().getPaper().getNote().equals(from.getModel().getPaper().getNote())) {
            getModel().getPaper().getNote().clear();
            for (LangString s : from.getModel().getPaper().getNote())
                getModel().getPaper().addNote(s.getLang(), s.getValue());
        }

        getPaper().getColor().setBackground(from.getPaper().getColor().getBackground());
        getPaper().setCols(from.getPaper().getCols());
        getPaper().setRows(from.getPaper().getRows());
        getPaper().getSize().setWidth(from.getPaper().getSize().getWidth());
        getPaper().getSize().setHeight(from.getPaper().getSize().getHeight());
        getPaper().getSize().setUnit(from.getPaper().getSize().getUnit());
        getPaper().getSize().setReference(from.getPaper().getSize().getReferenceUnit(),
                from.getPaper().getSize().getReferenceLength());
    }

    @Override
    protected void init()
    {
        super.init();
        initListeners();
    }

    /**
     * Create pointers to previous/next steps and setup model state invalidation callbacks.
     */
    public void initListeners()
    {
        PropertyChangeListener invalidateListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (getModel() != null && getModel().getSteps() != null)
                    getModel().getSteps().invalidateSteps();
            }
        };
        ObservablePropertyListener<?> invalidateObservableListener = new ObservablePropertyListener<Object>() {
            @Override
            public void changePerformed(ObservablePropertyEvent<?> evt)
            {
                if (getModel() != null && getModel().getSteps() != null)
                    getModel().getSteps().invalidateSteps();
            }
        };

        addPrefixedPropertyChangeListener(invalidateListener, MODEL_PROPERTY, Model.PAPER_PROPERTY,
                ModelPaper.COLORS_PROPERTY);
        addPrefixedPropertyChangeListener(invalidateListener, MODEL_PROPERTY, Model.PAPER_PROPERTY,
                ModelPaper.SIZE_PROPERTY);
        addPrefixedPropertyChangeListener(invalidateListener, MODEL_PROPERTY, Model.STEPS_PROPERTY,
                Steps.STEP_PROPERTY, Step.IMAGE_PROPERTY);
        addPrefixedObservablePropertyListener(invalidateObservableListener, MODEL_PROPERTY, Model.STEPS_PROPERTY,
                Steps.STEP_PROPERTY, Step.OPERATIONS_PROPERTY);

        ObservablePropertyListener<Step> defaultStateListener = new ObservablePropertyListener<Step>() {
            @Override
            public void changePerformed(ObservablePropertyEvent<? extends Step> evt)
            {
                if (evt.getEvent().getItem().getPrevious() == null
                        && (evt.getEvent().getItem().defaultModelState == null || evt.getEvent().getItem().defaultModelState
                                .getOrigami() != Origami.this))
                    evt.getEvent().getItem().defaultModelState = new DefaultModelState(Origami.this);
            }
        };

        addObservablePropertyListener(defaultStateListener, MODEL_PROPERTY, Model.STEPS_PROPERTY, Steps.STEP_PROPERTY);

        PropertyChangeListener defaultStatePropertyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (getModel() != null && getModel().getSteps() != null && getModel().getSteps().getStep().size() > 0) {
                    Step firstStep = getModel().getSteps().getStep().get(0);
                    if (firstStep.defaultModelState == null || firstStep.defaultModelState.getOrigami() != Origami.this)
                        firstStep.defaultModelState = new DefaultModelState(Origami.this);
                }
            }
        };

        addPrefixedPropertyChangeListener(defaultStatePropertyListener, MODEL_PROPERTY);
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
}
