/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

/**
 * Direction of a fold
 * 
 * @author Martin Pecka
 */
public enum Direction
{

    /**
     * A valley fold direction
     */
    VALLEY
    {
        @Override
        public Direction getOpposite()
        {
            return MOUNTAIN;
        }
    },

    /**
     * A mountain fold direction
     */
    MOUNTAIN
    {
        @Override
        public Direction getOpposite()
        {
            return VALLEY;
        }
    };

    /**
     * @return The opposite fold direction.
     */
    public abstract Direction getOpposite();
}
