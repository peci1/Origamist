/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import javax.swing.JDialog;

import cz.cuni.mff.peckam.java.origamist.model.Origami;

/**
 * A frame for editing metadata of an origami. It can also be used to fill metadata for a just-created origami.
 * 
 * @author Martin Pecka
 */
public class OrigamiPropertiesFrame extends JDialog
{
    /** */
    private static final long serialVersionUID = -8222843108896744313L;

    /**
     * If <code>null</code>, indicates that the dialog should display texts for creating a model metadata, otherwise it
     * displays texts for editing an existing model's metadata.
     */
    protected Origami         origami;

    /**
     * If <code>true</code>, show texts for creating a model, otherwise display texts for editing an existing model's
     * metadata.
     */
    protected boolean         isCreating;

    /**
     * @param origami If <code>null</code>, indicates that the dialog should display texts for creating a model
     *            metadata, otherwise it displays texts for editing an existing model's metadata.
     */
    public OrigamiPropertiesFrame(Origami origami)
    {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setOrigami(origami);
        isCreating = (origami == null);
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
    protected void setOrigami(Origami origami)
    {
        this.origami = origami;
    }

}
