/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.util.ArrayList;
import java.util.List;

/**
 * The internal state of the model after some steps.
 * 
 * @author Martin Pecka
 */
public class ModelState implements Cloneable
{
    protected List<Fold> folds = new ArrayList<Fold>();

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        ModelState result = (ModelState) super.clone();
        result.folds = new ArrayList<Fold>(folds);
        for (int i = 0; i < folds.size(); i++)
            folds.set(i, (Fold) folds.get(i).clone());

        return result;
    }
}
