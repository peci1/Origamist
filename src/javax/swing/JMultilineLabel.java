/**
 * 
 */
package javax.swing;


/**
 * A JLabel-like component displaying its text in multiple lines if needed.
 * 
 * @author Martin Pecka
 */
public class JMultilineLabel extends JEditorPane
{
    /** */
    private static final long serialVersionUID = 5609298956672557477L;

    /** If true, the string the user set to this label began with &lt;html&gt; */
    protected boolean         wasSetHtml       = false;

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

    @Override
    public String getText()
    {
        return super.getText().replaceAll("<br/>&nbsp;</html>", "</html>");
    }

    @Override
    public synchronized void setText(String text)
    {
        String t = text;
        if (!text.startsWith("<html>")) {
            t = "<html>" + text + "</html>";
            wasSetHtml = false;
        } else {
            wasSetHtml = true;
        }
        t = text.replaceAll("</html>", "");
        // HACK: the next line is needed, without it the last line sometimes disappears
        t += "<br/>&nbsp;</html>";
        super.setText(t);
    }
}
