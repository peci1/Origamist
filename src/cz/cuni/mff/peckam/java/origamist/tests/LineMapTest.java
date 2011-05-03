/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.tests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.math.CanonicLine3d;
import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.utils.Line3dMap;
import cz.cuni.mff.peckam.java.origamist.utils.RedBlackTree;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class LineMapTest
{
    protected class TestableLineMap<V> extends Line3dMap<V>
    {

        /** */
        private static final long serialVersionUID = 4572014073299869053L;

        public boolean isValidRedBlackTree()
        {
            if (root == null)
                return true;

            // every node is either red or black, by definition, no need to check it

            if (root.getColor() != Color.BLACK) {
                System.err.println("LineMapTest: tree's root isn't black");
                return false;
            }

            // leaf nodes are represented by null here, we can assume that null is black... so there is also no need to
            // check the condition on all leaves being black

            Queue<RedBlackTree<CanonicLine3d, V>.Entry> queue = new LinkedList<RedBlackTree<CanonicLine3d, V>.Entry>();
            queue.add(root);

            while (!queue.isEmpty()) {
                RedBlackTree<CanonicLine3d, V>.Entry e = queue.poll();
                // every red nodes has black children
                if (e.getColor() == Color.RED) {
                    if (e.getLeft() != null && e.getLeft().getColor() != Color.BLACK) {
                        System.err.println("LineMapTest: Left child of red entry " + e + " isn't black.");
                        return false;
                    }
                    if (e.getRight() != null && e.getRight().getColor() != Color.BLACK) {
                        System.err.println("LineMapTest: Right child of red entry " + e + " isn't black.");
                        return false;
                    }
                }
                if (e.getLeft() != null)
                    queue.add(e.getLeft());
                if (e.getRight() != null)
                    queue.add(e.getRight());
            }

            if (nrOfBlackNodesOnPathsToLeaves(root) == null) {
                System.err.println("LineMapTest: Not all paths from root to leaves have equal number of black nodes.");
                return false;
            }

            return true;
        }

        private Integer nrOfBlackNodesOnPathsToLeaves(RedBlackTree<CanonicLine3d, V>.Entry e)
        {
            if (e == null)
                return 1;

            Integer int1 = nrOfBlackNodesOnPathsToLeaves(e.getLeft());
            Integer int2 = nrOfBlackNodesOnPathsToLeaves(e.getRight());

            if (int1 == null || int2 == null)
                return null;

            if (int1 != int2) {
                System.err.println("LineMapTest: Non-equal # of black nodes on paths to leaves in node " + e);
                return null;
            }

            return int1 + (e.getColor() == Color.BLACK ? 1 : 0);
        }
    }

    /**
     * Test method for {@link cz.cuni.mff.peckam.java.origamist.utils.Line3dMap}.
     */
    @Test
    public void testTree()
    {
        TestableLineMap<Integer> map = new TestableLineMap<Integer>();
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)), 100);
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(-1, -1, -1)), 99);
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0.5d, 0.5d, 0.5d)), 98);
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)), 100);
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)), 100);
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)), 100);
        validateTree(map);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0, 0, 3)), 99);
        validateTree(map);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 100 });

        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)), 101);
        validateTree(map);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 101, 100 });

        map.epsilonRemove(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 0, 1)));
        validateTree(map);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 101, 100 });

        map.epsilonRemove(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)));
        validateTree(map);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 101 });

        // the following is one more complex real case where the tree failed to remain correct
        TestableLineMap<Object> map2 = new TestableLineMap<Object>();
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 1.0, 0.0), new Vector3d(1.0, -0.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 1.0, 0.0), new Vector3d(1.0, -0.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 1.0, 0.0), new Vector3d(1.0, -0.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)), new Object());
        map2.epsilonRemove(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)));
        map2.epsilonRemove(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)));
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.5, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0),
                new Vector3d(0.7071067811865475, 0.7071067811865475, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonRemove(new Line3d(new Point3d(0.0, 1.0, 0.0), new Vector3d(1.0, -0.0, -0.0)));
        map2.epsilonRemove(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)));
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(2.0, 0.0, 0.0), new Vector3d(-0.8944271909999159, 0.4472135954999579,
                0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 1.0, 0.0), new Vector3d(1.0, -0.0, -0.0)), new Object());
        map2.epsilonRemove(new Line3d(new Point3d(0.0, 1.0, 0.0), new Vector3d(1.0, -0.0, -0.0)));
        map2.epsilonRemove(new Line3d(new Point3d(2.0, 0.0, 0.0), new Vector3d(-0.8944271909999159, 0.4472135954999579,
                0.0)));
        map2.epsilonPut(new Line3d(new Point3d(-0.00000000000021360690993788012, 0.0, 0.0), new Vector3d(
                0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0),
                new Vector3d(0.7071067811865475, 0.7071067811865475, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.5, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(-0.00000000000021360690993788012, 0.0, 0.0), new Vector3d(
                0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0),
                new Vector3d(0.7071067811865475, 0.7071067811865475, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.5, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(-0.00000000000021360690993788012, 0.0, 0.0), new Vector3d(
                0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.5, 0.0, 0.0000003267948965381209), new Vector3d(0.0,
                0.9999999999997864, -0.0000006535897930762418)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0000000000002136, 0.0, 0.00000016339744826906044), new Vector3d(
                -0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.4999999999997864, 0.0, 0.0), new Vector3d(
                0.00000000000042721381987576024, 0.9999999999997864, 0.0000006535897930761021)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.9999999999998932, 0.0, -0.00000016339744826902553), new Vector3d(
                -0.8944271909995337, 0.4472135954998624, 0.0000008768827240310362)), new Object());
        map2.epsilonRemove(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(-0.0, 1.0, -0.0)));
        map2.epsilonRemove(new Line3d(new Point3d(0.0, 0.0, 0.0), new Vector3d(0.7071067811865475, 0.7071067811865475,
                -0.0)));
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.5, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.5, 0.0, 0.0000003267948965381209), new Vector3d(0.0,
                0.9999999999997864, -0.0000006535897930762418)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0000000000002136, 0.0, 0.00000016339744826906044), new Vector3d(
                -0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.00000000000010680345496894006, 0.0000003267948965381209),
                new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(-0.00000000000021360690993788012, 0.0, 0.00000016339744826906044),
                new Vector3d(0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.4999999999997864, 0.0, 0.0), new Vector3d(
                0.00000000000042721381987576024, 0.9999999999997864, 0.0000006535897930761021)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.9999999999998932, 0.0, -0.00000016339744826902553), new Vector3d(
                -0.8944271909995337, 0.4472135954998624, 0.0000008768827240310362)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(-0.7071067811865475, 0.7071067811865475,
                -0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0, 0.0, 0.0), new Vector3d(0.0, 1.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.5, 0.0), new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.5, 0.0, 0.0000003267948965381209), new Vector3d(0.0,
                0.9999999999997864, -0.0000006535897930762418)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.0000000000002136, 0.0, 0.00000016339744826906044), new Vector3d(
                -0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.00000000000010680345496894006, 0.0000003267948965381209),
                new Vector3d(1.0, 0.0, 0.0)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(-0.00000000000021360690993788012, 0.0, 0.00000016339744826906044),
                new Vector3d(0.8944271909999159, 0.4472135954998624, -0.00000029229424134369956)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.4999999999997864, 0.0, 0.0), new Vector3d(
                0.00000000000042721381987576024, 0.9999999999997864, 0.0000006535897930761021)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.9999999999998932, 0.0, -0.00000016339744826902553), new Vector3d(
                -0.8944271909995337, 0.4472135954998624, 0.0000008768827240310362)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(0.0, 0.250000000000267, -0.00000016339744826888593), new Vector3d(
                0.9999999999997864, 0.0000000000004269917752713823, -0.0000006535897930759625)), new Object());
        map2.epsilonPut(new Line3d(new Point3d(1.500000000001495, 0.0, -0.0000004901923448068672), new Vector3d(
                -0.8944271909998394, 0.4472135954992511, 0.0000008768827240308366)), new Object());

        validateTree(map2);

    }

    protected void assertArraysEqual(String message, Object[] a1, Object[] a2)
    {
        assertTrue(message + " a1: " + Arrays.toString(a1) + "; a2: " + Arrays.toString(a2), Arrays.deepEquals(a1, a2));
    }

    protected void validateTree(TestableLineMap<?> map)
    {
        assertTrue("Invalid red-black tree: " + map.treeKeysToString(), map.isValidRedBlackTree());
    }

    public static void main(String[] args)
    {
        new LineMapTest().testTree();

        System.out.println("Success.");
    }
}
