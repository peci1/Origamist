/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.Component;
import java.net.URL;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 * Renderer to render a locale as a label with flag and the locale's name.
 * 
 * @author Martin Pecka
 */
public class LocaleListCellRenderer extends DefaultListCellRenderer
{

    /** */
    private static final long serialVersionUID = -189604891084671473L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Locale l = (Locale) value;
        setText(l.getDisplayName(l));
        URL flag = getClass().getResource("/resources/images/flags/" + l.getCountry().toLowerCase() + ".png");
        if (flag != null)
            setIcon(new ImageIcon(flag));

        return this;
    }

}
