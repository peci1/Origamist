/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;

/**
 * A {@link UnitDimension} with a textual label.
 * 
 * @author Martin Pecka
 */
public class UnitDimensionWithLabel
{
    protected UnitDimension dimension;
    protected String        label;

    /**
     * @param dimension
     * @param label
     */
    public UnitDimensionWithLabel(UnitDimension dimension, String label)
    {
        this.dimension = dimension;
        this.label = label;
    }

    /**
     * @return the dimension
     */
    public UnitDimension getDimension()
    {
        return dimension;
    }

    /**
     * @param dimension the dimension to set
     */
    public void setDimension(UnitDimension dimension)
    {
        this.dimension = dimension;
    }

    /**
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
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
        UnitDimensionWithLabel other = (UnitDimensionWithLabel) obj;
        if (dimension == null) {
            if (other.dimension != null)
                return false;
        } else if (!dimension.equals(other.dimension))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }
}