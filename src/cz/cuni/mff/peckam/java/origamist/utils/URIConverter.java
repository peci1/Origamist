/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides conversion between Java URI and XML anyURI types
 * 
 * @author Martin Pecka
 */
public class URIConverter
{
    /**
     * Parse Java URI from XML anyURI
     * 
     * @param s
     * @return URI
     */
    public static URI parse(String s)
    {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Parse XML anyURI from Java URI
     * 
     * @param u
     * @return the string representation of the URI
     */
    public static String print(URI u)
    {
        return u.toString();
    }
}
