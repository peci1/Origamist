/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.LineArray;
import javax.media.j3d.Node;
import javax.media.j3d.PointArray;

import com.sun.j3d.utils.picking.PickResult;

import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;

public enum PickMode
{
    POINT
    {
        @Override
        PickMode getNext()
        {
            return LINE;
        }

        @Override
        List<PickResult> filterPickResults(PickResult[] results)
        {
            List<PickResult> result = new LinkedList<PickResult>();
            if (results == null)
                return result;

            // let points be first in the list
            List<PickResult> lines = new LinkedList<PickResult>();
            for (PickResult r : results) {
                if (r.getGeometryArray() != null) {
                    if (r.getGeometryArray() instanceof LineArray) {
                        lines.add(r);
                    } else if (r.getGeometryArray() instanceof PointArray) {
                        result.add(r);
                    }
                }
            }
            result.addAll(lines);
            return result;
        }
    },
    LINE
    {
        @Override
        PickMode getNext()
        {
            return LAYER;
        }

        @Override
        List<PickResult> filterPickResults(PickResult[] results)
        {
            List<PickResult> result = new LinkedList<PickResult>();
            if (results == null)
                return result;

            for (PickResult r : results) {
                if (r.getGeometryArray() != null && r.getGeometryArray() instanceof LineArray) {
                    result.add(r);
                }
            }
            return result;
        }
    },
    LAYER
    {
        @Override
        PickMode getNext()
        {
            return POINT;
        }

        @Override
        List<PickResult> filterPickResults(PickResult[] results)
        {
            List<PickResult> result = new LinkedList<PickResult>();
            if (results == null)
                return result;

            Node node;
            for (PickResult r : results) {
                node = r.getNode(PickResult.TRANSFORM_GROUP);
                if (node != null && node.getUserData() instanceof Layer) {
                    // we have two shapes for each layer and we just want one of them to appear in the list
                    if (result.size() == 0 || result.get(result.size() - 1).getNode(PickResult.TRANSFORM_GROUP) != node)
                        result.add(r);
                }
            }
            return result;
        }
    };

    /**
     * @return The next pick mode in a cycle.
     */
    abstract PickMode getNext();

    /**
     * Return only those pick results this pick mode is interested in.
     * 
     * @param results Some pick results to filter.
     * @return The filtered pick results.
     */
    abstract List<PickResult> filterPickResults(PickResult[] results);
}