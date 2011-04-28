/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A dimension with units specified.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public class UnitDimension extends cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension
{
    /** The localization resource bundle. */
    private static ResourceBundle messages;

    static {
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
     * Convert this dimension to a dimension with the given unit.
     * 
     * If any of the units is REL, uses the reference dimension associated with this dimension. Trying to convert
     * to/from relative units without the reference dimension set produces an {@link IllegalStateException}.
     * 
     * @param newUnit The unit of the requested dimension.
     * @return A corresponding dimension with the given unit.
     * 
     * @throws IllegalStateException If trying to convert to/from relative units without the reference dimension set.
     */
    public UnitDimension convertTo(Unit newUnit) throws IllegalStateException
    {
        UnitDimension res = new UnitDimension();
        res.setReference(referenceUnit, referenceLength);
        res.width = UnitHelper.convertTo(getUnit(), newUnit, width, referenceUnit, referenceLength);
        res.height = UnitHelper.convertTo(getUnit(), newUnit, height, referenceUnit, referenceLength);
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

    @Override
    public String toString()
    {
        String format = messages.getString("unitDimension.format");
        try {
            format = messages.getString("unitDimension.format." + getUnit().toString());
        } catch (MissingResourceException e) {}

        return MessageFormat.format(format, UnitHelper.formatUnit(getUnit(), getWidth()),
                UnitHelper.formatUnit(getUnit(), getHeight()));
    }

    /**
     * Return the text representation of the dimension.
     * 
     * @param respectPreferredUnit If true, change the dimension's unit to the unit set in configuration.
     * @return The text representation of the dimension.
     */
    public String toString(boolean respectPreferredUnit)
    {
        if (!respectPreferredUnit)
            return toString();

        Unit prefUnit = ServiceLocator.get(ConfigurationManager.class).get().getPreferredUnit();
        UnitDimension dim = this;
        if (prefUnit != null && !dim.unit.equals(prefUnit)) {
            try {
                dim = this.convertTo(prefUnit);
            } catch (IllegalStateException e) {
                Logger.getLogger("application").warn(e.getMessage(), e);
                return this.toString();
            }
        }
        return dim.toString();
    }

    /**
     * Set the reference dimension this unit uses when converting from relative units to absolute and vice versa.
     * 
     * @param unit The unit of the reference dimension.
     * @param length The length of the reference dimension.
     */
    public void setReference(Unit unit, Double length)
    {
        setReferenceUnit(unit);
        setReferenceLength(length);
    }
}
