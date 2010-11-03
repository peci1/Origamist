package cz.cuni.mff.peckam.java.origamist.utils;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class URIAdapter extends XmlAdapter<String, URI>
{

    /**
     * If not <code>null</code>, this URI is used to relativize the marshalled values and absolutize the unmarshalled
     * values.
     */
    protected URI relativeBase = null;

    public URIAdapter()
    {
        super();
    }

    public URI unmarshal(String value)
    {
        try {
            URI result = new URI(value);
            if (relativeBase != null && !result.isAbsolute() && !result.isOpaque()) {
                result = relativeBase.resolve(result);
            }
            return result;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public String marshal(URI value)
    {
        URI val = value;
        if (relativeBase != null) {
            val = relativeBase.relativize(val);
        }
        return val.toString();
    }

    /**
     * @return the relativeBase
     */
    public URI getRelativeBase()
    {
        return relativeBase;
    }

    /**
     * @param relativeBase the relativeBase to set
     */
    public void setRelativeBase(URI relativeBase)
    {
        this.relativeBase = relativeBase;
    }

}
