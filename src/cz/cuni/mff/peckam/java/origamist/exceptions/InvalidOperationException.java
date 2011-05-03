/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.exceptions;

import cz.cuni.mff.peckam.java.origamist.model.Operation;

/**
 * This exception is thrown if a model folding operation cannot be done (eg. the fold is invalidly specified, the paper
 * would tear or something similar).
 * 
 * @author Martin Pecka
 */
public class InvalidOperationException extends RuntimeException
{

    /** */
    private static final long serialVersionUID = -7848672676808780317L;

    /** The operation that has failed. */
    protected Operation       operation        = null;

    /**
     * @see RuntimeException#RuntimeException()
     */
    public InvalidOperationException()
    {
        super();
    }

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public InvalidOperationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public InvalidOperationException(String message)
    {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public InvalidOperationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param operation The operation that has failed.
     * 
     * @see RuntimeException#RuntimeException()
     */
    public InvalidOperationException(Operation operation)
    {
        super();
        this.operation = operation;
    }

    /**
     * @param operation The operation that has failed.
     * 
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public InvalidOperationException(String message, Throwable cause, Operation operation)
    {
        super(message, cause);
        this.operation = operation;
    }

    /**
     * @param operation The operation that has failed.
     * 
     * @see RuntimeException#RuntimeException(String)
     */
    public InvalidOperationException(String message, Operation operation)
    {
        super(message);
        this.operation = operation;
    }

    /**
     * @param operation The operation that has failed.
     * 
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public InvalidOperationException(Throwable cause, Operation operation)
    {
        super(cause);
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
}
