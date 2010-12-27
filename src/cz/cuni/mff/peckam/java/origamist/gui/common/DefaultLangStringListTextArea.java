/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import cz.cuni.mff.peckam.java.origamist.common.LangString;

/**
 * A textarea for inputting localized strings, which has the default textarea behavior set - it is scrollable and wraps
 * the overflowing lines.
 * 
 * @author Martin Pecka
 */
public class DefaultLangStringListTextArea extends JLangStringListTextField<JTextArea>
{
    /** */
    private static final long serialVersionUID = -5897950315777051488L;

    /**
     * @param strings The list of localized strings this input represents.
     */
    public DefaultLangStringListTextArea(List<LangString> strings)
    {
        super(strings, new JTextArea(3, 20), new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        getTextField().setLineWrap(true);
        getTextField().setWrapStyleWord(true);
    }
}
