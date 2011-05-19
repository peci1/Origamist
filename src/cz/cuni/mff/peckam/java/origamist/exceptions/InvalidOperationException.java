/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.exceptions;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.model.Operation;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * This exception is thrown if a model folding operation cannot be done (eg. the fold is invalidly specified, the paper
 * would tear or something similar).
 * 
 * @author Martin Pecka
 */
public class InvalidOperationException extends RuntimeException
{

    /** */
    private static final long       serialVersionUID   = -7848672676808780317L;

    /** Name of the resource bundle this class uses for issuing error messages. */
    public static final String      BUNDLE_NAME        = InvalidOperationException.class.getName();

    protected static final String   DEFAULT_BUNDLE_KEY = "unknown";

    /** The resource bundle this class can use. */
    protected static ResourceBundle messages;

    static {
        ServiceLocator.get(ConfigurationManager.class).get()
                .addAndRunResourceBundleListener(new Configuration.ResourceBundleLocaleListener(BUNDLE_NAME) {
                    @Override
                    public void bundleChanged()
                    {
                        messages = bundle;
                    }
                });
    }

    /** The operation that has failed. */
    protected Operation             operation          = null;

    /** The key in the resource bundle that describes this exception. */
    protected final String          bundleKey;

    /** Arguments for this exception that are passed to the resource bundle. */
    protected final Object[]        arguments;

    /**
     * 
     */
    public InvalidOperationException()
    {
        super();
        this.bundleKey = DEFAULT_BUNDLE_KEY;
        this.arguments = null;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param cause The cause of the exception.
     * @param arguments Arguments for this exception that are passed to the resource bundle.
     */
    public InvalidOperationException(String bundleKey, Throwable cause, Object... arguments)
    {
        super(messages.containsKey(bundleKey) ? MessageFormat.format(messages.getString(bundleKey), arguments)
                : bundleKey, cause);
        this.bundleKey = DEFAULT_BUNDLE_KEY;
        this.arguments = arguments;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param cause The cause of the exception.
     */
    public InvalidOperationException(String bundleKey, Throwable cause)
    {
        super(messages.containsKey(bundleKey) ? messages.getString(bundleKey) : bundleKey, cause);
        this.bundleKey = bundleKey;
        this.arguments = null;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param arguments Arguments for this exception that are passed to the resource bundle.
     */
    public InvalidOperationException(String bundleKey, Object... arguments)
    {
        super(messages.containsKey(bundleKey) ? MessageFormat.format(messages.getString(bundleKey), arguments)
                : bundleKey);
        this.bundleKey = bundleKey;
        this.arguments = arguments;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     */
    public InvalidOperationException(String bundleKey)
    {
        super(messages.containsKey(bundleKey) ? messages.getString(bundleKey) : bundleKey);
        this.bundleKey = bundleKey;
        this.arguments = null;
    }

    /**
     * @param cause The cause of the exception.
     */
    public InvalidOperationException(Throwable cause)
    {
        super(cause);
        this.bundleKey = DEFAULT_BUNDLE_KEY;
        this.arguments = null;
    }

    /**
     * @param operation The operation that has failed.
     */
    public InvalidOperationException(Operation operation)
    {
        this();
        this.operation = operation;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param cause The cause of the exception.
     * @param operation The operation that has failed.
     * @param arguments Arguments for this exception that are passed to the resource bundle.
     */
    public InvalidOperationException(String bundleKey, Throwable cause, Operation operation, Object... arguments)
    {
        this(bundleKey, cause, arguments);
        this.operation = operation;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param operation The operation that has failed.
     * @param cause The cause of the exception.
     */
    public InvalidOperationException(String bundleKey, Throwable cause, Operation operation)
    {
        this(bundleKey, cause);
        this.operation = operation;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param operation The operation that has failed.
     * @param arguments Arguments for this exception that are passed to the resource bundle.
     */
    public InvalidOperationException(String bundleKey, Operation operation, Object... arguments)
    {
        this(bundleKey, arguments);
        this.operation = operation;
    }

    /**
     * @param bundleKey Key in InvalidOperationException.properties that describes this exception.
     * @param operation The operation that has failed.
     */
    public InvalidOperationException(String bundleKey, Operation operation)
    {
        this(bundleKey);
        this.operation = operation;
    }

    /**
     * @param operation The operation that has failed.
     * @param cause The cause of the exception.
     */
    public InvalidOperationException(Throwable cause, Operation operation)
    {
        this(cause);
        this.operation = operation;
    }

    /**
     * @return The operation that has failed.
     */
    public Operation getOperation()
    {
        return operation;
    }

    /**
     * @param operation The operation that has failed.
     */
    public void setOperation(Operation operation)
    {
        this.operation = operation;
    }

    /**
     * @return A user friendly message.
     */
    public String getUserFriendlyMessage()
    {
        if (messages.containsKey(bundleKey + ".user.friendly")) {
            if (arguments == null)
                return messages.getString(bundleKey + ".user.friendly");
            else
                return MessageFormat.format(messages.getString(bundleKey + ".user.friendly"), arguments);
        } else if (messages.containsKey(bundleKey)) {
            if (arguments == null)
                return messages.getString(bundleKey);
            else
                return MessageFormat.format(messages.getString(bundleKey), arguments);
        } else {
            if (arguments == null)
                return bundleKey;
            else
                return bundleKey + ", " + Arrays.toString(arguments);
        }
    }
}
