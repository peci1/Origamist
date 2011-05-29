/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import cz.cuni.mff.peckam.java.origamist.model.jaxb.Operations;
import cz.cuni.mff.peckam.java.origamist.services.ServiceLocator;
import cz.cuni.mff.peckam.java.origamist.services.interfaces.ConfigurationManager;

/**
 * Helper class for Operations enum.
 * 
 * @author Martin Pecka
 */
public class OperationsHelper
{
    /** The cache of icons. */
    protected static Map<Operations, ImageIcon> icons = new EnumMap<Operations, ImageIcon>(Operations.class);

    /**
     * Get the default icon for the operation.
     * 
     * @param operation The operation to get icon for
     * @return The default icon of the operation
     */
    public static ImageIcon getIcon(Operations operation)
    {
        ImageIcon icon = icons.get(operation);
        if (icon != null)
            return icon;

        String path = "/resources/images/fold-marks/";
        switch (operation) {
            case MOUNTAIN_FOLD:
                path += "mountain_fold.png";
                break;
            case VALLEY_FOLD:
                path += "valley_fold.png";
                break;
            case MOUNTAIN_VALLEY_FOLD_UNFOLD:
                path += "mountain_fold_unfold.png";
                break;
            case VALLEY_MOUNTAIN_FOLD_UNFOLD:
                path += "valley_fold_unfold.png";
                break;
            case THUNDERBOLT_FOLD:
                path += "thunderbolt_fold.png";
                break;
            case INSIDE_REVERSE_FOLD:
                path += "inside_reverse_fold.png";
                break;
            case OUTSIDE_REVERSE_FOLD:
                path += "outside_reverse_fold.png";
                break;
            case INSIDE_CRIMP_FOLD:
                path += "inside_crimp_fold.png";
                break;
            case OUTSIDE_CRIMP_FOLD:
                path += "outside_crimp_fold.png";
                break;
            case ROTATE:
                path += "rotate.png";
                break;
            case TURN_OVER:
                path += "turn_over.png";
                break;
            case REPEAT_ACTION:
                path += "repeat_one.png";
                break;
            case SYMMETRY:
                path += "repeat_one.png";
                break;
            case PULL:
                path += "pull.png";
                break;
            default:
                return null;
        }

        icon = new ImageIcon(OperationsHelper.class.getResource(path));

        icons.put(operation, icon);
        return icon;
    }

    /**
     * Return the operation corresponding to the given constant.
     * 
     * @param operation The constant defining an operation.
     * @return The operation.
     */
    public static Operation getOperation(Operations operation)
    {
        return getOperation(operation, false);
    }

    /**
     * Return the operation corresponding to the given constant.
     * 
     * @param operation The constant defining an operation.
     * @param alternative If true, return the alternative version of the operation.
     * @return The operation.
     */
    public static Operation getOperation(Operations operation, boolean alternative)
    {
        Operation result = null;
        switch (operation) {
            case MOUNTAIN_FOLD:
                result = new FoldOperation();
                break;
            case VALLEY_FOLD:
                result = new FoldOperation();
                break;
            case MOUNTAIN_VALLEY_FOLD_UNFOLD:
                result = new FoldUnfoldOperation();
                break;
            case VALLEY_MOUNTAIN_FOLD_UNFOLD:
                result = new FoldUnfoldOperation();
                break;
            case THUNDERBOLT_FOLD:
                result = new ThunderboltFoldOperation();
                if (alternative)
                    ((ThunderboltFoldOperation) result).setInvert(true);
                break;
            case TURN_OVER:
                result = new TurnOverOperation();
                break;
            case ROTATE:
                result = new RotateOperation();
                break;
            case PULL:
                result = new PullOperation();
                break;
            case INSIDE_CRIMP_FOLD:
                result = new CrimpFoldOperation();
                break;
            case OUTSIDE_CRIMP_FOLD:
                result = new CrimpFoldOperation();
                break;
            case OPEN:
                result = null; // TODO
                break;
            case INSIDE_REVERSE_FOLD:
                result = new ReverseFoldOperation();
                break;
            case OUTSIDE_REVERSE_FOLD:
                result = new ReverseFoldOperation();
                break;
            case REPEAT_ACTION:
                result = new RepeatOperation();
                break;
            case MARKER:
                result = new MarkerOperation();
                break;
            case RABBIT_FOLD:
                result = null; // TODO
                break;
            case SQUASH_FOLD:
                result = null; // TODO
                break;
            case IMAGE:
                result = new ImageOperation();
                break;
            case SYMMETRY:
                result = new SymmetryOperation();
                break;
        }

        if (result == null)
            return null;

        result.setType(operation);
        return result;
    }

    /**
     * Return a localized text representation of the operation.
     * 
     * @param operation The operation to convert to string.
     * @return A localized text representation of the operation.
     */
    public static String toString(Operations operation)
    {
        return ResourceBundle.getBundle(Operation.BUNDLE_NAME,
                ServiceLocator.get(ConfigurationManager.class).get().getLocale()).getString(operation.toString());
    }
}
