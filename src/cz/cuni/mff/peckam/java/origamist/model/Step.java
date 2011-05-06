/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Locale;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyEvent;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyListener;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;

/**
 * A step of the model creation.
 * <p>
 * A step is a group of operations displayed at once.
 * <p>
 * Provides property: previous
 * <p>
 * Provides property: next
 * <p>
 * Provides property: steps
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Step extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Step
{

    /** The previous property. */
    public static final String                PREVIOUS_PROPERTY = "previous:cz.cuni.mff.peckam.java.origamist.model.Step";

    /** The next property. */
    public static final String                NEXT_PROPERTY     = "next:cz.cuni.mff.peckam.java.origamist.model.Step";

    /**
     * The hastable for more comfortable search in localized descriptions.
     */
    protected final Hashtable<Locale, String> descriptions      = new Hashtable<Locale, String>();

    /**
     * The cached model state after performing this step.
     */
    protected ModelState                      modelState        = null;

    /**
     * If this is the first step, use this model state as the previous one.
     */
    protected ModelState                      defaultModelState = null;

    /**
     * The step preceeding this one. If this is the first one, previous is null.
     */
    protected Step                            previous          = null;

    /**
     * The step succeeding this one. If this is the last one, next is null.
     */
    protected Step                            next              = null;

    /** The {@link Steps} object this step is part of. */
    protected Steps                           steps             = null;

    /**
     * Create a new step.
     */
    public Step()
    {
        setId(1); // the default step id

        ((ObservableList<LangString>) getDescription()).addObserver(new LangStringHashtableObserver(descriptions));
        addPropertyChangeListener(IMAGE_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                invalidateThisModelState();
            }
        });
        ((ObservableList<Operation>) getOperations()).addObserver(new Observer<Operation>() {
            @Override
            public void changePerformed(ChangeNotification<? extends Operation> change)
            {
                invalidateModelState();
            }
        });
        addPrefixedObservablePropertyListener(new ObservablePropertyListener<Operation>() {
            @Override
            public void changePerformed(ObservablePropertyEvent<? extends Operation> evt)
            {
                invalidateModelState();
            }
        }, OPERATIONS_PROPERTY);
        addPrefixedPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                invalidateModelState();
            }
        }, OPERATIONS_PROPERTY);

        if (zoom == null)
            zoom = 100.0;
    }

    /**
     * Return the localized description of the step.
     * 
     * @param l The locale of the description.
     * @return The localized description or null if l is null or not found
     */
    public String getDescription(Locale l)
    {
        if (descriptions.size() == 0) {
            // here we really don't want to display a not found message;
            // instead, we signalize that no message is defined, so its
            // space may be used in another way
            return null;
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

    /**
     * Perform folding from the previous step's state to a new state by this step.
     * 
     * @return The state the model would have after performing this step.
     * 
     * @throws InvalidOperationException If an operation cannot be done.
     */
    public synchronized ModelState getModelState() throws InvalidOperationException
    {
        if (this.modelState != null)
            return this.modelState;

        if (previous != null) {
            this.modelState = previous.getModelState().clone();
        } else {
            if (this.defaultModelState == null) {
                throw new IllegalStateException("Cannot create the default model state for step " + getId());
            }
            this.modelState = this.defaultModelState.clone();
        }

        this.modelState.proceedToNextStep();

        this.modelState.setStep(this);
        if (operations == null)
            return this.modelState;

        for (Operation o : operations) {
            try {
                this.modelState = o.getModelState(this.modelState);
            } catch (InvalidOperationException e) {
                if (e.getOperation() == null)
                    e.setOperation(o);
                throw e;
            }
        }

        return this.modelState;
    }

    /**
     * @param previous the previous step
     */
    public void setPrevious(Step previous)
    {
        Step old = this.previous;
        this.previous = previous;
        if ((old != previous && (old == null || previous == null)) || (old != null && !old.equals(previous))) {
            support.firePropertyChange(PREVIOUS_PROPERTY, old, previous);
            invalidateModelState();
        }
    }

    /**
     * @param next the next step
     */
    public void setNext(Step next)
    {
        Step old = this.next;
        this.next = next;
        if ((old != next && (old == null || next == null)) || (old != null && !old.equals(next))) {
            support.firePropertyChange(NEXT_PROPERTY, old, next);
            invalidateModelState();
        }
    }

    /**
     * @return the previous
     */
    public Step getPrevious()
    {
        return previous;
    }

    /**
     * @return the next
     */
    public Step getNext()
    {
        return next;
    }

    /**
     * @param modelState The model state to be used as the previous for the first step. Has no meaning for other steps
     *            than the first one.
     */
    public void setDefaultModelState(ModelState modelState)
    {
        ModelState oldState = this.defaultModelState;
        this.defaultModelState = modelState;
        if (oldState != modelState)
            invalidateModelState();
    }

    /**
     * Causes this step (and all following steps) to recompute its modelState from the previous step.
     */
    public void invalidateModelState()
    {
        invalidateThisModelState();

        if (this.next != null)
            this.next.invalidateModelState();
    }

    /**
     * Invalidate only this step's model state. You should call this only if you are sure that this model state's change
     * won't affect any further steps (eg. if you change the image in the step).
     */
    protected void invalidateThisModelState()
    {
        this.modelState = null;
    }

    /**
     * @return True if the model state is still valid.
     */
    public boolean isModelStateValid()
    {
        return modelState != null;
    }

    @Override
    public void setColspan(Integer value)
    {
        if (!value.equals(1))
            super.setColspan(value);
        else
            super.setColspan(null);
    }

    @Override
    public void setRowspan(Integer value)
    {
        if (!value.equals(1))
            super.setRowspan(value);
        else
            super.setRowspan(null);
    }

    /**
     * @return True if this step can be edited.
     */
    public boolean isEditable()
    {
        return next == null;
    }

    @Override
    protected String[] getNonChildProperties()
    {
        return new String[] { PREVIOUS_PROPERTY, NEXT_PROPERTY };
    }
}
