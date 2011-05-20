/**
 * 
 */
package javax.swing.origamist;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;

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
        // IMPORTANT: fix for bug 6993691
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6993691
        this.setEditorKit(new HTMLEditorKit() {
            private static final long serialVersionUID = -4615871144396935653L;

            protected Parser getParser()
            {
                try {
                    @SuppressWarnings("rawtypes")
                    Class c = Class.forName("javax.swing.text.html.parser.ParserDelegator");
                    Parser defaultParser = (Parser) c.newInstance();
                    return defaultParser;
                } catch (Throwable e) {}
                return null;
            }
        });

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
            super.setText("<html>&nbsp;</html>");
            return;
        }

        String t = text;
        if (!text.startsWith("<html>")) {
            t = "<html>" + text.replaceAll("<", "&lt;");
            wasSetHtml = false;
        } else {
            wasSetHtml = true;
        }
        t = text.replaceAll("</html>", "");
        // HACK: the next line is needed, without it the last line sometimes disappears
        if (!disableLastLineHack) {
            t += "<br/>&nbsp;</html>";
        } else {
            t += "</html>";
        }
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
