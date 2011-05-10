/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.math;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * A unit of angles.
 * 
 * @author Martin Pecka
 */
public enum AngleUnit
{
    /** Degrees. */
    DEGREE
    {
        @Override
        public double convertTo(double value, AngleUnit targetUnit)
        {
            if (targetUnit == this)
                return value;
            if (targetUnit == RAD)
                return value * degToRad;
            return RAD.convertTo(convertTo(value, RAD), targetUnit);
        }

        @Override
        public String formatValue(double value)
        {
            format.applyPattern(messages.getString("deg.unitString"));
            return format.format(new Object[] { value });
        }

        @Override
        public String getUnit()
        {
            return messages.getString("deg.unit");
        }

        @Override
        public String toString()
        {
            return messages.getString("deg.name");
        }
    },
    /** Radians. They are used as the conversion inter-unit. */
    RAD
    {
        @Override
        public double convertTo(double value, AngleUnit targetUnit)
        {
            if (targetUnit == this)
                return value;

            switch (targetUnit) {
                case DEGREE:
                    return value * radToDeg;
                case GRAD:
                    return value * radToGrad;
                default:
                    return value;
            }
        }

        @Override
        public String formatValue(double value)
        {
            double timesPi = value / Math.PI;
            format.applyPattern(messages.getString("rad.unitString"));

            return format.format(new Object[] { timesPi + "π" });
        }

        @Override
        public String getUnit()
        {
            return messages.getString("rad.unit");
        }

        @Override
        public String toString()
        {
            return messages.getString("rad.name");
        }

        @Override
        public Double parseValue(String text) throws NumberFormatException
        {
            String txt = text.toLowerCase().replaceAll("π", "pi");
            if (!txt.contains("pi"))
                return super.parseValue(txt);

            // allow strings like "2*PI", "pi*2", "PI/2", "2pi"
            if (txt.matches("pi\\s*/"))
                return Math.PI / Double.parseDouble(txt.replaceAll("\\s*pi\\s*/\\s*", ""));
            return Double.parseDouble(txt.replaceAll("\\s*\\*?\\s*pi\\s*\\*?\\s*", "")) * Math.PI;
        }

        @Override
        public String getNiceValue(double value)
        {
            double timesPi = value / Math.PI;
            return timesPi + "π";
        }
    },
    /** Grads. */
    GRAD
    {
        @Override
        public double convertTo(double value, AngleUnit targetUnit)
        {
            if (targetUnit == this)
                return value;
            if (targetUnit == RAD)
                return value * gradToRad;
            return RAD.convertTo(convertTo(value, RAD), targetUnit);
        }

        @Override
        public String formatValue(double value)
        {
            format.applyPattern(messages.getString("grad.unitString"));
            return format.format(new Object[] { value });
        }

        @Override
        public String getUnit()
        {
            return messages.getString("grad.unit");
        }

        @Override
        public String toString()
        {
            return messages.getString("grad.name");
        }
    };

    protected static final double degToRad  = Math.PI / 180d;
    protected static final double radToDeg  = 1d / degToRad;
    protected static final double gradToRad = Math.PI / 200d;
    protected static final double radToGrad = 1d / gradToRad;

    /** The resource bundle this class can use. */
    protected ResourceBundle      messages;

    /** The format object to format strings with. */
    protected MessageFormat       format;

    {
        ServiceLocator
                .get(ConfigurationManager.class)
                .get()
                .addAndRunResourceBundleListener(
                        new Configuration.ResourceBundleLocaleListener(AngleUnit.this.getDeclaringClass().getName()) {
                            @Override
                            public void bundleChanged()
                            {
                                messages = this.bundle;
                                format = new MessageFormat("", getProperty());
                            }
                        });
    }

    /**
     * Convert the given angle in <code>this</code> units to the target unit.
     * 
     * @param value The angle to convert.
     * @param targetUnit The unit to convert to.
     * @return The converted angle.
     */
    public abstract double convertTo(double value, AngleUnit targetUnit);

    /**
     * @return The full name of this unit.
     */
    @Override
    public abstract String toString();

    /**
     * @return The string representing this unit's mark.
     */
    public abstract String getUnit();

    /**
     * Return the string representing the given value in <code>this</code> unit.
     * 
     * @param value The angle to format.
     * @return The string representing the given angle.
     */
    public abstract String formatValue(double value);

    /**
     * Return the string representing the given value in <code>this</code> unit.
     * 
     * @param value The angle to format.
     * @param unit The unit of the given angle.
     * @return The string representing the given angle.
     */
    public String formatValue(double value, AngleUnit unit)
    {
        return formatValue(unit.convertTo(value, this));
    }

    /**
     * Parse an angle from the given string.
     * 
     * @param text The text to be parsed.
     * @return The parsed value.
     * 
     * @throws NumberFormatException If the string cannot be parsed as a value of this unit.
     */
    public Double parseValue(String text) throws NumberFormatException
    {
        return Double.parseDouble(text);
    }

    /**
     * Return a user-friendly string representation for the given value. The returned string has to be parseable by
     * {@link #parseValue(String)}.
     * 
     * @param value The value to get nice form of.
     * @return The nice form of the given value.
     */
    public String getNiceValue(double value)
    {
        return Double.toString(value);
    }
}
