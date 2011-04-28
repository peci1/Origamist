/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeListener;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.CustomPropertyChangeSupport;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;

/**
 * The steps the model consists of.
 * 
 * Provides property: model
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Steps extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Steps
{
    /** The model property. */
    public static final String            MODEL_PROPERTY = "model";

    /** The model these steps belong to. */
    protected Model                       model          = null;

    protected CustomPropertyChangeSupport support        = new CustomPropertyChangeSupport(this);

    public Steps()
    {
        // this observer handles the next/previous properties of Steps added to this list to reflect the steps'
        // positions in the list
        ((ObservableList<Step>) getStep()).addObserver(new Observer<Step>() {
            @Override
            public void changePerformed(ChangeNotification<Step> change)
            {
                if (change.getChangeType() != ChangeTypes.ADD) {
                    if (change.getOldItem().getPrevious() != null) {
                        change.getOldItem().getPrevious().setNext(change.getOldItem().getNext());
                    }
                    if (change.getOldItem().getNext() != null) {
                        change.getOldItem().getNext().setPrevious(change.getOldItem().getPrevious());
                    }
                    change.getOldItem().setNext(null);
                    change.getOldItem().setPrevious(null);
                    if (change.getChangeType() == ChangeTypes.REMOVE)
                        change.getOldItem().setSteps(null);
                }

                if (change.getChangeType() != ChangeTypes.REMOVE) {
                    int index = step.indexOf(change.getItem());
                    if (index > 0) {
                        Step prev = step.get(index - 1);
                        change.getItem().setPrevious(prev);
                        prev.setNext(change.getItem());
                    }
                    if (index + 1 < step.size()) {
                        Step next = step.get(index + 1);
                        change.getItem().setNext(next);
                        next.setPrevious(change.getItem());
                    }
                    if (change.getChangeType() == ChangeTypes.ADD)
                        change.getItem().setSteps(Steps.this);
                }
            }
        });
    }

    /**
     * @return The model these steps belong to.
     */
    public Model getModel()
    {
        return model;
    }

    /**
     * @param model The model these steps belong to.
     */
    void setModel(Model model)
    {
        Model old = this.model;
        this.model = model;
        if ((old != model && (old == null || model == null)) || (old != null && !old.equals(model)))
            support.firePropertyChange(MODEL_PROPERTY, old, model);
    }

    /**
     * @param listener
     * @return
     * @see cz.cuni.mff.peckam.java.origamist.utils.CustomPropertyChangeSupport#removeAllListeners(java.beans.PropertyChangeListener)
     */
    public List<String> removeAllListeners(PropertyChangeListener listener)
    {
        return support.removeAllListeners(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(propertyName, listener);
    }
}
