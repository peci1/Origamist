/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlTransient;

import cz.cuni.mff.peckam.java.origamist.configuration.Configuration;
import cz.cuni.mff.peckam.java.origamist.exceptions.InvalidOperationException;
import cz.cuni.mff.peckam.java.origamist.math.Segment3d;
import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.modelstate.ModelState;
import cz.cuni.mff.peckam.java.origamist.modelstate.arguments.OperationArgument;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;
import cz.cuni.mff.peckam.java.origamist.utils.HasBoundProperties;

/**
 * An operation on the model.
 * <p>
 * Provides property: icon
 * 
 * @author Martin Pecka
 */
@XmlTransient
public abstract class Operation extends cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation implements
        HasBoundProperties
{

    /** The icon property. */
    public static final String                  ICON_PROPERTY = "icon:cz.cuni.mff.peckam.java.origamist.model.Operation";

    /** The type property. */
    public static final String                  TYPE_PROPERTY = "type:cz.cuni.mff.peckam.java.origamist.model.Operation";

    /** The list of arguments of this operation. */
    protected transient List<OperationArgument> arguments     = null;

    /** The bundle for Operation. */
    protected transient ResourceBundle          messages;

    /** The name of the resource bundle that contains operations string. */
    public static final String                  BUNDLE_NAME   = Operation.class.getName().replaceAll("\\.[^.]*$", "")
                                                                      + ".Operations";

    /**
     * 
     */
    public Operation()
    {
        ServiceLocator.get(ConfigurationManager.class).get()
                .addAndRunResourceBundleListener(new Configuration.ResourceBundleLocaleListener(BUNDLE_NAME) {
                    @Override
                    public void bundleChanged()
                    {
                        messages = this.bundle;
                    }
                });
    }

    /**
     * @return The icon of this operation
     */
    @XmlTransient
    public ImageIcon getIcon()
    {
        return OperationsHelper.getIcon(type);
    }

    @Override
    public void setType(Operations value)
    {
        Operations oldType = this.type;
        super.setType(value);
        if (oldType != value)
            support.firePropertyChange(TYPE_PROPERTY, oldType, value);
    }

    /**
     * Perform folding from the previous state to a new state by this operation. Calling this method will alter the
     * passed ModelState.
     * 
     * <p>
     * Subclasses of {@link Operation} should overwrite this method. They shouldn't call
     * <code>super.getModelState(previousState)</code>.
     * 
     * @param previousState The state the model has now.
     * @return The passed-in state of the model altered by performing this operation.
     * 
     * @throws InvalidOperationException If the operation cannot be completed.
     */
    public ModelState getModelState(ModelState previousState) throws InvalidOperationException
    {
        throw new UnsupportedOperationException("Class " + getClass() + " is a subclass of " + Operation.class
                + " and therefore must overwrite the getModelState() method.");
    }

    /**
     * @return True if this operation should be skipped for its originating step in delayed operations mode. Only the
     *         operations sign will be added to the originating step.
     */
    public boolean isCompletelyDelayedToNextStep()
    {
        return false;
    }

    /**
     * Return a segment with the operation's furthest rotated point as P1 and the center of the operation as P2 (in the
     * last modelState this operation was applied to). If this method returns <code>null</code>, no specific location is
     * required.
     * 
     * @return A segment with the operation's furthest rotated point as P1 and the center of the operation as P2. If
     *         this method returns <code>null</code>, no specific location is required.
     * 
     * @throws IllegalStateException Can be thrown if this operation hasn't been applied to a modelState yet (but
     *             doesn't have to).
     */
    public Segment3d getMarkerPosition() throws IllegalStateException
    {
        return null;
    }

    /**
     * Create and return the list of arguments of this operation. Do not set it directly into this.arguments.
     */
    protected List<OperationArgument> initArguments()
    {
        return new ArrayList<OperationArgument>(0);
    }

    /**
     * @return The list of arguments of this operation. The returned list is unmodifiable.
     */
    public List<OperationArgument> getArguments()
    {
        if (arguments == null) {
            arguments = Collections.unmodifiableList(initArguments());
            for (int i = 0; i + 1 < arguments.size(); i++)
                arguments.get(i).setNext(arguments.get(i + 1));
        }
        return arguments;
    }

    /**
     * @return True if all required arguments of this operation are completely filled-in.
     */
    public boolean areRequiredAgrumentsComplete()
    {
        for (OperationArgument arg : getArguments()) {
            if (!arg.isComplete() && arg.isRequired())
                return false;
        }
        return true;
    }

    /**
     * @return True if all arguments of this operation are completely filled-in.
     */
    public boolean areAgrumentsComplete()
    {
        for (OperationArgument arg : getArguments()) {
            if (!arg.isComplete())
                return false;
        }
        return true;
    }

    /**
     * Fill the properties of this operation from this.arguments.
     * 
     * @throws IllegalStateException If {@link #areRequiredAgrumentsComplete()} returns false.
     */
    public void fillFromArguments() throws IllegalStateException
    {
        if (!areRequiredAgrumentsComplete())
            throw new IllegalStateException("Cannot fill operation properties from uncompleted argument list.");
    }

    /**
     * @return The name of this operation in the current locale.
     */
    public String getL7dName()
    {
        return messages.getString(type.toString());
    }

    /**
     * Return the text that should be displayed to the user when constructing the given argument.
     * 
     * @param argument The argument that is constructed (pass only the instances from {@link #getArguments()}).
     * @return The text to display (may contain HTML).
     */
    public String getL7dUserTip(OperationArgument argument)
    {
        return null;
    }

    /**
     * @return The description of the operation.
     */
    public String getDefaultDescription()
    {
        return OperationsHelper.toString(type);
    }

    /**
     * Return the description of this operation while it is being constructed.
     * 
     * @param currentArgument The argument the user currently sets.
     * @return The description.
     */
    public String getConstructDescription(OperationArgument currentArgument)
    {
        StringBuilder text = new StringBuilder("<html><body><span style=\"color:gray;\">").append(
                OperationsHelper.toString(type)).append("</span>");
        if (getArguments().size() > 0) {
            text.append("<ol style=\"margin: 0px; margin-left: 18px;\">");
            String currentArgStyle = "font-weight: bold; font-size: 120%; background-color: rgb(240,240,255);";
            String optionalArgStyle = "font-style: italic;";
            String completedArgStyle = "color: black; font-weight: bold;";
            for (OperationArgument arg : getArguments()) {
                text.append("<li><span style=\"font-size: 90%; color: gray; font-weight: normal;");
                if (currentArgument == arg)
                    text.append(currentArgStyle);
                if (!arg.isRequired())
                    text.append(optionalArgStyle);
                if (arg.isComplete())
                    text.append(completedArgStyle);
                text.append("\">");
                if (currentArgument == arg)
                    text.append(" &gt; ");
                text.append(
                        ResourceBundle.getBundle("editor",
                                ServiceLocator.get(ConfigurationManager.class).get().getLocale()).getString(
                                arg.getResourceBundleKey())).append(" </span></li>");
            }
            text.append("</ol>");
        }
        text.append("</body></html>");
        return text.toString();
    }

}
