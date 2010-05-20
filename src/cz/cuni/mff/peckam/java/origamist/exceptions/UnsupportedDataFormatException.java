/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.exceptions;

/**
 * This exception is thrown whenever the program tries to parse some data and
 * the data have invalid format.
 * 
 * @author Martin Pecka
 */
public class UnsupportedDataFormatException extends RuntimeException
{

    private static final long serialVersionUID = 8334384815885298171L;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UnsupportedDataFormatException()
    {
    }

    /**
     * Constructs a new exception with the specified detail message. The cause
     * is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     * 
     * @param message the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public UnsupportedDataFormatException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by
     *            the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public UnsupportedDataFormatException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>). This
     * constructor is useful for exceptions that are little more than wrappers
     * for other throwables.
     * 
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public UnsupportedDataFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
