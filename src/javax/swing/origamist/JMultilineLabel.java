/**
 * 
 */
package javax.swing.origamist;

import javax.swing.JEditorPane;
import javax.swing.UIManager;

/**
 * A JLabel-like component displaying its text in multiple lines if needed.
 * 
 * @author Martin Pecka
 */
public class JMultilineLabel extends JEditorPane
{
    /** */
    private static final long serialVersionUID    = 5609298956672557477L;

    /** If true, the string the user set to this label began with &lt;html&gt; */
    protected boolean         wasSetHtml          = false;

    /** If true, don't add a blank line at the end of the content. */
    protected boolean         disableLastLineHack = false;

    /** Exactly the text set by setText(). */
    protected String          rawText             = "";

    public JMultilineLabel(String text)
    {
        this.setEditable(false);
        this.setCursor(null);
        this.setOpaque(false);
        this.setFocusable(false);
        this.setFont(UIManager.getFont("Label.font"));
        this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        this.setMargin(null);
        this.setContentType("text/html");
        setText(text);
    }

    /**
     * @return The text that was last set by setText().
     */
    public String getRawText()
    {
        return this.rawText;
    }

    @Override
    public synchronized void setText(String text)
    {
        this.rawText = text;
        if (text == null || "".equals(text)) {
            super.setText("<html><body>&nbsp;</body></html>");
            return;
        }

        String t = text;
        if (!text.startsWith("<html>")) {
            t = "<html><body>" + text.replaceAll("<", "&lt;") + "</body></html>";
            wasSetHtml = false;
        } else {
            wasSetHtml = true;
        }
        t = text.replaceAll("</html>", "");
        // HACK: the next line is needed, without it the last line sometimes disappears
        if (!disableLastLineHack)
            t += "<br/>&nbsp;</html>";
        super.setText(t);
    }

    /**
     * @return If true, don't add a blank line at the end of the content.
     */
    public boolean isDisableLastLineHack()
    {
        return disableLastLineHack;
    }

    /**
     * @param disableLastLineHack If true, don't add a blank line at the end of the content.
     */
    public void setDisableLastLineHack(boolean disableLastLineHack)
    {
        this.disableLastLineHack = disableLastLineHack;
    }
}
