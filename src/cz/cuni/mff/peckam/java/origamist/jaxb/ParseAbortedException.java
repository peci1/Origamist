package cz.cuni.mff.peckam.java.origamist.jaxb;

import org.xml.sax.SAXException;

/**
 * This exception will tell that the parser didn't end with error, but it was just forced to stop reading of the
 * data.
 * 
 * @author Martin Pecka
 */
public class ParseAbortedException extends SAXException
{
    /** */
    private static final long serialVersionUID = -1560106113502958618L;
}