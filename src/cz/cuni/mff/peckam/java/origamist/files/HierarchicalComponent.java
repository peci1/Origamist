/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.files;

/**
 * A component that is a part of a hierarchy.
 * 
 * @author Martin Pecka
 */
public interface HierarchicalComponent
{
    /**
     * Returns the id of this component composed of names of it and all of its parent components connected with
     * <code>separator</code>, starting with the highest component.
     * 
     * @param separator The string to connect the components with.
     * 
     * @return The hierarchical id of this component.
     */
    String getHierarchicalId(String separator);

    /**
     * Returns the parent of this component, or <code>null</code>, if this is the topmost component.
     * 
     * @return The parent of this component, or <code>null</code>, if this is the topmost component.
     */
    HierarchicalComponent getParent();
}
