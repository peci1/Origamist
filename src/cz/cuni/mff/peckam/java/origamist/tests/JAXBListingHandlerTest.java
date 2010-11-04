/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.services.JAXBListingHandler;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class JAXBListingHandlerTest
{

    /**
     * Test method for
     * {@link cz.cuni.mff.peckam.java.origamist.services.JAXBListingHandler#export(cz.cuni.mff.peckam.java.origamist.files.Listing, java.io.File)}
     * and {@link cz.cuni.mff.peckam.java.origamist.services.JAXBListingHandler#loadListing(java.net.URL)}.
     */
    @Test
    public void testLoadAndExport()
    {
        try {
            File docBase = new File("tests/listing1");
            JAXBListingHandler h = new JAXBListingHandler();

            Listing l = h.load(new URL(docBase.toURI().toURL(), "listing.xml"));

            assertNotNull(l);

            File f = new File(docBase, "listingTest1.xml");
            h.export(l, f);

            File f2 = new File(docBase, "listingTest2.xml");
            Listing l2 = h.load(f.toURI().toURL());
            h.export(l2, f2);

            assertEquals(l, l2);
            InputStream fis = f.toURI().toURL().openStream();
            InputStream f2is = f2.toURI().toURL().openStream();

            int i;
            while ((i = fis.read()) != -1) {
                assertEquals(i, f2is.read());
            }

            fis.close();
            f2is.close();

            f.delete();
            f2.delete();

        } catch (MalformedURLException e) {
            fail(e.toString());
        } catch (UnsupportedDataFormatException e) {
            fail(e.toString());
        } catch (IOException e) {
            fail(e.toString());
        } catch (MarshalException e) {
            fail(e.toString());
        } catch (JAXBException e) {
            fail(e.toString());
        }
    }
}
