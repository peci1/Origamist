/**
 * 
 */
package javax.swing.origamist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * A button group providing the <code>selection</code> bound property.
 * 
 * @author Martin Pecka
 */
public class BoundButtonGroup extends ButtonGroup
{
    /** */
    private static final long                        serialVersionUID = 5917998096623763012L;

    /** The property helper instance. */
    protected PropertyChangeSupport                  properties       = new PropertyChangeSupport(this);

    /** The cache for obtaining a button with the given model. */
    protected Hashtable<ButtonModel, AbstractButton> buttonsForModels = new Hashtable<ButtonModel, AbstractButton>();

    /** The listener for maintaining the <code>buttonsForModels</code>. */
    protected PropertyChangeListener                 modelListener;

    /**
     * 
     */
    public BoundButtonGroup()
    {
        modelListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateButtonsForModels((ButtonModel) evt.getOldValue(), (ButtonModel) evt.getNewValue(),
                        (AbstractButton) evt.getSource());
            }
        };
    }

    @Override
    public void add(AbstractButton b)
    {
        super.add(b);
        buttonsForModels.put(b.getModel(), b);
        b.addPropertyChangeListener(AbstractButton.MODEL_CHANGED_PROPERTY, modelListener);
    }

    @Override
    public void remove(AbstractButton b)
    {
        super.remove(b);
        buttonsForModels.remove(b.getModel());
        b.removePropertyChangeListener(modelListener);
    }

    @Override
    public void clearSelection()
    {
        ButtonModel oldSelection = getSelection();
        super.clearSelection();
        if (oldSelection != null) {
            properties.firePropertyChange("selection", oldSelection, null);
        }
    }

    @Override
    public void setSelected(ButtonModel m, boolean b)
    {
        ButtonModel oldSelection = getSelection();
        super.setSelected(m, b);
        if (oldSelection != getSelection()) {
            properties.firePropertyChange("selection", oldSelection, getSelection());
        }
    }

    /**
     * @return The selected button.
     */
    public AbstractButton getSelectedButton()
    {
        return getButtonForModel(getSelection());
    }

    /**
     * Returns the button associated to the given model in this group.
     * 
     * @param model The model of the button.
     * @return Returns the button associated to the given model in this group.
     */
    public AbstractButton getButtonForModel(ButtonModel model)
    {
        if (model == null)
            return null;
        return buttonsForModels.get(model);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        properties.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        properties.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        properties.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        properties.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes the old model from <code>buttonsForModels</code> and adds a new entry for the new model and button.
     * 
     * @param oldModel The model to remove.
     * @param newModel The model to use as the new <code>button</code>'s key.
     * @param button The button that owns the <code>newModel</code>
     */
    protected void updateButtonsForModels(ButtonModel oldModel, ButtonModel newModel, AbstractButton button)
    {
        buttonsForModels.remove(oldModel);
        buttonsForModels.put(newModel, button);
    }

}
