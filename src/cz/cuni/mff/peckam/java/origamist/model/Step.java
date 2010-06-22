/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.Hashtable;
import java.util.Locale;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.common.jaxb.LangString;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotifyingList;
import cz.cuni.mff.peckam.java.origamist.utils.HashtableChangeNotificationListener;

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
        ((ChangeNotifyingList<LangString>) description).addChangeListener(new HashtableChangeNotificationListener(
                descriptions));
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
        LangString s = new cz.cuni.mff.peckam.java.origamist.common.jaxb.ObjectFactory().createLangString();
        s.setLang(l);
        s.setValue(desc);
        this.description.add(s);
    }

    /**
     * Perform folding from the previous step's state to a new state by this step.
     * 
     * @return The state the model would have after performing this step.
     */
    public ModelState getModelState()
    {
        if (this.modelState != null)
            return this.modelState;

        try {
            if (previous != null)
                this.modelState = (ModelState) previous.getModelState().clone();
            else
                this.modelState = (ModelState) this.defaultModelState.clone();
        } catch (CloneNotSupportedException e) {
            assert false : "ModelState does not support clone()";
            return null;
        }

        if (operation == null)
            return this.modelState;

        for (cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation o : operation)
            this.modelState = ((Operation) o).getModelState(this.modelState);

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
}
