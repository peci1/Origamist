/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.gui.viewer.OrigamiViewer;
import cz.cuni.mff.peckam.java.origamist.model.Origami;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;

/**
 * Listens for the selections of model files and displays them in the main area.
 * 
 * @author Martin Pecka
 */
public class ListingTreeSelectionListener implements TreeSelectionListener
{

    @Override
    public void valueChanged(final TreeSelectionEvent e)
    {
        if (!e.isAddedPath()) {
            return;
        }

        final Object selected = e.getPath().getLastPathComponent();
        if (selected instanceof File) {
            final OrigamiViewer viewer = ServiceLocator.get(OrigamiViewer.class);
            if (viewer == null) {
                Logger.getLogger("application").log(Level.ERROR,
                        "Cannot use ListingTreeSelectionListener when not using OrigamiViewer");
            } else {
                final File file = (File) selected;
                final ListingTree tree = (ListingTree) e.getSource();

                file.addPropertyChangeListener("isOrigamiLoaded", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        final Integer selectedRow;
                        if (tree.getSelectionRows() != null && tree.getSelectionRows().length > 0)
                            selectedRow = tree.getSelectionRows()[0];
                        else
                            selectedRow = null;
                        file.fillFromOrigami();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                // TODO: this is very very ineffective, but since we can change the hashcode of the file
                                // by loading the origami, this seems like the only suitable way to workaround crashing
                                // JTree which cannot deal with hashcode-changing nodes
                                tree.setModel(new ListingTreeModel((Listing) tree.getModel().getRoot()));
                                tree.restoreExpanded();
                                if (selectedRow != null)
                                    tree.setSelectionRow(selectedRow.intValue());
                            }
                        });
                    }
                });

                new Thread() {
                    @Override
                    public void run()
                    {
                        try {
                            final Origami origami = file.getOrigami();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                    viewer.setDisplayedOrigami(origami);
                                }
                            });
                        } catch (UnsupportedDataFormatException e1) {
                            Logger.getLogger("viewer").l7dlog(Level.ERROR, "modelLazyLoadException", e1);
                        } catch (IOException e1) {
                            Logger.getLogger("viewer").l7dlog(Level.ERROR, "modelLazyLoadException", e1);
                        }
                    }
                }.start();
            }
        }
    }

}