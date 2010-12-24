/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;
import cz.cuni.mff.peckam.java.origamist.utils.ParametrizedLocalizedString;

/**
 * This class provides additional functionality of the Unit enum.
 * 
 * In localization patterns you are given a number indicating which unit is to be used. The given number is equal the
 * unit's ordinal() value.
 * 
 * @author Martin Pecka
 */
public class UnitHelper
{
    /**
     * This associative array holds lengths of all units in meters
     */
    protected static Hashtable<Unit, Double> inMeters = new Hashtable<Unit, Double>();

    /** The resource bundle for getting the unit names. */
    private static ResourceBundle            messages;

    static {
        inMeters.put(Unit.MM, 0.001d);
        inMeters.put(Unit.CM, 0.01d);
        inMeters.put(Unit.M, 1.0d);
        inMeters.put(Unit.KM, 1000.0d);
        inMeters.put(Unit.REL, Double.NaN);
        inMeters.put(Unit.INCH, 0.0254d);
        inMeters.put(Unit.FOOT, 0.3048d);
        inMeters.put(Unit.MILE, 1609.344d);

        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                messages = ResourceBundle.getBundle("application", (Locale) evt.getNewValue());
            }
        };
        ServiceLocator.get(ConfigurationManager.class).get().addPropertyChangeListener("locale", l);
        l.propertyChange(new PropertyChangeEvent(new Object(), "locale", null, ServiceLocator
                .get(ConfigurationManager.class).get().getLocale()));
    }

    /**
     * Convert the value given in unit from to a value in unit to.
     * 
     * If any of the units is REL, return <code>Double.NaN</code>. If you need to convert a relative value, use the
     * overload with a reference Unit and dimension.
     * 
     * @param from The unit of the given value.
     * @param to The unit we want result to be in.
     * @param value The value in the from unit.
     * @return The corresponding value in the to unit.
     */
    public static Double convertTo(Unit from, Unit to, double value)
    {
        if (Double.isNaN(inMeters.get(from)) || Double.isNaN(inMeters.get(to)))
            return Double.NaN;
        return value * inMeters.get(from) / inMeters.get(to);
    }

    /**
     * Convert the value given in unit from to a value in unit to.
     * 
     * If any of the units is REL, use the given reference dimension.
     * 
     * @param from The unit of the given value.
     * @param to The unit we want result to be in.
     * @param value The value in the from unit.
     * @param referenceUnit The unit the reference dimension has.
     * @param referenceLength The length of the reference unit.
     * @return The corresponding value in the to unit.
     * 
     * @throws IllegalStateException If trying to convert to/from relative units without the reference dimension set.
     */
    public static Double convertTo(Unit from, Unit to, double value, Unit referenceUnit, Double referenceLength)
            throws IllegalStateException
    {
        Double res = convertTo(from, to, value);
        if (!Double.isNaN(res))
            return res;

        try {
            if (from == Unit.REL && referenceUnit != null && referenceLength != null) {
                return convertTo(referenceUnit, to, value * referenceLength);
            } else if (to == Unit.REL) {
                return convertTo(from, referenceUnit, value) / referenceLength;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new IllegalStateException(
                    "Trying to convert to/from a relative unit, but the reference dimension is <code>null</code>.");
        }

    }

    /**
     * Return the human-friendly name of the unit (eg. <code>mile</code> or <code>meter</code>).
     * 
     * @param unit The unit to get the name of.
     * @return The human-friendly name of the unit (eg. <code>mile</code> or <code>meter</code>).
     */
    public static String getUnitName(Unit unit)
    {
        return messages.getString("units." + unit.toString());
    }

    /**
     * Return the human-friendly name of the unit (eg. <code>mile</code> or <code>meter</code>).
     * 
     * @param unit The unit to get the name of.
     * @param asLocalizedString Only signature differentiating parameter, has no other meaning.
     * @return The human-friendly name of the unit (eg. <code>mile</code> or <code>meter</code>).
     */
    public static LocalizedString getUnitName(Unit unit, boolean asLocalizedString)
    {
        return new LocalizedString("application", "units." + unit.toString());
    }

    /**
     * Return the mark (abbreviation) of the unit (eg. <code>mm</code> or <code>″</code>).
     * 
     * @param unit The unit to get the mark of.
     * @return The mark (abbreviation) of the unit (eg. <code>mm</code> or <code>″</code>).
     */
    public static String getUnitMark(Unit unit)
    {
        return messages.getString("units." + unit.toString() + ".mark");
    }

    /**
     * Return the mark (abbreviation) of the unit (eg. <code>mm</code> or <code>″</code>).
     * 
     * @param unit The unit to get the mark of.
     * @param asLocalizedString Only signature differentiating parameter, has no other meaning.
     * @return The mark (abbreviation) of the unit (eg. <code>mm</code> or <code>″</code>).
     */
    public static LocalizedString getUnitMark(Unit unit, boolean asLocalizedString)
    {
        return new LocalizedString("application", "units." + unit.toString() + ".mark");
    }

    /**
     * Return the description of the unit.
     * 
     * The description would usually contain the name and the mark of the unit.
     * 
     * @param unit The unit to get the description of.
     * @return The description of the unit.
     */
    public static String getUnitDescription(Unit unit)
    {
        return String.format(messages.getString("units.description"), getUnitName(unit), getUnitMark(unit));
    }

    /**
     * Return the description of the unit.
     * 
     * The description would usually contain the name and the mark of the unit.
     * 
     * @param unit The unit to get the description of.
     * @param asLocalizedString Only signature differentiating parameter, has no other meaning.
     * @return The description of the unit.
     */
    public static LocalizedString getUnitDescription(Unit unit, boolean asLocalizedString)
    {
        return new ParametrizedLocalizedString("application", "units.description", getUnitName(unit, true),
                getUnitMark(unit, true));
    }

    /**
     * Return the formatted string that contains the given value and the unit's mark.
     * 
     * @param unit The unit to use.
     * @param value The value to display.
     * @return The formatted string that contains the given value and the unit's mark.
     */
    public static String formatUnit(Unit unit, double value)
    {
        return MessageFormat.format(messages.getString("units." + unit.toString() + ".format"), new Double(value));
    }

    /**
     * Return the formatted string that contains the given value and the unit's mark.
     * 
     * @param unit The unit to use.
     * @param value The value to display.
     * @param asLocalizedString Only signature differentiating parameter, has no other meaning.
     * @return The formatted string that contains the given value and the unit's mark.
     */
    public static LocalizedString formatUnit(Unit unit, double value, boolean asLocalizedString)
    {
        return new ParametrizedLocalizedString("application", "units." + unit.toString() + ".format", new Double(value));
    }
}
