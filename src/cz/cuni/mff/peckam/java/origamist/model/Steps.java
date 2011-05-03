/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
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
    public Steps()
    {
        // this observer handles the next/previous properties of Steps added to this list to reflect the steps'
        // positions in the list
        ((ObservableList<Step>) getStep()).addObserver(new Observer<Step>() {
            @Override
            public void changePerformed(ChangeNotification<? extends Step> change)
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
                }
            }
        });
    }

    /**
     * Invalidate all steps contained in this list.
     */
    public void invalidateSteps()
    {
        if (step.size() > 0)
            step.get(0).invalidateModelState();
    }
}
