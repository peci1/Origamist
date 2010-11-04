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
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.JAXBOrigamiHandler;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class JAXBOrigamiHandlerTest
{

    /**
     * Test method for
     * {@link cz.cuni.mff.peckam.java.origamist.services.JAXBOrigamiHandler#save(cz.cuni.mff.peckam.java.origamist.model.Origami, java.io.File)}
     * and {@link cz.cuni.mff.peckam.java.origamist.services.JAXBOrigamiHandler#loadModel(java.net.URI, boolean)}.
     */
    @Test
    public void testLoadAndSave()
    {
        try {
            File docBase = new File("tests");
            JAXBOrigamiHandler h = new JAXBOrigamiHandler(docBase.toURI().toURL());

            assertEquals("", docBase.toURI().toURL(), h.getDocumentBase());

            Origami o = h.loadModel(new URI("diagram1.xml"), false);

            assertNotNull(o);

            File f = new File(docBase, "diagramTestLoadAndSave.xml");
            h.save(o, f);

            File f2 = new File(docBase, "diagramTestLoadAndSave2.xml");
            Origami o2 = h.loadModel(f.toURI(), false);
            h.save(o2, f2);

            o2.setSrc(o.getSrc()); // we know the files have different sources
            assertEquals(o, o2);
            InputStream fis = f.toURI().toURL().openStream();
            InputStream f2is = f2.toURI().toURL().openStream();

            int i;
            while ((i = fis.read()) != -1) {
                assertEquals(i, f2is.read());
            }

            f.delete();
            f2.delete();

        } catch (MalformedURLException e) {
            fail(e.toString());
        } catch (UnsupportedDataFormatException e) {
            fail(e.toString());
        } catch (IOException e) {
            fail(e.toString());
        } catch (URISyntaxException e) {
            fail(e.toString());
        } catch (MarshalException e) {
            fail(e.toString());
        } catch (JAXBException e) {
            fail(e.toString());
        }
    }
}
