/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.services;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.origamist.JMultilineLabel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.jaxb.AdditionalTransforms.TransformLocation;
import cz.cuni.mff.peckam.java.origamist.jaxb.BindingsController;
import cz.cuni.mff.peckam.java.origamist.jaxb.BindingsManager;
import cz.cuni.mff.peckam.java.origamist.jaxb.ParseAbortedException;
import cz.cuni.mff.peckam.java.origamist.jaxb.ResultDelegatingDefaultHandler;
import cz.cuni.mff.peckam.java.origamist.jaxb.TransformInfo;
import cz.cuni.mff.peckam.java.origamist.model.Model;
import cz.cuni.mff.peckam.java.origamist.model.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.model.UnitDimension;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Unit;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;
import cz.cuni.mff.peckam.java.origamist.utils.ExportFormat;
import cz.cuni.mff.peckam.java.origamist.utils.ExportOptions;
import cz.cuni.mff.peckam.java.origamist.utils.PDFExportOptions;
import cz.cuni.mff.peckam.java.origamist.utils.PNGExportOptions;
import cz.cuni.mff.peckam.java.origamist.utils.SVGExportOptions;
import cz.cuni.mff.peckam.java.origamist.utils.URIAdapter;

/**
 * Loads an origami model from XML file using JAXB and vice versa.
 * 
 * The code is inspired by partial-unmarshalling example in JAXB section of JWSDP.
 * 
 * @author Martin Pecka
 */
public class JAXBOrigamiHandler extends Service implements OrigamiHandler
{

    /** The namespace of the newest schema. */
    public static final String LATEST_SCHEMA_NAMESPACE = "http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2";

    /** The model to return. */
    protected Origami          model                   = null;

    /** The base path for resolving relative URIs. */
    protected URL              documentBase            = null;

    public JAXBOrigamiHandler(URL documentBase)
    {
        this.documentBase = documentBase;
    }

    @Override
    public void save(Origami origami, File file) throws IOException, MarshalException, JAXBException
    {
        if (!file.exists())
            file.createNewFile();
        if (!file.isFile())
            throw new IOException("Cannot save the model in a directory or a non-file object: "
                    + file.getAbsolutePath() + ".");

        JAXBContext context = JAXBContext.newInstance("cz.cuni.mff.peckam.java.origamist.model.jaxb", getClass()
                .getClassLoader());
        Marshaller m = context.createMarshaller();

        // enable indenting and newline generation
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // make URLs in the listing relative to the location we save the model to
        m.setAdapter(new URIAdapter());
        m.getAdapter(URIAdapter.class).setRelativeBase(file.getParentFile().toURI());

        // do the Java class->XML conversion
        m.marshal(new ObjectFactory().createOrigami(origami), file);
    }

    @Override
    public Set<File> export(Origami origami, File file, ExportFormat format) throws IOException
    {
        return export(origami, file, format, null);
    }

    @Override
    public Set<File> export(Origami origami, File file, ExportFormat format, ExportOptions options) throws IOException
    {
        return export(origami, file, format, options, null);
    }

    @Override
    public Set<File> export(Origami origami, File file, ExportFormat format, ExportOptions options,
            Runnable progressCallback) throws IOException
    {
        Set<File> result = new HashSet<File>();
        if (format == ExportFormat.XML) {
            try {
                save(origami, file);
            } catch (MarshalException e) {
                throw new IOException(e);
            } catch (JAXBException e) {
                throw new IOException(e);
            }
            result.add(file);

            if (progressCallback != null)
                progressCallback.run();

        } else if (format == ExportFormat.PNG || format == ExportFormat.SVG || format == ExportFormat.PDF) {
            Double dpi = null;
            Locale locale = null;
            Insets pageInsets = null;
            boolean withBackground = true;

            if (options != null) {
                if (options instanceof PNGExportOptions) {
                    PNGExportOptions options2 = (PNGExportOptions) options;
                    dpi = options2.getDpi();
                    locale = options2.getLocale();
                    withBackground = options2.isWithBackground();
                    pageInsets = options2.getPageInsets();
                } else if (options instanceof SVGExportOptions) {
                    SVGExportOptions options2 = (SVGExportOptions) options;
                    dpi = options2.getDpi();
                    locale = options2.getLocale();
                    withBackground = options2.isWithBackground();
                    pageInsets = options2.getPageInsets();
                } else if (options instanceof PDFExportOptions) {
                    PDFExportOptions options2 = (PDFExportOptions) options;
                    dpi = options2.getDpi();
                    locale = options2.getLocale();
                    withBackground = options2.isWithBackground();
                    pageInsets = options2.getPageInsets();
                } else {
                    dpi = 72d;
                    locale = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
                    pageInsets = new Insets(25, 25, 25, 25);
                }
            } else {
                dpi = 72d;
                locale = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
                pageInsets = new Insets(25, 25, 25, 25);
            }

            final File parentFile = (file.isDirectory() ? file : file.getParentFile());

            final FileNameGenerator fileNames; // used only for PNG
            if (!file.isDirectory()) {
                fileNames = new FileNameGeneratorImpl(file.getName().replaceAll("\\.[^.]*$", ""), file.getName()
                        .replaceAll("^.*(\\.[^.]*)$", "$1"), origami.getNumberOfPages());
            } else {
                fileNames = new FileNameGeneratorImpl("", ".png", origami.getNumberOfPages());
            }

            UnitDimension paperDim = origami.getPaper().getSize();
            paperDim = paperDim.convertTo(Unit.INCH);
            final Dimension resultDim = new Dimension((int) (paperDim.getWidth() * dpi),
                    (int) (paperDim.getHeight() * dpi));

            final Graphics2D g;
            final BufferedImage buffer;
            if (format == ExportFormat.PNG) {
                buffer = new BufferedImage(resultDim.width, resultDim.height, BufferedImage.TYPE_INT_ARGB);
                g = buffer.createGraphics();
            } else { // TODO change to SVG and PDF code
                buffer = new BufferedImage(resultDim.width, resultDim.height, BufferedImage.TYPE_INT_ARGB);
                g = buffer.createGraphics();
            }

            Font font = new JMultilineLabel("").getFont();
            font = font.deriveFont((float) (font.getSize2D() * dpi / 72)); // normalize the font size

            IOException e = null;
            for (int i = 1; i <= origami.getNumberOfPages(); i++) {

                try {
                    drawPage(g, new Rectangle(0, 0, resultDim.width, resultDim.height), pageInsets, origami, i, locale,
                            font, withBackground, progressCallback);
                } catch (InterruptedException ex) {
                    g.dispose();
                    return result;
                }

                if (format == ExportFormat.PNG) {
                    try {
                        buffer.flush();
                        File pageFile = new File(parentFile, fileNames.getFileName(i));
                        ImageIO.write(buffer, "png", pageFile);
                        result.add(pageFile);
                    } catch (IOException ex) {
                        e = ex;
                    }
                } else {
                    // TODO SVG and PDF
                }
            }

            g.dispose();

            // this way we try to create the most files, so one error in the middle won't break the rest of files
            if (e != null)
                throw e;
        } else {
            // TODO other export formats
            Logger.getLogger("application").error("Unsupported export format: " + format);
            throw new IOException("Unsupported export format: " + format);
        }

        return result;
    }

    /**
     * Draw the given diagram page from the origami to the given graphics into the given rectangle.
     * 
     * @param graphics The graphics to draw to.
     * @param bounds The rectangle to draw into.
     * @param insets insets of the page.
     * @param origami The origami to draw.
     * @param page The page to draw.
     * @param locale The locale used to generate step descriptions.
     * @param font The font to draw text with.
     * @param withBackground If true, fill background with diagram background color from the origami, else leave the
     *            background transparent.
     * @param progressCallback The progress callback.
     */
    protected void drawPage(Graphics2D graphics, Rectangle bounds, Insets insets, Origami origami, int page,
            Locale locale, Font font, boolean withBackground, Runnable progressCallback) throws InterruptedException
    {
        int cols = origami.getPaper().getCols() != null ? origami.getPaper().getCols() : 1;
        int rows = origami.getPaper().getRows() != null ? origami.getPaper().getRows() : 1;

        int width = (bounds.width - cols * insets.left - insets.right) / cols;
        int height = (bounds.height - rows * insets.top - insets.bottom) / rows;

        if (withBackground)
            graphics.setBackground(origami.getPaper().getBackgroundColor());
        else
            graphics.setBackground(new Color(0, 0, 0, 0));
        graphics.clearRect(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());

        final Integer[] stepsPlacement = origami.getStepsPlacement(page);
        Step step = origami.getFirstStep(page);
        final int gridWidth = origami.getPaper().getCols();

        StepThumbnailGenerator generator = ServiceLocator.get(StepThumbnailGenerator.class);

        JPanel panel = new JPanel();
        panel.setSize(4000, 4000);

        JMultilineLabel label = new JMultilineLabel("");
        label.setOpaque(false);
        label.setFont(font);

        panel.setVisible(true);
        label.setVisible(true);

        for (int i : stepsPlacement) {
            final int x = i % gridWidth;
            final int y = i / gridWidth;

            final Rectangle stepBounds = new Rectangle(insets.left + x * (width + insets.left), insets.top + y
                    * (height + insets.top), width * (step.getColspan() != null ? step.getColspan() : 1), height
                    * (step.getRowspan() != null ? step.getRowspan() : 1));

            label.setText(step.getDescriptionWithId(locale));

            int labelHeight;
            {
                // this block computes the label's height when we know its fixed maximum width
                panel.removeAll();
                panel.setLayout(new FormLayout(stepBounds.width + "px", "default"));
                panel.add(label, new CellConstraints(1, 1));

                Queue<Container> containers = new LinkedList<Container>();
                containers.add(panel);
                Container container;
                while ((container = containers.poll()) != null) {
                    container.doLayout();
                    for (Component c : container.getComponents()) {
                        if (c instanceof Container)
                            containers.add((Container) c);
                    }
                }

                labelHeight = label.getSize().height;
            }

            Image image = generator.getThumbnail(origami, step, stepBounds.width, stepBounds.height - labelHeight,
                    withBackground);

            graphics.drawImage(image, stepBounds.x, stepBounds.y, null);

            label.paint(graphics.create(stepBounds.x, stepBounds.y + stepBounds.height - labelHeight, stepBounds.width,
                    labelHeight));

            try {
                if (progressCallback != null)
                    progressCallback.run();
            } catch (RuntimeException e) {
                throw new InterruptedException();
            }

            step = step.getNext();
        }
    }

    @Override
    public Origami loadModel(final URI path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException
    {
        URL url = null;
        if (path.isAbsolute()) {
            url = path.toURL();
        } else {
            url = new URL(documentBase, path.toString());
        }
        return loadModel(url, onlyMetadata);
    }

    @Override
    public Origami loadModel(final URL path, boolean onlyMetadata) throws IOException, UnsupportedDataFormatException
    {
        try {

            BindingsManager manager = ServiceLocator.get(BindingsManager.class);
            BindingsController<Origami> controller = new BindingsController<Origami>(manager, LATEST_SCHEMA_NAMESPACE);

            InputStream input = path.openStream();

            if (onlyMetadata) {
                TransformInfo transform = new TransformInfo(null, null, null, new MetadataLoadingHandler(input));
                controller.addAdditionalTransform(transform, TransformLocation.BEFORE_UNMARSHALLER, true);
            }

            model = controller.unmarshal(new InputStreamReader(input, "UTF8"));

            if (onlyMetadata) {
                // if only metadata are loaded, we need to provide a method to lazily load the rest of the diagram
                model.setLoadModelCallable(new Callable<Model>() {
                    @Override
                    public Model call() throws Exception
                    {
                        BindingsManager manager = ServiceLocator.get(BindingsManager.class);
                        BindingsController<Origami> controller = new BindingsController<Origami>(manager,
                                LATEST_SCHEMA_NAMESPACE);
                        return controller.unmarshal(new InputStreamReader(path.openStream())).getModel();
                    }
                });
            }

            model.setSrc(path);
            return (Origami) new ObjectFactory().createOrigami(model).getValue();
        } catch (UnmarshalException e) {
            throw new UnsupportedDataFormatException(e);
        } catch (JAXBException e) {
            throw new UnsupportedDataFormatException(e);
        }
    }

    /**
     * @return the documentBase
     */
    public URL getDocumentBase()
    {
        return documentBase;
    }

    /**
     * @param documentBase the documentBase to set
     */
    public void setDocumentBase(URL documentBase)
    {
        this.documentBase = documentBase;
    }

    /**
     * This XML filter first reads the version of the used schema, then decides, which JAXB unmarshaller it will use,
     * and, if desired, can stop reading when the model's metadata are read (in particular it will close the URL
     * connection immediately after the metadata are loaded).
     * 
     * @author Martin Pecka
     */
    protected class MetadataLoadingHandler extends ResultDelegatingDefaultHandler
    {
        /**
         * The input stream we are reading from. It is needed here in order to be able to prematurely close the
         * connection.
         */
        protected InputStream is;

        /** The QName of the root element. */
        protected String      rootElementQName = null;

        /**
         * @param is The input stream we are reading from.
         * @param onlyMetadata If true, close the <code>is</code> when the <code>model</code> tag is encountered.
         */
        protected MetadataLoadingHandler(InputStream is)
        {
            this.is = is;
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
                throws SAXException
        {
            if (namespaceURI.equals(LATEST_SCHEMA_NAMESPACE) && localName.equals("origami")) {
                rootElementQName = qName;
            } else if (localName.equals("model")) {
                try {
                    // metadate are loaded, so abort reading from the URL, close the connection
                    is.close();
                } catch (IOException e) {}

                super.startElement(namespaceURI, localName, qName, atts);
                // this will process the rest of the unmarshalling with the <model> tag empty
                endElement(namespaceURI, localName, qName);
                // this will end the marshalling at all
                endElement(LATEST_SCHEMA_NAMESPACE, "origami", rootElementQName);
                endDocument();

                // abort parsing
                throw new ParseAbortedException();
            }
            super.startElement(namespaceURI, localName, qName, atts);
        }
    }

    /**
     * A class that generates filenames for pages to be saved.
     * 
     * @author Martin Pecka
     */
    protected interface FileNameGenerator
    {
        /**
         * Return the filename of the given page.
         * 
         * @param page The page number (starting from 1!).
         * @return The file name.
         */
        String getFileName(int page);
    }

    /**
     * The default implementation of {@link FileNameGenerator} which includes the page number into the filename.
     * 
     * @author Martin Pecka
     */
    protected class FileNameGeneratorImpl implements FileNameGenerator
    {
        protected final String prefix;
        protected final String suffix;
        protected final int    numPages;
        protected final int    numDigits;

        /**
         * @param prefix The string to add before the digits.
         * @param suffix The string to add after the digits (including the dot and extension string).
         * @param numPages The overall number of pages this generator handles.
         */
        public FileNameGeneratorImpl(String prefix, String suffix, int numPages)
        {
            this.prefix = prefix;
            this.suffix = suffix;
            this.numPages = numPages;
            this.numDigits = (int) (Math.floor(Math.log10(numPages)) + 1);
        }

        @Override
        public String getFileName(int page)
        {
            String digits = Integer.toString(page);
            if (digits.length() < numDigits) {
                StringBuilder zeros = new StringBuilder();
                for (int i = 0; i < numDigits - digits.length(); i++)
                    zeros.append("0");
                digits = zeros + digits;
            }
            return prefix + digits + suffix;
        }
    }
}
