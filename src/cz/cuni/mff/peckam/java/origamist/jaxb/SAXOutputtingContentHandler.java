/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.jaxb;

import javax.xml.transform.sax.SAXResult;

import org.xml.sax.ContentHandler;

/**
 * A content handler that has a result object used for some output.
 * 
 * @author Martin Pecka
 */
public interface SAXOutputtingContentHandler extends ContentHandler
{
    /**
     * Set the {@link Result} associated with this handler to be used for the transformation.
     * 
     * @param result A {@link Result} instance, should not be <code>null</code>.
     * 
     * @throws IllegalArgumentException If <code>result</code> is invalid for some reason.
     */
    public void setResult(SAXResult result) throws IllegalArgumentException;
}
