/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.viewer;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.origamist.JHideablePanel;

import cz.cuni.mff.peckam.java.origamist.model.Origami;

/**
 * Panel with info about a model.
 * 
 * @author Martin Pecka
 */
public class ModelInfoPanel extends JHideablePanel
{
    /** */
    private static final long serialVersionUID = 9041458561866387156L;
    protected Origami         origami;

    public ModelInfoPanel(Origami origami)
    {
        super(BorderLayout.NORTH);

        getContent().add(new JLabel("<html>info<br>asdasdas<br>asdasdas<br> asdasf<br>dsfdsf</html>"));
        getContent().add(new JButton("very long text"));

        setOrigami(origami);
    }

    /**
     * @return the origami
     */
    public Origami getOrigami()
    {
        return origami;
    }

    /**
     * @param origami the origami to set
     */
    public void setOrigami(Origami origami)
    {
        this.origami = origami;
        show();
    }
}
