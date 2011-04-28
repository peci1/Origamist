/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.xml.bind.annotation.XmlTransient;

/**
 * An origami model.
 * <p>
 * Provides property: origami
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Model extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Model
{
    /** The origami property. */
    public static final String  ORIGAMI_PROPERTY = "origami";

    /** The origami this model belongs to. */
    protected transient Origami origami          = null;

    /**
     * 
     */
    public Model()
    {
        addPropertyChangeListener(STEPS_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getOldValue() != null)
                    ((Steps) evt.getOldValue()).setModel(null);
                if (evt.getNewValue() != null)
                    ((Steps) evt.getNewValue()).setModel(Model.this);
            }
        });
    }

    /**
     * @return The origami this model belongs to.
     */
    public Origami getOrigami()
    {
        return origami;
    }

    /**
     * @param origami The origami this model belongs to.
     */
    void setOrigami(Origami origami)
    {
        Origami old = this.origami;
        this.origami = origami;
        if ((old != origami && (old == null || origami == null)) || (old != null && !old.equals(origami)))
            support.firePropertyChange(ORIGAMI_PROPERTY, old, origami);
    }
}
