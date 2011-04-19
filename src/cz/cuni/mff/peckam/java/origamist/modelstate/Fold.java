/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;

/**
 * Represents a fold on the paper.
 * 
 * @author Martin Pecka
 */
public class Fold implements Cloneable
{
    /**
     * The lines this fold consists of.
     */
    protected ObservableList<FoldLine> lines             = new ObservableList<FoldLine>();

    /**
     * Id of the step this fold originated in.
     */
    protected Integer                  originatingStepId = null;

    /**
     * 
     */
    public Fold()
    {
        addObservers();
    }

    /**
     * @param originatingStepId Id of the step this fold originated in.
     */
    public Fold(Integer originatingStepId)
    {
        this();
        this.originatingStepId = originatingStepId;
    }

    /**
     * Add all needed observers to this object's fields.
     */
    protected void addObservers()
    {
        lines.addObserver(getLinesObserver());
    }

    /**
     * @return The lines this fold consists of.
     */
    public ObservableList<FoldLine> getLines()
    {
        return lines;
    }

    /**
     * @return Id of the step this fold originated in.
     */
    public Integer getOriginatingStepId()
    {
        return originatingStepId;
    }

    /** The observer of the list of lines that automatically assigns this fold as the parent for all new lines. */
    private transient Observer<FoldLine> linesObserver = null;

    /**
     * @return The observer of the list of lines that automatically assigns this fold as the parent for all new lines.
     */
    private Observer<FoldLine> getLinesObserver()
    {
        if (linesObserver == null) {
            linesObserver = new Observer<FoldLine>() {
                @Override
                public void changePerformed(ChangeNotification<FoldLine> change)
                {
                    if (change.getChangeType() != ChangeTypes.ADD) {
                        change.getOldItem().setFold(null);
                    }
                    if (change.getChangeType() != ChangeTypes.REMOVE) {
                        change.getItem().setFold(Fold.this);
                    }
                }
            };
        }
        return linesObserver;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
        result = prime * result + ((originatingStepId == null) ? 0 : originatingStepId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Fold other = (Fold) obj;

        if (originatingStepId == null) {
            if (other.originatingStepId != null)
                return false;
        } else if (!originatingStepId.equals(other.originatingStepId))
            return false;

        if (lines == null) {
            if (other.lines != null)
                return false;
        } else if (!lines.equals(other.lines))
            return false;

        return true;
    }

    @Override
    public String toString()
    {
        return "Fold [originatingStepId=" + originatingStepId + ", lines=" + lines + "]";
    }

    /**
     * <p>
     * <b>Cloning this class will not trigger any external observers on the lines field to be triggered, although the
     * contents of that list are themselves cloned. The observers are, however, copied along with the list (not cloned).
     * Note that the referenced ModelTriangles in any FoldLines aren't cloned.</b>
     * </p>
     * 
     * {@inheritDoc}
     */
    @Override
    public Fold clone()
    {
        try {
            Fold result = (Fold) super.clone();

            result.lines = new ObservableList<FoldLine>();

            // create a new linesObserver and add it to the list of lines
            result.linesObserver = null;
            result.lines.addObserver(result.getLinesObserver());

            for (FoldLine l : this.lines) {
                // calling add() will trigger the linesObserver which sets the cloned Fold as the parent of the fold
                // lines
                result.lines.add(l.clone());
            }

            // add the rest of observers to avoid calling them when cloning the lines list
            for (Observer<FoldLine> o : lines.getObservers()) {
                if (o != linesObserver)
                    result.lines.addObserver(o);
            }

            return result;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
