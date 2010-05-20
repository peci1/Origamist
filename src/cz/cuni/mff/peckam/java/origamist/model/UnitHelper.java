/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.Hashtable;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;

/**
 * This class provides additional functionality of the Unit enum.
 * 
 * In localization patterns you are given a number indicating which unit is to
 * be used. The given number is equal the unit's ordinal() value.
 * 
 * @author Martin Pecka
 */
public class UnitHelper
{
    /**
     * This associative array holds lengths of all units in meters
     */
    protected static Hashtable<Unit, Double> inMeters = new Hashtable<Unit, Double>();

    static {
        inMeters.put(Unit.MM, 0.001);
        inMeters.put(Unit.CM, 0.01);
        inMeters.put(Unit.M, 1.0);
        inMeters.put(Unit.KM, 1000.0);
        inMeters.put(Unit.REL, null);
        inMeters.put(Unit.INCH, 0.0254);
        inMeters.put(Unit.FOOT, 0.3048);
        inMeters.put(Unit.MILE, 1609.344);
    }

    /**
     * Convert the value given in unit from to a value in unit to.
     * 
     * If any of the units is REL, return null. If you need to convert a
     * relative value, use the overload with a reference Unit and dimension
     * 
     * @param from The unit of the given value
     * @param to The unit we want result to be in
     * @param value The value in the from unit
     * @return The corresponding value in the to unit
     */
    public static Double convertTo(Unit from, Unit to, double value)
    {
        if (inMeters.get(from) == null || inMeters.get(to) == null)
            return null;
        return value * inMeters.get(from) / inMeters.get(to);
    }

    /**
     * Convert the value given in unit from to a value in unit to.
     * 
     * If any of the units is REL, use the given reference dimension.
     * 
     * @param from The unit of the given value
     * @param to The unit we want result to be in
     * @param value The value in the from unit
     * @param referenceUnit The unit the reference dimension has
     * @param referenceLength The length of the reference unit
     * @return The corresponding value in the to unit
     */
    public static Double convertTo(Unit from, Unit to, double value,
            Unit referenceUnit, double referenceLength)
    {
        Double res = convertTo(from, to, value);
        if (res != null)
            return res;

        if (from == Unit.REL) {
            return convertTo(referenceUnit, to, value * referenceLength);
        } else if (to == Unit.REL) {
            return convertTo(from, referenceUnit, value) / referenceLength;
        } else {
            return null;
        }

    }
}
