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

    public JMultilineLabel(String text)
    {
        if (text.startsWith("<html>")) {
            this.setContentType("text/html");
            String t = text.replaceAll("</html>", "");
            // HACK: the next line is needed, without it the last line sometimes disappears
            t += "<br/>&nbsp;</html>";
            setText(t);
        } else {
            this.setContentType("text/plain");
            setText(text);
        }
        this.setEditable(false);
        this.setCursor(null);
        this.setOpaque(false);
        this.setFocusable(false);
        this.setFont(UIManager.getFont("Label.font"));
        this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        this.setMargin(null);
    }

    @Override
    public String getText()
    {
        return super.getText().replaceAll("<br/>&nbsp;</html>", "</html>");
    }
}
