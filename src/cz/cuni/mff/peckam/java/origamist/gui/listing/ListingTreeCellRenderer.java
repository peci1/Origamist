/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.listing;

import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JImage;
import javax.swing.JLabel;
import javax.swing.JMultilineLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.cuni.mff.peckam.java.origamist.common.BinaryImage;
import cz.cuni.mff.peckam.java.origamist.exceptions.UnsupportedDataFormatException;
import cz.cuni.mff.peckam.java.origamist.files.Category;
import cz.cuni.mff.peckam.java.origamist.files.File;
import cz.cuni.mff.peckam.java.origamist.files.Listing;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class ListingTreeCellRenderer extends DefaultTreeCellRenderer
{

    /** */
    private static final long serialVersionUID = 3865858017604226291L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus)
    {
        Locale l = ServiceLocator.get(ConfigurationManager.class).get().getDiagramLocale();
        ResourceBundle messages = ResourceBundle.getBundle("cz.cuni.mff.peckam.java.origamist.gui.Gui", l);

        if (value instanceof Listing || value instanceof Category) {
            String text;
            if (value instanceof Listing) {
                text = messages.getString("listingRootNodeText");
            } else {
                text = ((Category) value).getName(l);
            }
            super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf, row, hasFocus);
            setIcon(expanded ? getOpenIcon() : getClosedIcon());
            return this;
        } else if (value instanceof File) {
            JPanel renderer = new JPanel();

            FormLayout layout = new FormLayout("$dmargin,30dlu,$lcgap,[30dlu,pref,100dlu],$dmargin",
                    "$dmargin,pref,$lgap,pref,$lgap,pref,$dmargin");
            layout.setRowGroups(new int[][] { { 2, 4 } });
            renderer.setLayout(layout);

            CellConstraints cc = new CellConstraints();
            File file = (File) value;
            if (file.isOrigamiLoaded()) {
                try {
                    ImageIcon icon = ((BinaryImage) file.getOrigami().getThumbnail().getImage()).getImageIcon();
                    if (icon != null) {
                        JImage image = new JImage(icon.getImage());
                        renderer.add(image, cc.xywh(2, 2, 1, 5));
                    }
                } catch (UnsupportedDataFormatException e) {
                    assert false : "The origami is loaded, but getOrigami() threw UnsupportedDataFormatException.";
                } catch (IOException e) {
                    assert false : "The origami is loaded, but getOrigami() threw IOException.";
                }
            }
            JLabel name = new JLabel("<html><b>" + file.getName(l) + "</b></html>");
            renderer.add(name, cc.xy(4, 2));

            JLabel author = new JLabel("<html><b>Autor: </b>" + file.getAuthor().getName() + "</html>");
            renderer.add(author, cc.xy(4, 4));
            author.setFont(author.getFont().deriveFont(Font.PLAIN));

            if (file.getShortdesc().size() > 0) {
                JMultilineLabel desc = new JMultilineLabel("<html><b>Popis: </b>" + file.getShortDesc(l) + "</html>");
                desc.setFont(name.getFont().deriveFont(Font.PLAIN));
                renderer.add(desc, cc.xy(4, 6));
            }
            return renderer;
        } else {
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }
}
