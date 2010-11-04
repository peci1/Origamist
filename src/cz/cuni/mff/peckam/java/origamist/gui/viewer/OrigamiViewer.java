/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.Categories;
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.files.ObjectFactory;
import cz.cuni.mff.peckam.java.origamist.gui.CommonGui;
import cz.cuni.mff.peckam.java.origamist.gui.DiagramRenderer;
import cz.cuni.mff.peckam.java.origamist.logging.GUIAppender;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.model.Step;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ListingHandler;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.OrigamiHandler;

/**
 * The viewer of the origami model.
 * 
 * @author Martin Pecka
 */
public class OrigamiViewer extends CommonGui
{

    private static final long serialVersionUID            = -6853141518719373854L;

    /** The mode of displaying diagrams. */
    protected StartupMode     startupMode                 = StartupMode.PAGE;

    /** Download whole models for all selected files. */
    public static int         MODEL_DOWNLOAD_MODE_ALL     = -2;
    /** Don't download models. They'll be downloaded on demand (metadata will not be accessible). */
    public static int         MODEL_DOWNLOAD_MODE_HEADERS = -1;
    /** Download only metadata for all models. */
    public static int         MODEL_DOWNLOAD_MODE_NONE    = 0;
    /**
     * Meaning of the values is listed below. The values only have effect when a whole directory or a set of files is
     * specified as the list of files to show.
     * 
     * MODEL_DOWNLOAD_MODE_ALL - Download whole models for all selected files.
     * MODEL_DOWNLOAD_MODE_NONE - Don't download models. They'll be downloaded on demand (metadata will not be
     * accessible).
     * MODEL_DOWNLOAD_MODE_HEADERS - Download only metadata for all models.
     * other positive number <code>n</code> - use the ALL mode for the first <code>n</code> files and the NONE mode
     * for all other files.
     */
    protected int             modelDownloadMode           = 10;

    /** Name of the file containing the listing of files to display. */
    public static String      LISTING_FILE_NAME           = "listing.xml";

    /** The files to be displayed by the viewer. */
    protected Listing         filesToDisplay              = null;

    /** If true, show the sidebar with open files by default, otherwise hide it by default. */
    protected boolean         showFileListingByDefault    = false;

    /**
     * Create and setup all the form components.
     */
    @Override
    protected void createComponents()
    {
        try {

            handleAppletParams();

            // if the iterator should be empty, then handleAppletParams will die with an exception
            Origami o = filesToDisplay.recursiveIterator().next().getOrigami();
            /*
             * StepRenderer r = new StepRenderer(); r.setOrigami(o); r.setStep((Step)
             * o.getModel().getSteps().getStep().get(3)); r.setPreferredSize(new Dimension(200, 200));
             */
            // TODO remove testing stuff and put some more meaningful code
            DiagramRenderer r = new DiagramRenderer(o, (Step) o.getModel().getSteps().getStep().get(0));
            r.setPreferredSize(new Dimension(500, 500));
            getContentPane().add(r, BorderLayout.NORTH);
            /*
             * try {
             * File docBase = new File(getDocumentBase().toURI());
             * if (!docBase.isDirectory())
             * docBase = docBase.getParentFile();
             * ServiceLocator.get(OrigamiHandler.class).save(o, new File(docBase, "..\\model.xml"));
             * } catch (MarshalException e) {
             * System.err.println(e);
             * } catch (JAXBException e) {
             * System.err.println(e);
             * } catch (URISyntaxException e) {}
             */
        } catch (UnsupportedDataFormatException e) {
            System.err.println(e); // TODO handle errors in data files
        } catch (IOException e) {
            System.err.println(e); // TODO handle IO errors
        }

    }

    /**
     * Setup the form layout.
     */
    @Override
    protected void buildLayout()
    {

    }

    /**
     * Handles the applet parameters. Eg. loads listing.xml, or sets other settings.
     */
    protected void handleAppletParams()
    {
        handleStartupModeParam();
        handleModelDownloadModeParam();
        handleFilesParam();
        // the recursive param is handled by the previous handler
    }

    /**
     * Handles the "startupMode" applet parameter.
     */
    protected void handleStartupModeParam()
    {
        if (getParameter("startupMode") != null) {
            try {
                startupMode = StartupMode.valueOf(getParameter("startupMode").toUpperCase());
            } catch (IllegalArgumentException e) {
                Logger.getLogger("viewer").l7dlog(Level.ERROR, "startupModeParamInvalid",
                        new Object[] { Arrays.asList(StartupMode.values()) }, e);
                // we just do nothing, so the default value remains set
            }
        }
    }

    /**
     * Handles the "modelDownloadMode" applet parameter.
     */
    protected void handleModelDownloadModeParam()
    {
        String param = getParameter("modelDownloadMode");
        if (param != null) {
            try {
                modelDownloadMode = Integer.parseInt(param);
                if (modelDownloadMode < 0) {
                    modelDownloadMode = MODEL_DOWNLOAD_MODE_NONE;
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                if (param.equalsIgnoreCase("all"))
                    modelDownloadMode = MODEL_DOWNLOAD_MODE_ALL;
                else if (param.equalsIgnoreCase("headers"))
                    modelDownloadMode = MODEL_DOWNLOAD_MODE_HEADERS;
                else if (param.equalsIgnoreCase("none")) {
                    modelDownloadMode = MODEL_DOWNLOAD_MODE_NONE;
                } else {
                    Logger.getLogger("viewer").l7dlog(Level.ERROR, "modelDownloadModeParamInvalid", e);
                    // we just do nothing, so the default value remains set
                }
            }
        }
    }

    /**
     * Handles the "files" applet parameter. (Also handles the "recursive" parameter, where applicable)
     */
    protected void handleFilesParam()
    {
        String param = getParameter("files");
        if (param == null) {
            Logger.getLogger("viewer").l7dlog(Level.FATAL, "filesParamMissing",
                    new IllegalArgumentException("The 'files' applet parameter must be set"));
            return;
        }

        if (param.endsWith(LISTING_FILE_NAME)) {
            showFileListingByDefault = true;
            try {
                URL paramURL = new URL(getDocumentBase(), param);
                // use the listing.xml location as the base for relative model URLs
                ServiceLocator.get(OrigamiHandler.class).setDocumentBase(paramURL);
                filesToDisplay = ServiceLocator.get(ListingHandler.class).load(paramURL);
            } catch (MalformedURLException e) {
                Logger.getLogger("viewer").l7dlog(Level.FATAL, "filesParamInvalidListingURL", e);
            } catch (UnsupportedDataFormatException e) {
                Logger.getLogger("viewer").l7dlog(Level.FATAL, "filesParamInvalidListingFormat", e);
            } catch (IOException e) {
                Logger.getLogger("viewer").l7dlog(Level.FATAL, "filesParamInvalidListingUnread", e);
            }
        } else {
            List<String> filesAsStrings = Arrays.asList(param.split(" "));
            boolean recursive = getParameter("recursive") != null;
            ObjectFactory of = new ObjectFactory();
            filesToDisplay = (Listing) of.createListing();
            List<File> files = new LinkedList<File>();
            for (String fileString : filesAsStrings) {
                try {
                    File file = new File(new URL(getDocumentBase(), fileString).toURI());
                    files.add(file);
                } catch (MalformedURLException e) {
                    Logger.getLogger("viewer").l7dlog(Level.ERROR, "filesParamInvalidItem",
                            new Object[] { fileString }, e);
                } catch (URISyntaxException e) {
                    Logger.getLogger("viewer").l7dlog(Level.ERROR, "filesParamInvalidItem",
                            new Object[] { fileString }, e);
                }
            }
            filesToDisplay.addFiles(files, recursive ? null : 1, null);

            if ((filesToDisplay.getFiles() == null || filesToDisplay.getFiles().getFile().size() == 0)
                    && (filesToDisplay.getCategories() == null || ((Categories) filesToDisplay.getCategories())
                            .sizeRecursive() == 0)) {
                Logger.getLogger("viewer").l7dlog(Level.FATAL, "filesParamInvalidFileList", null,
                        new IllegalArgumentException("No input files defined."));
            }

            // we only show the file listing if two or more models are being displayed
            Iterator<cz.cuni.mff.peckam.java.origamist.files.File> it = filesToDisplay.recursiveIterator();
            int numOfModels = 0;
            while (it.hasNext()) {
                numOfModels++;
                if (numOfModels == 2)
                    break;
            }

            showFileListingByDefault = numOfModels >= 2;

            if (modelDownloadMode == MODEL_DOWNLOAD_MODE_HEADERS || modelDownloadMode == MODEL_DOWNLOAD_MODE_ALL
                    || modelDownloadMode > 0) {
                Iterator<cz.cuni.mff.peckam.java.origamist.files.File> iterator = filesToDisplay.recursiveIterator();
                boolean onlyMetadata = modelDownloadMode == MODEL_DOWNLOAD_MODE_HEADERS;

                int i = 0;
                while (iterator.hasNext() && (modelDownloadMode == MODEL_DOWNLOAD_MODE_ALL || i++ < modelDownloadMode)) {
                    cz.cuni.mff.peckam.java.origamist.files.File file = iterator.next();
                    try {
                        file.getOrigami(onlyMetadata);
                        file.fillFromOrigami();
                    } catch (UnsupportedDataFormatException e) {
                        Logger.getLogger("viewer").l7dlog(Level.ERROR, "invalidModelFile",
                                new Object[] { file.getSrc() }, e);
                    } catch (MalformedURLException e) {
                        Logger.getLogger("viewer").l7dlog(Level.ERROR, "invalidModelSource",
                                new Object[] { file.getSrc() }, e);
                    } catch (IOException e) {
                        Logger.getLogger("viewer").l7dlog(Level.ERROR, "modelLoadIOError",
                                new Object[] { file.getSrc() }, e);
                    }
                }
            }
        }
    }

    @Override
    public void start()
    {
        super.start();

    }

    @Override
    public void stop()
    {
        super.stop();
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }

    @Override
    protected void setupLoggers()
    {
        super.setupLoggers();

        Logger l = Logger.getLogger("viewer");
        l.setResourceBundle(ResourceBundle.getBundle("viewer", ServiceLocator.get(ConfigurationManager.class).get()
                .getLocale()));
        l.setLevel(Level.ALL);
        l.addAppender(new GUIAppender(this));
    }

}