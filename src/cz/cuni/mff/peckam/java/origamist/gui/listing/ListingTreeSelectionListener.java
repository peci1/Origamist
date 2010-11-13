/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.io.IOException;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.gui.viewer.OrigamiViewer;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ListingTreeSelectionListener implements TreeSelectionListener
{

    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        if (!e.isAddedPath()) {
            return;
        }
        Object selected = e.getPath().getLastPathComponent();
        if (selected instanceof cz.cuni.mff.peckam.java.origamist.files.File) {
            try {
                OrigamiViewer viewer = ServiceLocator.get(OrigamiViewer.class);
                if (viewer == null)
                    Logger.getLogger("application").log(Level.ERROR,
                            "Cannot use ListingTreeSelectionListener when not using OrigamiViewer");
                else
                    viewer.setDisplayedOrigami(((cz.cuni.mff.peckam.java.origamist.files.File) selected).getOrigami());
            } catch (UnsupportedDataFormatException e1) {
                Logger.getLogger("viewer").l7dlog(Level.ERROR, "modelLazyLoadException", e1);
            } catch (IOException e1) {
                Logger.getLogger("viewer").l7dlog(Level.ERROR, "modelLazyLoadException", e1);
            }
        }
    }

}
