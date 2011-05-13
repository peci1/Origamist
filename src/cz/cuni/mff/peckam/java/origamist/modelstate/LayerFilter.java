/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.modelstate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A filter for filtering layer intersections.
 * 
 * @author Martin Pecka
 */
public class LayerFilter
{
    /** Indices of the layers to be retained. */
    protected List<Integer> intFilter   = null;
    /** The layers to be retained. */
    protected Set<Layer>    layerFilter = null;

    /**
     * @param intFilter Indices of the layers to be retained.
     */
    public LayerFilter(List<Integer> intFilter)
    {
        if (intFilter == null)
            throw new NullPointerException("Cannot create a null layer filter.");
        this.intFilter = intFilter;
    }

    /**
     * @param layerFilter The layers to be retained.
     */
    public LayerFilter(Set<Layer> layerFilter)
    {
        if (layerFilter == null)
            throw new NullPointerException("Cannot create a null layer filter.");
        this.layerFilter = layerFilter;
    }

    /**
     * Filter out layer intersections this layer isn't interested in.
     * 
     * @param layers The map of layer intersections to be filtered (will be altered).
     * @return The altered input map (the same instance).
     */
    public Map<Layer, ModelSegment> filter(Map<Layer, ModelSegment> layers)
    {
        if (layers == null)
            return null;

        if (layerFilter != null) {
            layers.keySet().retainAll(layerFilter);
        } else {
            int i = 1;
            Iterator<Entry<Layer, ModelSegment>> it = layers.entrySet().iterator();
            while (it.hasNext()) {
                it.next();
                if (!intFilter.contains(i++)) {
                    it.remove();
                }
            }
        }
        return layers;
    }
}
