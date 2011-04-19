/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.Hashtable;
import java.util.Locale;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.LangString;
import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.utils.LangStringHashtableObserver;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;

/**
 * A step of the model creation.
 * 
 * A step is a group of operations displayed at once.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class Step extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Step
{
    /**
     * The hastable for more comfortable search in localized descriptions.
     */
    protected Hashtable<Locale, String> descriptions      = new Hashtable<Locale, String>();

    /**
     * The cached model state after performing this step.
     */
    protected ModelState                modelState        = null;

    /**
     * If this is the first step, use this model state as the previous one.
     */
    protected ModelState                defaultModelState = null;

    /**
     * The step preceeding this one. If this is the first one, previous is null.
     */
    protected Step                      previous          = null;

    /**
     * The step succeeding this one. If this is the last one, next is null.
     */
    protected Step                      next              = null;

    /**
     * Create a new step.
     */
    public Step()
    {
        ((ObservableList<LangString>) getDescription()).addObserver(new LangStringHashtableObserver(descriptions));
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
    public ModelState getModelState() throws InvalidOperationException
    {
        if (this.modelState != null)
            return this.modelState;

        if (previous != null)
            this.modelState = previous.getModelState().clone();
        else
            this.modelState = this.defaultModelState.clone();

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
        this.previous = previous;
    }

    /**
     * @param next the next step
     */
    public void setNext(Step next)
    {
        this.next = next;
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
        this.defaultModelState = modelState;
    }

    /**
     * Causes this step (and all following steps) to recompute its modelState from the previous step.
     */
    public void invalidateModelState()
    {
        this.modelState = null;
        if (this.next != null)
            this.next.invalidateModelState();
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
}
