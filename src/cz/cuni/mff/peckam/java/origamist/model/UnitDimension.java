/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;

/**
 * A dimension with units specified.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class UnitDimension extends cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension
{
    /**
     * Convert this dimension to a dimension with the given unit
     * 
     * @param newUnit The unit of the requested dimension
     * @return A corresponding dimension with the given unit
     */
    public UnitDimension convertTo(Unit newUnit)
    {
        UnitDimension res = new UnitDimension();
        res.width = UnitHelper.convertTo(getUnit(), newUnit, width);
        res.height = UnitHelper.convertTo(getUnit(), newUnit, height);
        res.setUnit(newUnit);
        return res;
    }

    /**
     * Return the bigger dimension.
     * 
     * @return The bigger dimension.
     */
    public double getMax()
    {
        return Math.max(width, height);
    }
}
