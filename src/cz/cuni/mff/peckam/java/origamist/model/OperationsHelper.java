/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.model;

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
    /**
     * Get the default icon for the operation.
     * 
     * @param operation The operation to get icon for
     * @return The default icon of the operation
     */
    public static ImageIcon getIcon(Operations operation)
    {
        // TODO image icon code
        return null;
    }

    /**
     * Return a localized text representation of the operation.
     * 
     * @param operation The operation to convert to string.
     * @return A localized text representation of the operation.
     */
    public static String toString(Operations operation)
    {
        String key = null;
        switch (operation) {
            case MOUNTAIN_FOLD:
                key = "menu.operation.mountain";
                break;
            case VALLEY_FOLD:
                key = "menu.operation.valley";
                break;
            case MOUNTAIN_VALLEY_FOLD_UNFOLD:
                key = "menu.operation.mountainFoldUnfold";
                break;
            case VALLEY_MOUNTAIN_FOLD_UNFOLD:
                key = "menu.operation.valleyFoldUnfold";
                break;
            case THUNDERBOLT_FOLD:
                key = "menu.operation.thunderboltMountainFirst";
                break;
            case TURN_OVER:
                key = "menu.operation.turnOver";
                break;
            case ROTATE:
                key = "menu.operation.rotate";
                break;
            case PULL:
                key = "menu.operation.pull";
                break;
            case INSIDE_CRIMP_FOLD:
                key = "menu.operation.crimpInside";
                break;
            case OUTSIDE_CRIMP_FOLD:
                key = "menu.operation.crimpOutside";
                break;
            case OPEN:
                key = "menu.operation.open";
                break;
            case INSIDE_REVERSE_FOLD:
                key = "menu.operation.reverseInside";
                break;
            case OUTSIDE_REVERSE_FOLD:
                key = "menu.operation.reverseOutside";
                break;
            case REPEAT_ACTION:
                key = "menu.operation.repeat";
                break;
            case RABBIT_FOLD:
                key = "menu.operation.rabbit";
                break;
            case SQUASH_FOLD:
                key = "menu.operation.squash";
                break;
        }

        if (key == null)
            return null;

        return ResourceBundle.getBundle("editor", ServiceLocator.get(ConfigurationManager.class).get().getLocale())
                .getString(key);
    }
}
