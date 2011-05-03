/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.utils.Arrays;
import cz.cuni.mff.peckam.java.origamist.utils.ChangeNotification;
import cz.cuni.mff.peckam.java.origamist.utils.CustomPropertyChangeSupport;
import cz.cuni.mff.peckam.java.origamist.utils.HasBoundProperties;
import cz.cuni.mff.peckam.java.origamist.utils.HasObservableProperties;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList;
import cz.cuni.mff.peckam.java.origamist.utils.ObservableList.ChangeTypes;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertiesSupport;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyEvent;
import cz.cuni.mff.peckam.java.origamist.utils.ObservablePropertyListener;
import cz.cuni.mff.peckam.java.origamist.utils.Observer;
import cz.cuni.mff.peckam.java.origamist.utils.PropagatedObservablePropertyEvent;
import cz.cuni.mff.peckam.java.origamist.utils.PropagatedPropertyChangeEvent;

/**
 * The base of all JAXB-generated classes.
 * 
 * @author Martin Pecka
 */
@XmlTransient
public abstract class GeneratedClassBase implements HasBoundProperties, HasObservableProperties
{
    /** The object handling property changes. */
    protected transient final CustomPropertyChangeSupport support            = new CustomPropertyChangeSupport(this);

    /** The object handling observable property changes. */
    protected transient final ObservablePropertiesSupport observableSupport  = new ObservablePropertiesSupport(this);

    /**
     * The object this one is attached to by a child monitor. Used mainly to recognize if this object is being monitored
     * or not at all.
     */
    private transient GeneratedClassBase                  attachedTo         = null;

    /** The parent object's property this object is attached to. Will be null for elements of list fields. */
    private transient String                              attachedToProperty = null;

    /** A child monitor listener - re-propagates all events propagated from a child. */
    private transient final PropertyChangeListener        childListener;

    /** An observable child monitor listener - re-propagates all events propagated from a child. */
    private transient final ObservablePropertyListener<?> observableChildListener;

    public GeneratedClassBase()
    {
        childListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (!(evt instanceof PropagatedPropertyChangeEvent))
                    return;

                if (evt instanceof PropagatedPropertyChangeEvent
                        && ((PropagatedPropertyChangeEvent) evt).getDeepSource() == GeneratedClassBase.this)
                    return;

                support.firePropertyChange(new PropagatedPropertyChangeEvent(GeneratedClassBase.this,
                        getAttachedToProperty(), evt));
            }
        };

        observableChildListener = new ObservablePropertyListener<Object>() {
            @Override
            public void changePerformed(ObservablePropertyEvent<?> evt)
            {
                if (evt instanceof PropagatedObservablePropertyEvent
                        && ((PropagatedObservablePropertyEvent<?>) evt).getDeepSource() == GeneratedClassBase.this)
                    return;

                observableSupport.fireObservablePropertyChange(new PropagatedObservablePropertyEvent<Object>(
                        GeneratedClassBase.this, getAttachedToProperty(), evt));
            }
        };

        // monitor all changes in children to manage the child monitor listeners
        support.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt instanceof PropagatedPropertyChangeEvent) {
                    return;
                }

                if (evt.getOldValue() != null) {
                    if (evt.getOldValue() instanceof HasBoundProperties)
                        ((HasBoundProperties) evt.getOldValue()).removeAllListeners(childListener);
                    if (evt.getOldValue() instanceof HasObservableProperties)
                        ((HasObservableProperties) evt.getOldValue())
                                .removeAllObservablePropertyListeners(observableChildListener);
                    if (evt.getOldValue() instanceof GeneratedClassBase
                            && ((GeneratedClassBase) evt.getOldValue()).attachedTo == GeneratedClassBase.this) {
                        ((GeneratedClassBase) evt.getOldValue()).attachedTo = null;
                        ((GeneratedClassBase) evt.getOldValue()).attachedToProperty = null;
                    }
                }

                if (evt.getNewValue() != null
                        && (evt.getNewValue() instanceof HasBoundProperties || evt.getNewValue() instanceof HasObservableProperties)) {
                    if (!(evt.getNewValue() instanceof GeneratedClassBase)
                            || (((GeneratedClassBase) evt.getNewValue()).attachedTo == null && !Arrays.contains(
                                    getNonChildProperties(), evt.getPropertyName()))) {
                        if (evt.getNewValue() instanceof HasBoundProperties)
                            ((HasBoundProperties) evt.getNewValue()).addPropertyChangeListener(childListener);
                        if (evt.getNewValue() instanceof HasObservableProperties)
                            ((HasObservableProperties) evt.getNewValue())
                                    .addObservablePropertyListener(observableChildListener);
                        if (evt.getNewValue() instanceof GeneratedClassBase) {
                            ((GeneratedClassBase) evt.getNewValue()).attachedTo = GeneratedClassBase.this;
                            ((GeneratedClassBase) evt.getNewValue()).attachedToProperty = evt.getPropertyName();
                        }
                    }
                } else if (getAttachedToProperty() != null) {
                    support.firePropertyChange(new PropagatedPropertyChangeEvent(GeneratedClassBase.this,
                            getAttachedToProperty(), evt));
                }
            }
        });
    }

    /**
     * Finish the initialization of the generated class after all fields are properly initialized.
     */
    protected void init()
    {
        if (getListFields() != null) {

            assert getListProperties() != null;

            List<String> properties = java.util.Arrays.asList(getListProperties());
            Iterator<String> propertiesIt = properties.iterator();

            for (final List<?> list : getListFields()) {
                assert propertiesIt.hasNext();
                final String propertyName = propertiesIt.next();

                if (!(list instanceof ObservableList<?>))
                    continue;

                ((ObservableList<?>) list).addObserver(new Observer<Object>() {
                    @Override
                    public void changePerformed(ChangeNotification<?> change)
                    {
                        if (change.getChangeType() != ChangeTypes.ADD) {
                            if (change.getOldItem() != null) {
                                if (change.getOldItem() instanceof HasBoundProperties)
                                    ((HasBoundProperties) change.getOldItem()).removeAllListeners(childListener);
                                if (change.getOldItem() instanceof HasObservableProperties)
                                    ((HasObservableProperties) change.getOldItem())
                                            .removeAllObservablePropertyListeners(observableChildListener);
                                if (change.getOldItem() instanceof GeneratedClassBase
                                        && ((GeneratedClassBase) change.getOldItem()).attachedTo == GeneratedClassBase.this) {
                                    ((GeneratedClassBase) change.getOldItem()).attachedTo = null;
                                    ((GeneratedClassBase) change.getOldItem()).attachedToProperty = null;
                                }
                            }
                        }
                        if (change.getChangeType() != ChangeTypes.REMOVE) {
                            if (change.getItem() != null) {
                                if (!(change.getItem() instanceof GeneratedClassBase)
                                        || (((GeneratedClassBase) change.getItem()).attachedTo == null && !Arrays
                                                .contains(getNonChildProperties(), propertyName))) {
                                    if (change.getItem() instanceof HasBoundProperties)
                                        ((HasBoundProperties) change.getItem())
                                                .addPropertyChangeListener(childListener);
                                    if (change.getItem() instanceof HasObservableProperties)
                                        ((HasObservableProperties) change.getItem())
                                                .addObservablePropertyListener(observableChildListener);
                                    if (change.getItem() instanceof GeneratedClassBase) {
                                        ((GeneratedClassBase) change.getItem()).attachedTo = GeneratedClassBase.this;
                                        ((GeneratedClassBase) change.getItem()).attachedToProperty = propertyName;
                                    }
                                }
                            }
                        }

                        observableSupport.fireObservablePropertyChange(new PropagatedObservablePropertyEvent<Object>(
                                GeneratedClassBase.this, getAttachedToProperty(), new ObservablePropertyEvent<Object>(
                                        GeneratedClassBase.this, propertyName, change)));
                    }
                });
            }
        }
    }

    /**
     * @return the attachedTo
     */
    public GeneratedClassBase getAttachedTo()
    {
        return attachedTo;
    }

    /**
     * @return the attachedToProperty
     */
    public String getAttachedToProperty()
    {
        return attachedToProperty != null ? attachedToProperty : (attachedTo != null ? attachedTo.attachedToProperty
                : null);
    }

    /**
     * Override this method in all child classes having observable list fields.
     * 
     * @return All observable list fields of this class (will be used to setup property change listeners on these
     *         fields).
     */
    protected List<?>[] getListFields()
    {
        return null;
    }

    /**
     * Override this method in all child classes having observable list fields.
     * 
     * @return All observable list fields' property names in the same order as getListFields().
     */
    protected String[] getListProperties()
    {
        return null;
    }

    /**
     * @return An array of all properties not referring to the "children" of this object (eg. referring to siblings or
     *         parents).
     */
    protected String[] getNonChildProperties()
    {
        return null;
    }

    @Override
    public List<String> removeAllListeners(PropertyChangeListener param0)
    {
        return this.support.removeAllListeners(param0);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener param0)
    {
        this.support.addPropertyChangeListener(param0);
    }

    @Override
    public void addPropertyChangeListener(String param0, PropertyChangeListener param1)
    {
        this.support.addPropertyChangeListener(param0, param1);
    }

    @Override
    public void removePropertyChangeListener(String param0, PropertyChangeListener param1)
    {
        this.support.removePropertyChangeListener(param0, param1);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener param0)
    {
        this.support.removePropertyChangeListener(param0);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener, String... propertyName)
    {
        support.addPropertyChangeListener(listener, propertyName);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener, String... propertyName)
    {
        support.removePropertyChangeListener(listener, propertyName);
    }

    @Override
    public void addPrefixedPropertyChangeListener(PropertyChangeListener listener, String... prefix)
    {
        support.addPrefixedPropertyChangeListener(listener, prefix);
    }

    @Override
    public void removePrefixedPropertyChangeListener(PropertyChangeListener listener)
    {
        support.removePrefixedPropertyChangeListener(listener);
    }

    @Override
    public void addObservablePropertyListener(ObservablePropertyListener<?> listener)
    {
        observableSupport.addObservablePropertyListener(listener);
    }

    @Override
    public void addObservablePropertyListener(String property, ObservablePropertyListener<?> listener)
    {
        observableSupport.addObservablePropertyListener(property, listener);
    }

    @Override
    public void removeObservablePropertyListener(String property, ObservablePropertyListener<?> listener)
    {
        observableSupport.removeObservablePropertyListener(property, listener);
    }

    @Override
    public void removeObservablePropertyListener(ObservablePropertyListener<?> listener)
    {
        observableSupport.removeObservablePropertyListener(listener);
    }

    @Override
    public List<String> removeAllObservablePropertyListeners(ObservablePropertyListener<?> listener)
    {
        return observableSupport.removeAllObservablePropertyListeners(listener);
    }

    @Override
    public void addObservablePropertyListener(ObservablePropertyListener<?> listener, String... property)
    {
        observableSupport.addObservablePropertyListener(listener, property);
    }

    @Override
    public void removeObservablePropertyListener(ObservablePropertyListener<?> listener, String... property)
    {
        observableSupport.removeObservablePropertyListener(listener, property);
    }

    @Override
    public <T> void addPrefixedObservablePropertyListener(ObservablePropertyListener<T> listener, String... prefix)
    {
        observableSupport.addPrefixedObservablePropertyListener(listener, prefix);
    }

    @Override
    public <T> void removePrefixedObservablePropertyListener(ObservablePropertyListener<T> listener)
    {
        observableSupport.removePrefixedObservablePropertyListener(listener);
    }
}