/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.LineArray;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;

import com.sun.j3d.utils.pickfast.PickTool;

import cz.cuni.mff.peckam.java.origamist.modelstate.Layer;
import cz.cuni.mff.peckam.java.origamist.utils.LocalizedString;

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
        List<PickInfo> filterPickResults(PickTool pickTool, PickInfo[] results)
        {
            List<PickInfo> result = new LinkedList<PickInfo>();
            if (results == null)
                return result;

            // let points be first in the list
            List<PickInfo> lines = new LinkedList<PickInfo>();
            for (PickInfo r : results) {
                if (r.getNode() instanceof Shape3D) {
                    Shape3D shape = (Shape3D) r.getNode();
                    if (shape.getGeometry() != null) {
                        if (shape.getGeometry() instanceof LineArray) {
                            lines.add(r);
                        } else if (shape.getGeometry() instanceof PointArray) {
                            result.add(r);
                        }
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
        List<PickInfo> filterPickResults(PickTool pickTool, PickInfo[] results)
        {
            List<PickInfo> result = new LinkedList<PickInfo>();
            if (results == null)
                return result;

            for (PickInfo r : results) {
                if (r.getNode() instanceof Shape3D) {
                    Shape3D shape = (Shape3D) r.getNode();
                    if (shape.getGeometry() != null && shape.getGeometry() instanceof LineArray) {
                        result.add(r);
                    }
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
        List<PickInfo> filterPickResults(PickTool pickTool, PickInfo[] results)
        {
            List<PickInfo> result = new LinkedList<PickInfo>();
            if (results == null)
                return result;

            Node node;
            for (PickInfo r : results) {
                node = pickTool.getNode(r, PickTool.TYPE_TRANSFORM_GROUP);
                if (node != null && node.getUserData() instanceof Layer) {
                    // we have two shapes for each layer and we just want one of them to appear in the list
                    if (result.size() == 0
                            || pickTool.getNode(result.get(result.size() - 1), PickTool.TYPE_TRANSFORM_GROUP) != node)
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
     * @param pickTool The pick tool (canvas) used for the picking that gave these results.
     * @param results Some pick results to filter.
     * @return The filtered pick results.
     */
    abstract List<PickInfo> filterPickResults(PickTool pickTool, PickInfo[] results);

    /**
     * @return A localized name of this pick mode.
     */
    public String toL7dString()
    {
        return new LocalizedString(PickMode.class.getName(), this.toString()).toString();
    }
}